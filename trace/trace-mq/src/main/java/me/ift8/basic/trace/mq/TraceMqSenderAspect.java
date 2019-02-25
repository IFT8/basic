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

/**
 * Created by zhuye on 09/06/2017.
 */
@Slf4j
@Aspect
public class TraceMqSenderAspect {
    private MetricsUtils metricsUtils;
    private boolean hasMetricsUtils;

    public TraceMqSenderAspect() {
    }

    public TraceMqSenderAspect(MetricsUtils metricsUtils) {
        this.metricsUtils = metricsUtils;
    }

    @PostConstruct
    private void init() {
        hasMetricsUtils = metricsUtils != null;
        log.info("[aop] TraceMqSenderAspect 加载完成 hasMetricsUtils:{}", hasMetricsUtils);
    }

    @Around("execution(public * me.ift8.basic.*.sender.MessageSender.sendMQ(..)) || execution(public * me.ift8.basic.*.sender.MessageSender.asyncSendMQWithJson(..)) || execution(public * me.ift8.basic.*.sender.MessageSender.sendMQWithJson(..))")
    public Object aroundSend(ProceedingJoinPoint point) throws Throwable {
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

        if (messageBody == null) {
            return point.proceed();
        }
        MessageContext context = messageBody.getContext();
        if (context == null) {
            context = new MessageContext();
        }

        String traceName = className + "|" + context.getTopic() + ":send";

        TraceContext traceContext = context.getTraceContext();
        if (traceContext == null) {
            traceContext = new TraceContext();
            context.setTraceContext(traceContext);
        }

        Map<String, String> tagMap = MetricMqUtils.buildTagMapByMqContext(context);

        Transaction transaction = Trace.newTransaction(TraceTypeEnum.MQ_SEND, traceName);

        traceContext.setParentTraceId(Trace.getParentMessageId());
        traceContext.setRootTraceId(Trace.getRootMessageId());
        traceContext.setTraceId(Trace.getCurrentMessageIdOnly());

        Trace.logMetricForCount(traceName);

        return TraceAopUtils.proceeAndTrace(point, metricsUtils, traceName, tagMap, transaction);
    }
}

