package me.ift8.basic.trace.job;

import me.ift8.basic.metrics.MetricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Configuration
public class TraceJobConfig {
    @Autowired(required = false)
    private MetricsUtils metricsUtils;

    @Bean
    public TraceJobAspect traceJobAspect() {
        return new TraceJobAspect(metricsUtils);
    }
}
