package me.ift8.basic.redis.lock;

import me.ift8.basic.exception.ServiceException;

/**
 * Created by IFT8 on 2018/4/16.
 */
public abstract class AbstractRedisLock {
    public abstract void doInLock(RedisLockFun fun) throws ServiceException;

    public abstract <T> T doInLock(RedisLockRetFun<T> fun) throws ServiceException;
}
