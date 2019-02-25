package me.ift8.basic.fun;


import me.ift8.basic.exception.ServiceException;

/**
 * Created by IFT8 on 2019/1/4.
 */
@FunctionalInterface
public interface DoSomethingRetFun<T> {
    T doSomethingAndRet() throws ServiceException;
}
