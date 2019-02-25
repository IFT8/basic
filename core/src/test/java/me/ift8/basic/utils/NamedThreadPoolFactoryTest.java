package me.ift8.basic.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2019-01-14.
 */
@Slf4j
public class NamedThreadPoolFactoryTest {

    @Test
    public void sleepPolicyTest1() {
        ThreadPoolExecutor threadPoolExecutor = NamedThreadPoolFactory.namedThreadPoolExecutor(2, 4, "threadPoolExecutor", new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                while (executor.getQueue().size() >= 4) {
                    try {
                        log.info("wait executor.getTaskCount():{}", executor.getTaskCount());
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        log.error("中断异常", e);
                    }
                }
            }
        });

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(() -> log.info("doing"));
        }
    }
}
