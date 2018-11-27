package me.ift8.basic.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

/**
 * Created by IFT8 on 2017/6/3.
 */
@Order(1)
@Configuration
public class ObjectMapperConfig {

    @Bean
    @Profile("prod")
    public ObjectMapper objectMapper() {
        ObjectMapper prodObjectMapper = baseObjectMapper();
        prodObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return prodObjectMapper;
    }

    @Bean
    @Profile({"dev", "qa"})
    public ObjectMapper devObjectMapper() {
        return baseObjectMapper();
    }

    private ObjectMapper baseObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //未知枚举当做NULL
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
                //未知字段忽略
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                //J8时间
                .registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module());

        return objectMapper;
    }
}
