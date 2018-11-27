package me.ift8.basic.http.client;

import me.ift8.basic.constants.ErrorMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;

/**
 * Created by IFT8 on 16/8/23.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PoolingHttpClientManager {
    private final Registry<ConnectionSocketFactory> SOCKET_FACTORY_REGISTRY = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE).register("https", trustAllHttpsCertificates()).build();
    private final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(SOCKET_FACTORY_REGISTRY);
    private CloseableHttpClient client;

    //默认超时30s
    private static final int DEFAULT_TIMEOUT = 30000;

    @Deprecated
    public PoolingHttpClientManager(Integer maxConnTotal, Integer soTimeout, Integer connectionTimeout, Boolean enableSSL) {
        init(maxConnTotal, soTimeout, connectionTimeout);
    }

    public PoolingHttpClientManager(Integer maxConnTotal, Integer soTimeout, Integer connectionTimeout) {
        init(maxConnTotal, soTimeout, connectionTimeout);
    }

    private void init(Integer maxConnTotal, Integer soTimeout, Integer connectionTimeout) {
        //设置连接数
        setCMMaxConnTotal(maxConnTotal);

        //默认配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间
                .setSocketTimeout(soTimeout == null ? DEFAULT_TIMEOUT : soTimeout)
                //连接上服务器(握手成功)的时间
                .setConnectTimeout(connectionTimeout == null ? DEFAULT_TIMEOUT : connectionTimeout)
                //从连接池中获取连接的超时时间，超过该时间未拿到可用连接
                .setConnectionRequestTimeout(connectionTimeout == null ? DEFAULT_TIMEOUT : connectionTimeout)
                .build();

        //10s 检查存活
        cm.setValidateAfterInactivity(10000);

        HttpClientBuilder httpClientBuilder = HttpClientFactory.createHttpClientBuilder(defaultRequestConfig);
        httpClientBuilder.setConnectionManager(cm);

        client = httpClientBuilder.build();
    }

    private void setCMMaxConnTotal(Integer maxConnTotal) {
        if (maxConnTotal == null) {
            return;
        }

        //设置连接数
        cm.setMaxTotal(maxConnTotal);
        //路由最大连接
        cm.setDefaultMaxPerRoute(maxConnTotal);
    }

    public CloseableHttpClient getHttpClient() {
        return client;
    }

    public PoolStats getTotalStats() {
        return this.cm.getTotalStats();
    }

    private static SSLConnectionSocketFactory trustAllHttpsCertificates() {
        try {
            //信任所有
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (chain, authType) -> true).build();
            return new SSLConnectionSocketFactory(sslContext);
        } catch (Exception e) {
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }
}
