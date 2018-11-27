package me.ift8.basic.model;

import org.springframework.beans.BeanUtils;

/**
 * Created by IFT8 on 2017/5/12.
 */
public interface Parsable<F> {
    /**
     * F 转换成 this
     */
    default void parse(F source) {
        BeanUtils.copyProperties(source, this);
    }
}
