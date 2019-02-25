package me.ift8.basic.metrics;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.trace.core.Trace;
import me.ift8.basic.utils.DefaultValue;
import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by zhuye on 09/06/2017.
 */
@Slf4j
@Aspect
public class MetricsAspect {
    private MetricsUtils metricsUtils = null;

    public MetricsAspect() {
    }

    public MetricsAspect(MetricsUtils metricsUtils) {
        this.metricsUtils = metricsUtils;
    }

    /**
     * 客户端是否存在
     */
    private boolean hasMetricsClient = false;

    /**
     * 强制打点 (强制 出入参)
     */
    private static final Metrics FORCE_METRICS = new Metrics() {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }

        @Override
        public boolean recordTime() {
            return true;
        }

        @Override
        public boolean recordSuccessCount() {
            return true;
        }

        @Override
        public String name() {
            return "";
        }

        @Override
        public boolean logOutput() {
            return true;
        }

        @Override
        public boolean logInput() {
            return true;
        }

        @Override
        public String desc() {
            return "";
        }

        @Override
        public boolean ignoreBizError() {
            return false;
        }

        @Override
        public boolean ignoreSysError() {
            return false;
        }

        @Override
        public boolean ignoreError() {
            return false;
        }

    };

    /**
     * obj[]转jsonstr
     */
    private String toJson(Object[] o) {
        if (o == null) {
            return null;
        }
        if (o.length == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (Object o1 : o) {
            sb.append(toJson(o1)).append(",");
        }
        return sb.substring(0, sb.length() - 1) + "]";
    }

    /**
     * obj转jsonstr
     */
    private String toJson(Object o) {
        if (o == null) {
            return "null";
        }
        try {
            return JsonUtils.toJsonWithinException(o);
        } catch (Exception e) {
            return "[" + o.getClass().toString() + "]";
        }
    }


    @PostConstruct
    private void init() {
        hasMetricsClient = metricsUtils != null;
        log.info("[aop] MetricsAspect 加载完成: class=[{}] hasMetricsClient=[{}] controller && api 强制打出入参 (忽略请加Metrics注解)", this.getClass().getSimpleName(), this.hasMetricsClient);
    }

    //api实现会强制加Metric，其他只要打Metric标签才会打 (ajc Bug 对于注解 需要外加限制' execution(* *(..)) ')
    @Around("execution(* *(..)) && @annotation(me.ift8.basic.metrics.Metrics) || execution(public * me.ift8..*.controller..*.*(..)) || execution(public * me.ift8..*.api..*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return doAaround(point);
    }

    protected Object doAaround(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature ms = (MethodSignature) point.getSignature();
        Metrics metrics;
        //api 使用impl的注解
        if (point.getSignature().getDeclaringType().isInterface()) {
            Method method = point.getTarget().getClass().getMethod(ms.getMethod().getName(), ms.getParameterTypes());
            metrics = method.getDeclaredAnnotation(Metrics.class);
        } else {
            metrics = ms.getMethod().getDeclaredAnnotation(Metrics.class);
        }
        if (metrics == null) {
            metrics = FORCE_METRICS;
        }

        //metricsName
        String metricName = StringUtils.isEmpty(metrics.name()) ? ms.getDeclaringTypeName() + "-" + ms.getName() : metrics.name();
        //入参json
        String inputJson = null;

        //记录入参metric
        if (metrics.logInput()) {
            inputJson = toJson(point.getArgs());
            log.info("{}调用【{}】- 参数为【{}】", metrics.desc(), metricName, inputJson);
        }

        //开始执行业务逻辑
        try {
            Object result = point.proceed();

            //记录返回值metric
            if (metrics.logOutput()) {
                log.info("{}调用【{}】成功 - 返回值为【{}】", metrics.desc(), metricName, toJson(result));
            }

            //记录方法执行成功次数
            if (metrics.recordSuccessCount() && hasMetricsClient) {
                metricsUtils.success(metricName, start);
            }

            return result;
        } catch (Exception e) {
            if (StringUtils.isEmpty(inputJson) && !(e instanceof ServiceException)) {
                inputJson = toJson(point.getArgs());
            }

            boolean ignoreError = ignoreError(e, metrics);

            //不抛出业务异常返回的默认值
            Object defaultResult = DefaultValue.getDefaultValue(ms.getReturnType());
            logError(e, metrics.desc(), metricName, ignoreError, inputJson, defaultResult);

            //失败Metrics点
            recordErrorMetrics(e, metricName, start);

            //异常处理
            if (ignoreError) {
                //返回默认值
                return defaultResult;
            } else {
                //正常抛出
                throw e;
            }
        } finally {
            //所有Metrics点
            if (metrics.recordTime() && hasMetricsClient) {
                metricsUtils.total(metricName, start);
            }
        }
    }

    private void recordErrorMetrics(Exception e, String metricsName, long start) {
        if (hasMetricsClient) {
            //业务异常
            if (e instanceof ServiceException) {
                metricsUtils.serviceFail(metricsName, start);
            } else {
                //系统异常
                metricsUtils.systemFail(metricsName, start);
            }
            Trace.logError(metricsName, e);
        }
    }

    /**
     * 是否忽略异常
     */
    private boolean ignoreError(Exception e, Metrics metrics) {
        boolean ignore = false;
        //业务异常
        if (e instanceof ServiceException) {
            //是否忽略
            if (metrics.ignoreBizError()) {
                ignore = true;
            }
        }
        //系统异常 是否忽略
        else if (metrics.ignoreSysError()) {
            ignore = true;
        }

        return metrics.ignoreError() || ignore;
    }

    /**
     * 记录日志
     */
    void logError(Exception e, String metricsDesc, String metricsName, boolean ignoreException, String inputJson, Object defaultResult) {
        boolean isBizException = e instanceof ServiceException;

        StringBuilder sb = new StringBuilder();

        sb.append("{}调用【{}】-");
        sb.append(isBizException ? "【业务异常】" : "【系统异常】");
        sb.append("-【参数={}】");

        //业务异常打印code 信息
        if (isBizException) {
            ServiceException se = (ServiceException) e;
            sb.append("- code=").append(se.getErrorCode()).append(", msg=").append(se.getErrorMessage());
        }

        //忽略异常打印内容
        if (ignoreException) {
            sb.append("-【忽略异常】");
            sb.append("-【返回默认值defaultResult=").append(defaultResult).append("】");
        }

        if (isBizException || ignoreException) {
            log.warn(sb.toString(), metricsDesc, metricsName, inputJson);
        } else {
            //系统异常打堆栈
            log.error(sb.toString(), metricsDesc, metricsName, inputJson, e);
        }
    }
}

