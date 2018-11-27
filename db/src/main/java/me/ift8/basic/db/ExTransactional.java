package me.ift8.basic.db;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Created by zhuye on 29/06/2017.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor = Exception.class)
@Documented
public @interface ExTransactional {

}