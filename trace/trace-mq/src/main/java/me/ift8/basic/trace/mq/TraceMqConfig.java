package me.ift8.basic.trace.mq;

import me.ift8.basic.metrics.MetricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Configuration
public class TraceMqConfig {
    @Autowired(required = false)
    private MetricsUtils metricsUtils;

    @Bean
    public TraceMqListenerAspect traceMqListenerAspect() {
        return new TraceMqListenerAspect(metricsUtils);
    }

    @Bean
    public TraceMqSenderAspect traceMqSenderAspect() {
        return new TraceMqSenderAspect(metricsUtils);
    }
}
