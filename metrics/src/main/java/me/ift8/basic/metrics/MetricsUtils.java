package me.ift8.basic.metrics;

import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuye on 09/06/2017.
 */
@AllArgsConstructor
public class MetricsUtils {
    private final String appId;
    private MetricsClient metricsClient;

    public boolean needInit() {
        return metricsClient.needInit();
    }

    @SafeVarargs
    public final void general(Class clazz, String method, String name, String type, long count, long time, Map.Entry<String, String>... tags) {
        Map<String, String> tagMap = new HashMap<>();

        String metrics = "[" + appId + "]";

        tagMap.put("appId", appId);

        if (clazz != null) {
            tagMap.put("class", clazz.getName());
            metrics += "_" + "{" + clazz.getName().replaceAll("\\.", "_") + "}";
        }
        if (!StringUtils.isEmpty(method)) {
            tagMap.put("method", method);
            metrics += "_" + method;
        }
        if (!StringUtils.isEmpty(name)) {
            tagMap.put("name", name);
            metrics += "_" + name;
        }
        if (!StringUtils.isEmpty(type)) {
            tagMap.put("type", type);
            metrics += "_" + type;
        }
        for (Map.Entry<String, String> tag : tags) {
            tagMap.put(tag.getKey(), tag.getValue());
        }

        metricsClient.write(metrics, count, time, tagMap);
    }

    /**
     * 执行总调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tags   额外的标签，比如用户ID
     */
    @SafeVarargs
    public final void total(Class clazz, String method, String name, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "total", 1, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void total(String name, long begin, Map.Entry<String, String>... tags) {
        total(null, null, name, begin, tags);
    }

    /**
     * 执行成功调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tags   额外的标签，比如用户ID
     */
    @SafeVarargs
    public final void success(Class clazz, String method, String name, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "success", 1, System.currentTimeMillis() - begin, tags);
    }


    @SafeVarargs
    public final void success(String name, long begin, Map.Entry<String, String>... tags) {
        success(null, null, name, begin, tags);
    }

    /**
     * 执行出现系统异常调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tags   额外的标签，比如用户ID
     */
    @SafeVarargs
    public final void systemFail(Class clazz, String method, String name, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "systemFail", 1, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void systemFail(String name, long begin, Map.Entry<String, String>... tags) {
        systemFail(null, null, name, begin, tags);
    }

    /**
     * 执行出现业务异常调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tags   额外的标签，比如用户ID
     */
    @SafeVarargs
    public final void serviceFail(Class clazz, String method, String name, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "serviceFail", 1, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void serviceFail(String name, long begin, Map.Entry<String, String>... tags) {
        serviceFail(null, null, name, begin, tags);
    }

    @SafeVarargs
    public final void successMultipleTimes(Class clazz, String method, String name, long count, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "success", count, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void successMultipleTimes(String name, long count, long begin, Map.Entry<String, String>... tags) {
        successMultipleTimes(null, null, name, count, begin, tags);
    }

    @SafeVarargs
    public final void systemFailMultipleTimes(Class clazz, String method, String name, long count, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "systemFail", count, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void systemFailMultipleTimes(String name, long count, long begin, Map.Entry<String, String>... tags) {
        systemFailMultipleTimes(null, null, name, count, begin, tags);
    }

    @SafeVarargs
    public final void serviceFailMultipleTimes(Class clazz, String method, String name, long count, long begin, Map.Entry<String, String>... tags) {
        general(clazz, method, name, "serviceFail", count, System.currentTimeMillis() - begin, tags);
    }

    @SafeVarargs
    public final void serviceFailMultipleTimes(String name, long count, long begin, Map.Entry<String, String>... tags) {
        serviceFailMultipleTimes(null, null, name, count, begin, tags);
    }
}
