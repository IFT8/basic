package me.ift8.basic.trace.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import me.ift8.basic.trace.core.enums.TraceTypeEnum;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2019-02-01.
 */
@Slf4j
public class TraceTest {
    private ThreadPoolExecutor traceCasePool =
            new ThreadPoolExecutor(10, 10, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(128), new ThreadFactoryBuilder().setNameFormat("trace-thread-%d").build());

    @After
    public void after() throws InterruptedException {
        TimeUnit.SECONDS.sleep(30);
    }

    @Test
    public void getMessageId() {
        String currentMessageId = Trace.getCurrentMessageIdOnly();

        log.info("{}", currentMessageId);
    }

    @Test
    public void tarceLogCase() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Trace.logEvent(TraceTypeEnum.LOG, "logEventLog");
            Trace.logEvent(TraceTypeEnum.CALL, "logEventCall");
            Trace.logMetricForCount("logMetricForCount");
            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }

    @Test
    public void poolCase() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            String currentMessageId = Trace.getCurrentMessageIdOnly();
            log.info("strat {} {}", i, currentMessageId);
            Trace.logEvent(TraceTypeEnum.LOG, "logEventLogStart");

            int finalI = i;
            traceCasePool.submit(() -> {
                log.info("1 {} {}", finalI, currentMessageId);

                Trace.logEvent(TraceTypeEnum.TASK, "logEventTask1");
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Trace.logEvent(TraceTypeEnum.TASK, "logEventTask2");
                log.info("2 {} {}", finalI, currentMessageId);
            });
            Trace.logEvent(TraceTypeEnum.LOG, "logEventLogEnd");

            log.info("end {} {}", finalI, currentMessageId);
            TimeUnit.MILLISECONDS.sleep(1000);
        }
    }

    @Test
    public void continueTrace() {
        TraceTypeEnum traceType = TraceTypeEnum.MQ_LISTEN;
        Trace.startNewTrace(traceType, getClass().getSimpleName());
        String msgId = Trace.getCurrentMessageIdOnly();
        log.info("currentMsgId:{}", msgId);

        for (int i = 0; i < 8; i++) {
            if ((i & 1) == 0) {
                Trace.continueTrace(msgId, msgId, traceType, getClass().getSimpleName());
            }
            log.info("msgId:{} pId:{} ,rootId:{}", msgId, Trace.getParentMessageId(), Trace.getRootMessageId());
        }
    }

    @Test
    public void continueTraceLog() {
        TraceTypeEnum traceType = TraceTypeEnum.MQ_LISTEN;
        Trace.startNewTrace(traceType, getClass().getSimpleName());
        String msgId = Trace.getCurrentMessageIdOnly();
        log.info("currentMsgId:{}", msgId);

        for (int i = 0; i < 8; i++) {
            Trace.continueTrace(msgId, msgId, traceType, getClass().getSimpleName());
            if((i&1)==0){
                Trace.logMetricForCount("great_gps_trace");
            }
            log.info("msgId:{} pId:{} ,rootId:{}", Trace.getCurrentMessageIdOnly(), Trace.getParentMessageId(), Trace.getRootMessageId());
        }
    }

    @Test
    public void tarceCasePool() throws InterruptedException {
        TraceTypeEnum traceType = TraceTypeEnum.CALL;

        for (int i = 0; i < 8; i++) {
            String currentMessageId = Trace.getCurrentMessageIdOnly();
            log.info("strat {} {}", i, currentMessageId);
            log.info("msgId:{} pId:{} ,rootId:{}", Trace.getCurrentMessageIdOnly(), Trace.getParentMessageId(), Trace.getRootMessageId());

            traceCasePool.submit(() -> {
                Trace.continueTrace(currentMessageId, currentMessageId, traceType, getClass().getSimpleName());
                log.info("msgId:{} pId:{} ,rootId:{}", Trace.getCurrentMessageIdOnly(), Trace.getParentMessageId(), Trace.getRootMessageId());
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("msgId:{} pId:{} ,rootId:{}", Trace.getCurrentMessageIdOnly(), Trace.getParentMessageId(), Trace.getRootMessageId());
            });

            log.info("msgId:{} pId:{} ,rootId:{}", Trace.getCurrentMessageIdOnly(), Trace.getParentMessageId(), Trace.getRootMessageId());

        }
    }
}