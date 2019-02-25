package me.ift8.basic.trace.mq;

import me.ift8.basic.metrics.MetricsUtils;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.mq.constant.MessageContext;
import me.ift8.basic.mq.constant.TraceContext;
import me.ift8.basic.trace.aop.utils.TraceAopUtils;
import me.ift8.basic.trace.core.Trace;
import me.ift8.basic.trace.core.enums.TraceTypeEnum;
import me.ift8.basic.trace.mq.utils.MetricMqUtils;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zhuye on 09/06/2017.
 */
@Slf4j
@Aspect
public class TraceMqListenerAspect {
    private MetricsUtils metricsUtils;
    private boolean hasMetricsUtils;

    public TraceMqListenerAspect() {
    }

    public TraceMqListenerAspect(MetricsUtils metricsUtils) {
        this.metricsUtils = metricsUtils;
    }

    @PostConstruct
    private void init() {
        hasMetricsUtils = metricsUtils != null;
        log.info("[aop] TraceMqListenerAspect 加载完成 hasMetricsUtils:{}", hasMetricsUtils);
    }

    @Around("execution(* me.ift8.basic.*.listener.AbstractMessageHandler.handle(..))")
    public Object aroundListen(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        MessageBody messageBody = null;
        for (Object arg : args) {
            boolean isMsgBodyClass = arg instanceof MessageBody;
            if (isMsgBodyClass) {
                messageBody = (MessageBody) arg;
                break;
            }
        }
        String className = getClass().getName();
        String rootId = null;
        String parentId = null;
        String topic = null;
        MessageContext context = null;
        if (messageBody != null) {
            context = Optional.ofNullable(messageBody.getContext()).orElse(new MessageContext());
            topic = context.getTopic();

            TraceContext traceContext = Optional.ofNullable(context.getTraceContext()).orElse(new TraceContext());
            rootId = traceContext.getRootTraceId();
            parentId = traceContext.getTraceId();
        }
        String traceName = className + "|" + topic + ":listen";
        Map<String, String> tagMap = MetricMqUtils.buildTagMapByMqContext(context);

        Transaction transaction = Trace.continueTrace(rootId, parentId, TraceTypeEnum.MQ_LISTEN, traceName);
        Trace.logMetricForCount(traceName);

        return TraceAopUtils.proceeAndTrace(point, metricsUtils, traceName, tagMap, transaction);
    }
}

