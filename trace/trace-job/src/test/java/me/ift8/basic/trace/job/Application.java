package me.ift8.basic.trace.job;

import me.ift8.basic.job.SimpleJob;
import me.ift8.basic.metrics.MetricsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by zhuye on 16/01/2017.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"me.ift8.basic.*"})
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private MetricsUtils metricsUtils;

    @Bean
    public TraceJobAspect traceJobAspect() {
        return new TraceJobAspect(metricsUtils);
    }

    @Bean
    public SimpleJob simpleJob(){
        return new SimpleJob();
    }
}
