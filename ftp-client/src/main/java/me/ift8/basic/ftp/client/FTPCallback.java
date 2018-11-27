package me.ift8.basic.ftp.client;


import org.apache.commons.net.ftp.FTPClient;

/**
 * Created by IFT8 on 2017/5/23.
 */
@FunctionalInterface
public interface FTPCallback<T> {
    T execute(FTPClient client) throws Exception;
}
