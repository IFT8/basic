package me.ift8.basic.ftp.client.client;

import me.ift8.basic.ftp.client.util.FtpUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPCmd;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.io.IOException;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FtpClientFactory implements PooledObjectFactory<FTPClient> {

    private FtpClientConfig clientConfig;

    public FtpClientFactory(FtpClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public PooledObject<FTPClient> makeObject() throws Exception {
        FTPClient client = FtpUtils.create(clientConfig);
        return new DefaultPooledObject<>(client);
    }


    @Override
    public void destroyObject(PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient cli = pooledObject.getObject();

        if (cli == null) {
            return;
        }

        if (cli.isConnected()) {
            cli.logout();
            cli.disconnect();
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> pooledObject) {
        FTPClient cli = pooledObject.getObject();

        boolean isConnected = cli.isConnected();
        boolean isAvailable = cli.isAvailable();

        if (!isConnected || !isAvailable) {
            log.warn("[连接失效] isConnected={},isAvailable={}", isConnected, isAvailable);
            return false;
        }

        try {
            // 心跳
            cli.sendCommand(FTPCmd.NOOP);
            return true;
        } catch (IOException e) {
            log.error("[心跳检测失败]", e);
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<FTPClient> pooledObject) throws Exception {
    }
}
