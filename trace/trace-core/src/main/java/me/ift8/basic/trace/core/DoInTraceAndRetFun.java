package me.ift8.basic.trace.core;


/**
 * Created by IFT8 on 2019/1/4.
 */
@FunctionalInterface
public interface DoInTraceAndRetFun<T> {
    T doInTraceAndRet() throws Exception;
}
