package me.ift8.basic.fun;


import me.ift8.basic.exception.ServiceException;

/**
 * Created by IFT8 on 2019/1/4.
 */
@FunctionalInterface
public interface DoSomethingFun {
    void doSomething() throws ServiceException;
}
