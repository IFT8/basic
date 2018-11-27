package me.ift8.basic.ftp.client.pool;

import me.ift8.basic.ftp.client.client.FtpClientConfig;
import me.ift8.basic.ftp.client.client.FtpClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
public class FtpClientPool extends GenericObjectPool<FTPClient> {

    private FtpClientConfig clientConfig;

    public FtpClientPool(FtpClientConfig clientConfig) {
        super(new FtpClientFactory(clientConfig), new me.ift8.basic.ftp.client.pool.FtpPoolConfig());
        this.clientConfig = clientConfig;
    }

    public FtpClientPool(me.ift8.basic.ftp.client.pool.FtpPoolConfig ftpPoolConfig, FtpClientConfig clientConfig) {
        super(new FtpClientFactory(clientConfig), ftpPoolConfig);
        this.clientConfig = clientConfig;
    }

    @Override
    public FTPClient borrowObject() throws Exception {
        FTPClient client = super.borrowObject();

        if (log.isDebugEnabled()) {
            String currentPath = client.printWorkingDirectory();
            log.debug("[取出时重置工作目录] workingDir={}, currentPath={} ", clientConfig.getWorkingDir(), currentPath);
        }

        client.changeWorkingDirectory(clientConfig.getWorkingDir());
        return client;
    }
}
