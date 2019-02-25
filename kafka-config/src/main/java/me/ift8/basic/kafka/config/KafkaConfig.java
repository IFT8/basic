package me.ift8.basic.kafka.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Slf4j
@Data
@Configuration
@EnableConfigurationProperties(KafkaConfigProperties.class)
public class KafkaConfig {
}
