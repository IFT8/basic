package me.ift8.basic.redis.config;

import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {
    @Resource
    private RedissonProperties redissonProperties;

    private Config config = new Config();

    @PostConstruct
    private void init() {
        RedissonProperties.RedissonPool pool = redissonProperties.getRedissonPool();
        if (pool == null) {
            pool = new RedissonProperties.RedissonPool();
        }

        //sentinel
        if (redissonProperties.getSentinel() != null) {
            SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
            sentinelServersConfig.setMasterName(redissonProperties.getSentinel().getMaster());
            List<String> nodes = redissonProperties.getSentinel().getNodes();
            sentinelServersConfig.addSentinelAddress(nodes.toArray(new String[1]));
            sentinelServersConfig.setDatabase(redissonProperties.getDatabase());
            if (redissonProperties.getPassword() != null) {
                sentinelServersConfig.setPassword(redissonProperties.getPassword());
            }

            sentinelServersConfig.setMasterConnectionPoolSize(pool.getConnectionPoolSize());
            sentinelServersConfig.setSlaveConnectionPoolSize(pool.getSlaveConnectionPoolSize());
            sentinelServersConfig.setSubscriptionConnectionPoolSize(pool.getSubscriptionPoolSize());

            sentinelServersConfig.setMasterConnectionMinimumIdleSize(pool.getMinIdleSize());
            sentinelServersConfig.setSlaveConnectionMinimumIdleSize(pool.getMinIdleSize());

        } else { //single server
            SingleServerConfig singleServerConfig = config.useSingleServer();
            // format as redis://127.0.0.1:7181 or rediss://127.0.0.1:7181 for SSL
            String schema = redissonProperties.isSsl() ? "rediss://" : "redis://";
            singleServerConfig.setAddress(schema + redissonProperties.getHost() + ":" + redissonProperties.getPort());
            singleServerConfig.setDatabase(redissonProperties.getDatabase());

            singleServerConfig.setConnectionPoolSize(pool.getConnectionPoolSize());
            singleServerConfig.setSubscriptionConnectionPoolSize(pool.getSubscriptionPoolSize());
            singleServerConfig.setConnectionMinimumIdleSize(pool.getMinIdleSize());

            if (redissonProperties.getPassword() != null) {
                singleServerConfig.setPassword(redissonProperties.getPassword());
            }
        }

        String configJson;
        try {
            configJson = config.toJSON();
        } catch (IOException ignore) {
            configJson = JsonUtils.toJson(config);
        }

        log.info("[config] RedissonAutoConfiguration redissonProperties={} configJson={}", redissonProperties, configJson);
    }

    @Bean
    public RedissonClient redissonClient() {
        return Redisson.create(config);
    }
}