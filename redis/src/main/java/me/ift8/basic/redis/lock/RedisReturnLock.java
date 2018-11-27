package me.ift8.basic.redis.lock;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2018/3/27.
 */
@Slf4j
public class RedisReturnLock extends AbstractRedisLock {

    private static final int ONE_SECOND = 1000;
    private static final String LOCK_KEY_PATH_PREFIX = "redisLock:";

    private static final int DEFAULT_EXPIRY_TIME_MILLIS = 60 * ONE_SECOND;
    private static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 10 * ONE_SECOND;

    private final RedissonClient redissonClient;

    private final String lockKeyPath;

    private final int lockExpiryInMillis;
    private final int acquiryTimeoutInMillis;

    public RedisReturnLock(RedissonClient redissonClient, String lockKey) {
        this(redissonClient, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    public RedisReturnLock(RedissonClient redissonClient, String lockKey, int acquireTimeoutMillis) {
        this(redissonClient, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    public RedisReturnLock(RedissonClient redissonClient, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this.redissonClient = redissonClient;
        this.lockKeyPath = LOCK_KEY_PATH_PREFIX + lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis;
    }

    /**
     * redis锁内执行
     */
    @Override
    public void doInLock(RedisLockFun fun) throws ServiceException {
        RLock lock = redissonClient.getLock(this.lockKeyPath);
        tryLock(lock);

        try {
            fun.execute();
        } finally {
            unLock(lock);
        }
    }

    /**
     * redis锁内执行
     */
    @Override
    public <T> T doInLock(RedisLockRetFun<T> fun) throws ServiceException {
        RLock lock = redissonClient.getLock(this.lockKeyPath);
        tryLock(lock);

        try {
            return fun.executeAndRet();
        } finally {
            unLock(lock);
        }
    }

    private void tryLock(RLock lock) {
        boolean locked;
        try {
            locked = lock.tryLock(this.acquiryTimeoutInMillis, this.lockExpiryInMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.error("Redis锁中断[系统异常] lockKeyPath={}", this.lockKeyPath, e);
            throw new SystemException("REDIS_LOCK_INTERRUPTED_ERROR", "Redis锁中断", e);
        }

        if (!locked) {
            log.error("Redis锁获取失败[系统异常] lockKeyPath={}", this.lockKeyPath);
            throw new SystemException("REDIS_LOCK_FAILED", "Redis锁获取失败");
        }

        if (log.isDebugEnabled()) {
            log.debug("[getLock] Redis锁获取成功 lockKeyPath={}", this.lockKeyPath);
        }
    }

    private void unLock(RLock lock) {
        lock.unlock();
        if (log.isDebugEnabled()) {
            log.debug("[releaseLock] lockKeyPath={} lockHoldCount={}", this.lockKeyPath, lock.getHoldCount());
        }
    }
}
