package me.ift8.basic.trace.job;

import me.ift8.basic.job.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TraceJobAspectTest {
    @Resource
    private SimpleJob simpleJob;

    @Test
    public void jobAopTest() throws Exception {
        simpleJob.execute(null);
    }
}