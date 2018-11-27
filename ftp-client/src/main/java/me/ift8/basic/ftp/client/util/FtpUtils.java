package me.ift8.basic.ftp.client.util;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.ftp.client.client.FtpClientConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

/**
 * Created by IFT8 on 2017/5/21.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FtpUtils {
    //CAS 创建目录超时毫秒数
    private static final int DEAFULT_TIMEOUT_MS = 500;
    //CAS 创建目录间隔毫秒数
    private static final int DEAFULT_INTERVAL_MS = 50;
    private static final String LOCAL_ENCODING = "UTF-8";

    /**
     * 创建FTPClient
     *
     * @param clientConfig 配置文件
     */
    public static FTPClient create(FtpClientConfig clientConfig) throws IOException {
        FTPClient client = createFTPClient(clientConfig.getUseSecure());
        client.setBufferSize(clientConfig.getBufferSize());

        FTPClientConfig ftpClientConfig = new FTPClientConfig();
        ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
        client.setControlEncoding(LOCAL_ENCODING);
        client.configure(ftpClientConfig);

        //连接
        long startTime = System.currentTimeMillis();
        client.setConnectTimeout(clientConfig.getConnectTimeout());
        client.setDataTimeout(clientConfig.getDataTimeout());
        client.connect(clientConfig.getHost(), clientConfig.getPort());
        if (log.isDebugEnabled()) {
            log.debug("[建立连接] clientConfig={} , 耗时={}ms", clientConfig, System.currentTimeMillis() - startTime);
        }
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            log.error("[建立连接失败]" + clientConfig.toString());
            return null;
        }

        // 设置传输协议
        client.enterLocalPassiveMode();

        //登录
        startTime = System.currentTimeMillis();
        boolean loginSuccess = client.login(clientConfig.getUsername(), clientConfig.getPassword());
        if (log.isDebugEnabled()) {
            log.debug("[登录] success={} , clientConfig={} , 耗时={}ms", loginSuccess, clientConfig.getWorkingDir(), System.currentTimeMillis() - startTime);
        }

        if (!loginSuccess) {
            log.error("[登录失败] : " + clientConfig.toString());
            return null;
        }

        //切换工作目录
        boolean success = client.changeWorkingDirectory(clientConfig.getWorkingDir());
        if (log.isDebugEnabled()) {
            log.debug("[切换工作目录] workingDir={}, success={}", clientConfig.getWorkingDir(), success);
        }
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        return client;
    }

    private static FTPClient createFTPClient(boolean useSecure) {
        if (!useSecure) {
            return new FTPClient();
        }
        try {
            FTPSClient client = new FTPSClient(false);
            client.setNeedClientAuth(false);
            client.setUseClientMode(false);
            return client;
        } catch (Exception e) {
            log.error("FTPSClient创建失败[系统异常]", e);
            throw ErrorMessage.SYS_ERROR.getSystemException();
        }
    }

    /**
     * 创建目录支持层级
     *
     * @see File#mkdirs()
     */
    public static boolean mkdirs(FTPClient client, String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            log.warn("mkdirs[失败] path为空");
            return false;
        }

        //存在 则创建成功
        if (existDir(client, path)) {
            return true;
        }
        //创建成功
        if (mkdir(client, path)) {
            return true;
        }

        //层级目录创建
        String parentDir = getParentDir(path);
        //创建父目录 && 以及当前目录
        return parentDir != null && mkdirs(client, parentDir) && mkdir(client, path);
    }

    /**
     * 是否存在目录
     */
    public static boolean existDir(FTPClient client, String dirPath) throws IOException {
        boolean success = client.changeWorkingDirectory(dirPath);
        if (log.isDebugEnabled()) {
            log.debug("[切换目录] success={} , path:{}, currentDirectory={}", success, dirPath, client.printWorkingDirectory());
        }
        return success;
    }

    /**
     * 单文件上传
     *
     * @param uploadPath
     */
    public static boolean uploadSignal(FTPClient client, String uploadPath, String fileName, InputStream input) {
        try {
            boolean success = client.storeFile(uploadPath + "/" + fileName, input);
            if (!success) {
                log.error("uploadSignal [失败] fileName={} ,replyString={} ,uploadPath={} ,printWorkingDirectory={}", fileName, client.getReplyString(), uploadPath, client.printWorkingDirectory());
            }
            return success;
        } catch (IOException e) {
            log.error("uploadSignal [异常] fileName={} , host={}", fileName, client.getPassiveHost(), e);
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignore) {
                    input = null;
                }
            }
        }
    }

    public static void casMkdirs(FTPClient client, String workingDirectory, String path) throws IOException {
        casMkdirs(client, workingDirectory, path, DEAFULT_TIMEOUT_MS, DEAFULT_INTERVAL_MS);
    }

    /**
     * CAS 创建目录
     */
    public static void casMkdirs(FTPClient client, String workingDirectory, String path, int timeoutMs, int intervalMs) throws IOException {
        do {
            //创建目录如果需要
            if (FtpUtils.mkdirs(client, path)) {
                return;
            }
            //还原工作目录
            client.changeWorkingDirectory(workingDirectory);

            timeoutMs -= intervalMs;
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                log.error("casMkdirs [中断异常]", e);
            }

        } while (timeoutMs >= 0);
        //目录不存在 且创建失败
        log.error("FTP创建目录失败 [系统异常] workingDirectory={} , path={}", workingDirectory, path);
        throw ErrorMessage.SYS_ERROR.getSystemException("FTP创建目录失败");
    }

    /**
     * 创建目录
     */
    private static boolean mkdir(FTPClient client, String dirPath) throws IOException {
        boolean success = client.makeDirectory(dirPath);
        if (log.isDebugEnabled()) {
            log.debug("[创建目录] success:{} ,path:{}", success, dirPath);
        }
        return success;
    }

    /**
     * 获取父目录
     *
     * @see File#getParent()
     */
    private static String getParentDir(String dirPath) {
        int index = dirPath.lastIndexOf('/');
        if (index < prefixLength(dirPath)) {
            if ((prefixLength(dirPath) > 0) && (dirPath.length() > prefixLength(dirPath)))
                return dirPath.substring(0, prefixLength(dirPath));
            return null;
        }
        return dirPath.substring(0, index);
    }

    /**
     * 获取文件路径前缀长度 (文件系统相关)
     *
     * @see java.io.UnixFileSystem#prefixLength(java.lang.String)
     */
    private static int prefixLength(String pathname) {
        if (pathname.length() == 0) {
            return 0;
        }
        return (pathname.charAt(0) == '/') ? 1 : 0;
    }
}
