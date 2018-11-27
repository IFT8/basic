package me.ift8.basic.metrics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhuye on 09/06/2017.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {

    /**
     * metric名称，默认：包名.类名$方法名
     * 业务异常:metric名称_bus_fail_count
     * 系统异常:metric名称_sys_fail_count
     * 成功数量:metric名称_success_count
     */
    String name() default "";

    /**
     * 方法描述，默认：包名.类名$方法名
     */
    String desc() default "";

    /**
     * 是否记录方法执行时间metric，默认记录
     */
    boolean recordTime() default true;

    /**
     * 是否记录方法执行成功数量metric，默认记录
     */
    boolean recordSuccessCount() default true;

    /**
     * 是否log打印方法入参(json格式，业务/系统异常强制打印)，默认不打印
     */
    boolean logInput() default false;

    /**
     * 是否log打印方法返回值(json格式，业务/系统异常强制打印)，默认不打印
     */
    boolean logOutput() default false;

    /**
     * 是否忽略业务异常，如果为true则业务异常将不抛出，默认抛出
     */
    boolean ignoreBizError() default false;

    /**
     * 是否忽略系统异常，如果为true则系统异常将不抛出，默认抛出
     */
    boolean ignoreSysError() default false;

    /**
     * 是否忽略所有异常，如果为true则任何异常都不抛出，默认抛出
     */
    boolean ignoreError() default false;
}
