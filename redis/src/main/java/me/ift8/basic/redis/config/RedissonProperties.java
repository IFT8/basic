package me.ift8.basic.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by IFT8 on 2018/4/11.
 */
@Data
@ConfigurationProperties(prefix = "spring.redis")
public class RedissonProperties {
    private int database = 0;
    private String url;
    private String host = "localhost";
    private String password;
    private int port = 6379;
    private boolean ssl;
    private Sentinel sentinel;
    private RedissonPool redissonPool;

    @Data
    public static class Sentinel {
        private String master;
        private List<String> nodes;
    }

    @Data
    public static class RedissonPool {
        private int subscriptionPoolSize = 50;
        private int minIdleSize = 32;
        private int connectionPoolSize = 64;
        private int slaveConnectionPoolSize = 64;
    }
}
