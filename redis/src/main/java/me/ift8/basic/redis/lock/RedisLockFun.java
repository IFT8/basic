package me.ift8.basic.redis.lock;


import me.ift8.basic.exception.ServiceException;

/**
 * Created by IFT8 on 2017/3/11.
 */
@FunctionalInterface
public interface RedisLockFun {
    void execute() throws ServiceException;
}
