package me.ift8.basic.metrics.test;

import me.ift8.basic.metrics.MetricsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhuye on 09/06/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@SuppressWarnings("SpringJavaAutowiringInspection")
public class MetricsTest {

    private static Random random = new Random();
    @Autowired
    private MetricsUtils metricsUtils;

    @Test
    public void metricsUtilsTest() {

        for (int i = 0; i < 10000; i++) {
            long begin = System.currentTimeMillis();
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Map.Entry<String, String> tag = new AbstractMap.SimpleEntry("userId", "11");
            metricsUtils.success(MetricsTest.class, "metricsUtilsTest", "job1", begin, tag);
            if (random.nextInt(100) < 10) {
                metricsUtils.serviceFail(MetricsTest.class, "metricsUtilsTest", "job1", begin);
                metricsUtils.systemFail(MetricsTest.class, "metricsUtilsTest", "job1", begin);
            }
//            metricsUtils.successMultipleTimes(MetricsTest.class, "metricsUtilsTest", "multi", 2, begin);
//            metricsUtils.serviceFailMultipleTimes(MetricsTest.class, "metricsUtilsTest", "multi", 2, begin);
//            metricsUtils.systemFailMultipleTimes(MetricsTest.class, "metricsUtilsTest", "multi", 2, begin);
        }

    }
}
