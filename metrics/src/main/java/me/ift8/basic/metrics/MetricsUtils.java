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

    public final void general(Class clazz, String method, String name, String type, long count, long duringMs, Map<String, String> tagMap) {
        //节省空间
        if (count == 0) {
            return;
        }

        if (tagMap == null) {
            tagMap = new HashMap<>();
        }
        String metrics = "[" + appId + "]";

        if (clazz != null) {
            metrics += "_" + "{" + clazz.getName().replaceAll("\\.", "_") + "}";
        }
        if (!StringUtils.isEmpty(method)) {
            metrics += "_" + method;
        }
        if (!StringUtils.isEmpty(name)) {
            metrics += "_" + name;
        }
        if (!StringUtils.isEmpty(type)) {
            metrics += "_" + type;
        }

        metricsClient.write(metrics, count, duringMs, tagMap);
    }

    /**
     * 执行总调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tagMap 额外的标签，比如用户ID
     */
    public final void total(Class clazz, String method, String name, long begin, Map<String, String> tagMap) {
        total(clazz, method, name, 1, System.currentTimeMillis() - begin, tagMap);
    }

    public final void total(Class clazz, String method, String name, long begin) {
        total(clazz, method, name, 1, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void total(Class clazz, String method, String name, long count, long duringMs, Map<String, String> tagMap) {
        general(clazz, method, name, "total", count, duringMs, tagMap);
    }

    public final void total(String name, long begin, Map<String, String> tagMap) {
        total(null, null, name, begin, tagMap);
    }

    public final void total(String name, long begin) {
        total(null, null, name, begin, new HashMap<>());
    }

    public final void total(String name, long count, long begin, Map<String, String> tagMap) {
        total(null, null, name, count, System.currentTimeMillis() - begin, tagMap);
    }

    public final void total(String name, long count, long begin) {
        total(null, null, name, count, System.currentTimeMillis() - begin, new HashMap<>());
    }

    /**
     * 执行成功调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tagMap 额外的标签，比如用户ID
     */
    public final void success(Class clazz, String method, String name, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "success", 1, System.currentTimeMillis() - begin, tagMap);
    }

    public final void success(Class clazz, String method, String name, long begin) {
        general(clazz, method, name, "success", 1, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void success(String name, long begin, Map<String, String> tagMap) {
        success(null, null, name, begin, tagMap);
    }

    public final void success(String name, long begin) {
        success(null, null, name, begin, new HashMap<>());
    }

    /**
     * 执行出现系统异常调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tagMap 额外的标签，比如用户ID
     */
    public final void systemFail(Class clazz, String method, String name, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "systemFail", 1, System.currentTimeMillis() - begin, tagMap);
    }

    public final void systemFail(Class clazz, String method, String name, long begin) {
        general(clazz, method, name, "systemFail", 1, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void systemFail(String name, long begin, Map<String, String> tagMap) {
        systemFail(null, null, name, begin, tagMap);
    }

    public final void systemFail(String name, long begin) {
        systemFail(null, null, name, begin, new HashMap<>());
    }

    /**
     * 执行出现业务异常调用
     *
     * @param clazz  调用的类，如果希望measurement名字短一点或者为了统一可以不传
     * @param method 调用的方法名
     * @param name   方法中的逻辑名或处理的不同数据名
     * @param begin  方法执行开始时间
     * @param tagMap 额外的标签，比如用户ID
     */
    public final void serviceFail(Class clazz, String method, String name, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "serviceFail", 1, System.currentTimeMillis() - begin, tagMap);
    }

    public final void serviceFail(Class clazz, String method, String name, long begin) {
        general(clazz, method, name, "serviceFail", 1, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void serviceFail(String name, long begin, Map<String, String> tagMap) {
        serviceFail(null, null, name, begin, tagMap);
    }

    public final void serviceFail(String name, long begin) {
        serviceFail(null, null, name, begin, new HashMap<>());
    }


    public final void successMultipleTimes(Class clazz, String method, String name, long count, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "success", count, System.currentTimeMillis() - begin, tagMap);
    }

    public final void successMultipleTimes(Class clazz, String method, String name, long count, long begin) {
        general(clazz, method, name, "success", count, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void successMultipleTimes(String name, long count, long begin, Map<String, String> tagMap) {
        successMultipleTimes(null, null, name, count, begin, tagMap);
    }

    public final void successMultipleTimes(String name, long count, long begin) {
        successMultipleTimes(null, null, name, count, begin, new HashMap<>());
    }

    public final void systemFailMultipleTimes(Class clazz, String method, String name, long count, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "systemFail", count, System.currentTimeMillis() - begin, tagMap);
    }

    public final void systemFailMultipleTimes(Class clazz, String method, String name, long count, long begin) {
        general(clazz, method, name, "systemFail", count, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void systemFailMultipleTimes(String name, long count, long begin, Map<String, String> tagMap) {
        systemFailMultipleTimes(null, null, name, count, begin, tagMap);
    }

    public final void systemFailMultipleTimes(String name, long count, long begin) {
        systemFailMultipleTimes(null, null, name, count, begin, new HashMap<>());
    }


    public final void serviceFailMultipleTimes(Class clazz, String method, String name, long count, long begin, Map<String, String> tagMap) {
        general(clazz, method, name, "serviceFail", count, System.currentTimeMillis() - begin, tagMap);
    }

    public final void serviceFailMultipleTimes(Class clazz, String method, String name, long count, long begin) {
        general(clazz, method, name, "serviceFail", count, System.currentTimeMillis() - begin, new HashMap<>());
    }

    public final void serviceFailMultipleTimes(String name, long count, long begin, Map<String, String> tagMap) {
        serviceFailMultipleTimes(null, null, name, count, begin, tagMap);
    }

    public final void serviceFailMultipleTimes(String name, long count, long begin) {
        serviceFailMultipleTimes(null, null, name, count, begin, new HashMap<>());
    }
}
