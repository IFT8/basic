package me.ift8.basic.ftp.client;


import me.ift8.basic.ftp.client.client.FtpClientConfig;
import me.ift8.basic.ftp.client.pool.FtpPoolConfig;
import me.ift8.basic.ftp.client.util.FtpUtils;
import me.ift8.basic.task.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
public class FtpTemplateTest {
    private PooledFtpManager ftpTemplate;
    private FtpClientConfig ftpClientConfig;

    private LongAdder failCount = new LongAdder();

    @Before
    public void init() {
        ftpClientConfig = new FtpClientConfig();

        ftpClientConfig.setHost("123.59.155.238");
        ftpClientConfig.setUsername("paydayloan");
        ftpClientConfig.setPassword("paydayloan_pass!@#");
        ftpClientConfig.setWorkingDir("/");

//        ftpClientConfig.setPort(2122);
//        ftpClientConfig.setHost("hz-sftp1.lianlianpay.com");
//        ftpClientConfig.setUsername("SH-QiAo");
//        ftpClientConfig.setPassword("APgdlbY5");
//        ftpClientConfig.setWorkingDir("/SH-QiAo/201704281001688843");
//        ftpClientConfig.setUseSecure(true);

        FtpPoolConfig poolConfig = new FtpPoolConfig();
        poolConfig.setMaxTotal(50);
        poolConfig.setMaxIdle(50);
        poolConfig.setMinIdle(20);

        ftpTemplate = new PooledFtpManager(ftpClientConfig);
    }

    private String uploadFileSignalUsePool() {
        String fileName = RandomUtils.nextInt(1, 10) + ".html";
        File file = new File("/Users/IFT8/Desktop/" + fileName);
        String path = "test2/agreement/loan/";
        try {
            String randomPath = path + RandomUtils.nextInt(1, 10000);
            log.info("random path:{}", randomPath);

            String uploadAbsolutePath = ftpTemplate.uploadFile(file.getName() + RandomUtils.nextInt(1, 10000), new FileInputStream(file), path);

            //check
            File uploadedFile = new File(ftpClientConfig.getWorkingDir() + "/" + path + "/" + fileName);
            log.info("文件{} 是否真正成功={}", uploadedFile.getAbsolutePath(), uploadedFile.exists() ? "成功" : "!失败!");
            if (!uploadedFile.exists()) {
                failCount.increment();
            }
            return uploadAbsolutePath;
        } catch (IOException e) {
            log.error("异常", e);
        }
        return path;
    }

    private String uploadFileSignal() {
        long start = System.currentTimeMillis();
        String fileName = RandomUtils.nextInt(1, 10) + ".html";
        File file = new File("/Users/IFT8/Desktop/" + fileName);
        String path = "test/agreement/loan/";
        try {
            String randomPath = path + RandomUtils.nextInt(1, 10000);
            log.info("random path:{}", randomPath);


            FTPClient client = FtpUtils.create(ftpClientConfig);

            //创建目录如果需要
            FtpUtils.mkdirs(client, randomPath);
            //上传
            boolean success = FtpUtils.uploadSignal(client, path, fileName, new FileInputStream(file));

            String uploadAbsolutePath = randomPath + "/" + fileName;
            log.info("[文件上传结束]:success={}, fileName={} , uploadPath={}, 耗时={}ms", success, fileName, path, System.currentTimeMillis() - start);

            if (!success) {
                failCount.increment();
            }
            return uploadAbsolutePath;
        } catch (IOException e) {
            log.error("异常", e);
        }
        return path;
    }

    @Test
    public void uploadFileUsePool() throws InterruptedException, IOException {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            TaskManager.addTask(this::uploadFileSignalUsePool);
        }

        TaskManager.shutdownAndAwait();
        log.info("上传[失败]次数={} 总共耗时{}ms", failCount.toString(), System.currentTimeMillis() - start);
    }

    @Test
    public void uploadFile() throws InterruptedException {
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1; i++) {
            TaskManager.addTask(this::uploadFileSignal);
        }

        TaskManager.shutdownAndAwait();
        log.info("上传[失败]次数={} 总共耗时{}ms", failCount.toString(), System.currentTimeMillis() - start);
    }

    @Test
    public void download() throws Exception {
        String filePath = "sftp://SH-QiAo@hz-sftp1.lianlianpay.com:2122/SH-QiAo/201704281001688843/RHZZW_201704281001688843_20170713.csv";
        for (int i = 0; i < 50; i++) {
            TaskManager.addTask(() -> downloadFun(filePath));
            TaskManager.addTask(this::uploadFileSignalUsePool);
        }
        TaskManager.shutdownAndAwait();
    }

    @Test
    public void downloadS() throws Exception {
        String filePath = "RHZZW_201704281001688843_20170713.csv";
        downloadFun(filePath);
    }

    private void download(String filePath) {
        try {
            FTPClient client = ftpTemplate.getFtpClient();
            try (InputStream inputStream = client.retrieveFileStream(filePath)) {
                if (inputStream == null) {
                    log.warn("文件流获取失败[FTP异常] filePath={}", filePath);
                    return;
                }

                int available = inputStream.available();
                client.getReply();
                log.info("available={}", available);
            }
            //返回Pool
            ftpTemplate.returnFtpClient(client);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    private void downloadFun(String filePath) {
        try {
            ftpTemplate.executeAndReply(ftpClient -> {
                try (InputStream inputStream = ftpClient.retrieveFileStream(filePath)) {
                    if (inputStream == null) {
                        log.warn("文件流获取失败[FTP异常] filePath={}", filePath);
                        return null;
                    }

                    int available = inputStream.available();
                    log.info("available={}", available);
                    return available;
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
