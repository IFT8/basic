package me.ift8.basic.ftp.client;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.ftp.client.client.FtpClientConfig;
import me.ift8.basic.ftp.client.pool.FtpClientPool;
import me.ift8.basic.ftp.client.pool.FtpPoolConfig;
import me.ift8.basic.ftp.client.util.FtpUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PooledFtpManager {

    private FtpClientPool ftpClientPool;
    private String workingDirectory;

    public PooledFtpManager(FtpClientConfig clientConfig) {
        ftpClientPool = new FtpClientPool(clientConfig);
        workingDirectory = clientConfig.getWorkingDir();
    }

    public PooledFtpManager(FtpPoolConfig ftpPoolConfig, FtpClientConfig clientConfig) {
        ftpClientPool = new FtpClientPool(ftpPoolConfig, clientConfig);
        workingDirectory = clientConfig.getWorkingDir();
    }

    /**
     * 上传文件
     *
     * @param file               上传文件
     * @param uploadPath 上传工作目录(相对路径为相对于工作路径)
     */
    public String uploadFile(File file, String uploadPath) throws IOException {
        return uploadFile(file.getName(), new FileInputStream(file), uploadPath);
    }

    /**
     * 上传文件
     *
     * @param fileName   上传文件名称
     * @param input      上传的流
     * @param uploadPath 上传工作目录(相对路径为相对于工作路径)
     */
    public String uploadFile(String fileName, InputStream input, String uploadPath) throws IOException {
        long start = System.currentTimeMillis();
        if (log.isDebugEnabled()) {
            log.debug("[文件上传开始]: fileName={} ,uploadPath={}", fileName, uploadPath);
        }
        FTPClient client = getFtpClient();
        //如果是相对路径 则转成绝对路径(避免切换路径问题)
        String uploadAbsolutePath = uploadPath;
        if (!uploadPath.startsWith("/")) {
            uploadAbsolutePath = workingDirectory + "/" + uploadPath;
        }

        boolean success;
        try {
            //创建目录如果需要
            FtpUtils.casMkdirs(client, workingDirectory, uploadAbsolutePath);
            //上传
            success = FtpUtils.uploadSignal(client, uploadAbsolutePath, fileName, input);
        } catch (IOException e) {
            log.error("文件上传 [系统异常] fileName={}", fileName, e);
            //无效化
            invalidateFTPClient(client);
            client = null;
            throw e;
        } finally {
            //返回Pool
            if (client != null) {
                returnFtpClient(client);
            }
        }

        if (!success) {
            throw ErrorMessage.SYS_ERROR.getSystemException();
        }

        String path = uploadAbsolutePath + "/" + fileName;
        log.info("[文件上传结束]: fileName={} , uploadPath={}, 耗时={}ms", fileName, path, System.currentTimeMillis() - start);

        return path;
    }

    /**
     * 返回Pool
     */
    public void returnFtpClient(FTPClient client) {
        ftpClientPool.returnObject(client);
    }

    /**
     * 从Pool取FTPClient
     */
    public FTPClient getFtpClient() {
        try {
            return ftpClientPool.borrowObject();
        } catch (Exception e) {
            log.error("从Pool获取FTPClient失败[系统异常] ", e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }

    /**
     * 无效化FTPClient
     */
    private void invalidateFTPClient(FTPClient client) {
        try {
            log.warn("无效化FTPClient");
            ftpClientPool.invalidateObject(client);
        } catch (Exception e) {
            log.error("无效化FTPClient失败[系统异常] ", e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }

    /**
     * client内执行
     */
    private <T> T execute(FTPCallback<T> fun, boolean reply) throws Exception {
        FTPClient client = getFtpClient();
        try {
            return fun.execute(client);
        } catch (IOException e) {
            log.error("FTPCallback[执行失败]  ", e);
            //无效化
            invalidateFTPClient(client);
            client = null;
            throw e;
        } finally {
            //返回Pool
            if (client != null) {
                //完成回执
                try {
                    if (reply) {
                        client.getReply();
                    }
                } catch (Exception e) {
                    log.error("FTPCallback[回执失败]  ", e);
                }
                returnFtpClient(client);
            }
        }
    }

    /**
     * client内执行
     *
     * @return fun的返回值
     * @throws Exception fun的异常 以及可能的IOException
     */
    public <T> T execute(FTPCallback<T> fun) throws Exception {
        return execute(fun, false);
    }

    /**
     * client内执行(主要是下载 流操作 需要回执)
     *
     * @return fun的返回值
     * @throws Exception fun的异常 以及可能的IOException
     */
    public <T> T executeAndReply(FTPCallback<T> fun) throws Exception {
        return execute(fun, true);
    }
}
