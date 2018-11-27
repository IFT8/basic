package me.ift8.basic.job.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chenfeng on 2016/11/24.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobSchedule {

    /**
     * 作业名称
     * @return
     */
    String name();

    /**
     * cron表达式
     * @return
     */
    String cron();

    /**
     * 作业描述信息
     * @return
     */
    String description() default "";

    /**
     * 作业默认是否禁用
     * @return
     */
    boolean disabled() default false;
}
