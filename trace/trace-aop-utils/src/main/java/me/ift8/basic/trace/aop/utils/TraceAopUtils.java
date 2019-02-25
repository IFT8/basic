package me.ift8.basic.trace.aop.utils;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.metrics.MetricsUtils;
import me.ift8.basic.trace.core.Trace;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Map;

/**
 * Created by IFT8 on 2019-02-21.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TraceAopUtils {

    public static Object proceeAndTrace(ProceedingJoinPoint point, MetricsUtils metricsUtils, String traceName, Map<String, String> tagMap, Transaction transaction) throws Throwable {
        long begin = System.currentTimeMillis();
        boolean hasMetricsUtils = metricsUtils != null;
        try {
            Object result = point.proceed();
            transaction.setStatus(Message.SUCCESS);

            Trace.logMetricForCount(traceName + "-SUCCESS");
            if (hasMetricsUtils) {
                metricsUtils.success(traceName, begin, tagMap);
            }

            return result;
        } catch (Throwable e) {
            Trace.logError(e);
            transaction.setStatus(e);

            if (hasMetricsUtils) {
                if (e instanceof ServiceException) {
                    metricsUtils.serviceFail(traceName, begin, tagMap);
                }
                metricsUtils.systemFail(traceName, begin, tagMap);
            }
            throw e;
        } finally {
            transaction.complete();
            if (hasMetricsUtils) {
                metricsUtils.total(traceName, begin, tagMap);
            }
        }
    }
}
