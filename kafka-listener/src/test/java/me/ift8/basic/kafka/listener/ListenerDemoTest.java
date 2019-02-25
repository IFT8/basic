package me.ift8.basic.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by IFT8 on 2018-12-21.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"me.ift8"})
public class ListenerDemoTest {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ListenerDemoTest.class, args);
    }
}