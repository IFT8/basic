package me.ift8.basic.http.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by IFT8 on 2017/9/15.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientFactory {
    //默认超时30s
    private static final int DEFAULT_TIMEOUT = 30000;

    public static CloseableHttpClient getHttpClient() {
        return getHttpClient(null, null, null);
    }

    public static CloseableHttpClient getHttpClient(Boolean enableSSL) {
        return getHttpClient(null, null, enableSSL);
    }

    public static CloseableHttpClient getHttpClient(Integer timeout, Boolean enableSSL) {
        return getHttpClient(timeout, timeout, enableSSL);
    }

    public static CloseableHttpClient getHttpClient(Integer soTimeout, Integer connectionTimeout, Boolean enableSSL) {
        //默认配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间
                .setSocketTimeout(soTimeout == null ? DEFAULT_TIMEOUT : soTimeout)
                //连接上服务器(握手成功)的时间
                .setConnectTimeout(connectionTimeout == null ? DEFAULT_TIMEOUT : connectionTimeout)
                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接
                .setConnectionRequestTimeout(connectionTimeout == null ? DEFAULT_TIMEOUT : connectionTimeout)
                .build();

        HttpClientBuilder httpClientBuilder = createHttpClientBuilder(defaultRequestConfig);

        return httpClientBuilder.build();
    }

    static HttpClientBuilder createHttpClientBuilder(RequestConfig defaultRequestConfig) {
        //Socket Config
        SocketConfig defaultSocketConfig = SocketConfig.custom()
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .setSoReuseAddress(true)
                .build();

        //HTTPv1.1 在请求时候设置

        return HttpClients.custom()
                //默认请求配置
                .setDefaultRequestConfig(defaultRequestConfig)
                .setDefaultSocketConfig(defaultSocketConfig)
                //Debug测试代理
//                .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost("localhost",8888)))
                ;
    }
}
