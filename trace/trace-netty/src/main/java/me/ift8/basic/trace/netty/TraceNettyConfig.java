package me.ift8.basic.trace.netty;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Configuration
public class TraceNettyConfig {
    @Bean
    public TraceNettyAspect traceNettyAspect(){
        return new TraceNettyAspect();
    }
}
