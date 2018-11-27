package me.ift8.basic.metrics.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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


}
