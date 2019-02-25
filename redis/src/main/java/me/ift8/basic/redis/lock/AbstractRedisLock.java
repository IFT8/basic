package me.ift8.basic.redis.lock;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.fun.DoSomethingFun;
import me.ift8.basic.fun.DoSomethingRetFun;

/**
 * Created by IFT8 on 2018/4/16.
 */
public abstract class AbstractRedisLock {
    public abstract void doInLock(DoSomethingFun fun) throws ServiceException;

    public abstract <T> T doInLock(DoSomethingRetFun<T> fun) throws ServiceException;
}
