package me.ift8.basic.metrics;

import com.dianping.cat.Cat;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2019-02-01.
 */
public class CatTest {

    @After
    public void after() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void logMetricForCount() throws InterruptedException {
        Cat.logMetricForCount("3ji2o", 123);
    }

    @Test
    public void logEvent() {
        Cat.logEvent("URL", "local");

        String currentMessageId = Cat.getCurrentMessageId();
        System.out.println(currentMessageId);
    }
}