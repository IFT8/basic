package me.ift8.basic.metrics;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Created by zhuye on 11/06/2017.
 */
@Slf4j
@ToString
@Configuration
public class MetricsConfig {
    @Value("${common.appId:}")
    private String appId;
    @Value("${metrics.url:}")
    private String url;
    @Value("${metrics.username:}")
    private String username;
    @Value("${metrics.password:}")
    private String password;
    @Value("${metrics.database:}")
    private String database;

    @PostConstruct
    private void init(){
        log.info("[config] MetricsConfig={}", this);
    }

    @Bean
    public MetricsClient metricsClient() {
        //允许不打Influx点
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        return new MetricsClient(url, username, password, database);
    }

    @Bean
    public MetricsUtils metricsUtils() {
        //允许不打Influx点
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        return new MetricsUtils(appId, metricsClient());
    }

    @Bean
    public MetricsAspect metricsAspect() {
        return new MetricsAspect(metricsUtils());
    }

}
