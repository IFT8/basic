package me.ift8.basic.metrics.test;

import me.ift8.basic.metrics.api.TestService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by IFT8 on 2017/5/8.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MetricsAspectTest {
    @Resource
    private TestService testService;

    @Test
    public void aspectTest() throws InterruptedException {

        for (int i = 0; i < 100; i++) {
            log.info(testService.aspect("aspect test"));
            Thread.sleep(1000);
        }
    }

}