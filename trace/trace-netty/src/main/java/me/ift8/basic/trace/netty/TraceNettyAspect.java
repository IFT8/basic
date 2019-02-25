package me.ift8.basic.trace.netty;

import me.ift8.basic.metrics.MetricsUtils;
import me.ift8.basic.trace.aop.utils.TraceAopUtils;
import me.ift8.basic.trace.core.Trace;
import me.ift8.basic.trace.core.enums.TraceTypeEnum;
import com.dianping.cat.message.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.PostConstruct;

/**
 * Created by zhuye on 09/06/2017.
 */
@Slf4j
@Aspect
public class TraceNettyAspect {
    private MetricsUtils metricsUtils;

    public TraceNettyAspect() {
    }

    public TraceNettyAspect(MetricsUtils metricsUtils) {
        this.metricsUtils = metricsUtils;
    }

    @PostConstruct
    private void init() {
        log.info("[aop] TraceNettyAspect 加载完成 hasMetricsUtils:{}",  metricsUtils != null);
    }

    @Around("execution(public * io.netty.channel.ChannelInboundHandlerAdapter.channelRead(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String className = getClass().getName();
        String traceName = className + ".channelRead" + ":netty";

        Transaction transaction = Trace.startNewTrace(TraceTypeEnum.CALL, className);
        Trace.logMetricForCount(traceName);

        return TraceAopUtils.proceeAndTrace(point, metricsUtils, traceName, null, transaction);
    }
}

