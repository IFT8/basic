package me.ift8.basic.redis.lock;


import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

@Slf4j
public class RedisLock extends AbstractRedisLock {
    private static final Lock NO_LOCK = new Lock(new UUID(0l, 0l), 0l);

    private static final int ONE_SECOND = 1000;
    private static final String LOCK_KEY_PATH_PREFIX = "redisLock:";

    public static final int DEFAULT_EXPIRY_TIME_MILLIS = 60 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 10 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 100;

    private final StringRedisTemplate redisClient;

    private final String lockKeyPath;

    private final int lockExpiryInMillis;
    private final int acquiryTimeoutInMillis;
    private final UUID lockUUID;

    private Lock lock = null;

    protected static class Lock {
        private UUID uuid;
        private long expiryTime;

        protected Lock(UUID uuid, long expiryTimeInMillis) {
            this.uuid = uuid;
            this.expiryTime = expiryTimeInMillis;
        }

        protected static Lock fromString(String text) {
            try {
                String[] parts = text.split(":");
                UUID theUUID = UUID.fromString(parts[0]);
                long theTime = Long.parseLong(parts[1]);
                return new Lock(theUUID, theTime);
            } catch (Exception any) {
                log.error("Lock fromString [系统异常]", any);
                return NO_LOCK;
            }
        }

        public UUID getUUID() {
            return uuid;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        @Override
        public String toString() {
            return uuid.toString() + ":" + expiryTime;
        }

        boolean isExpired() {
            return getExpiryTime() < System.currentTimeMillis();
        }

        boolean isExpiredOrMine(UUID otherUUID) {
            return this.isExpired() || this.getUUID().equals(otherUUID);
        }
    }


    /**
     * Detailed constructor with default acquire timeout 10000 msecs and lock
     * expiration of 60000 msecs.
     *
     * @param lockKey lock key (ex. account:1, ...)
     */
    public RedisLock(StringRedisTemplate redisClient, String lockKey) {
        this(redisClient, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor with default lock expiration of 60000 msecs.
     *
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     */
    public RedisLock(StringRedisTemplate redisClient, String lockKey, int acquireTimeoutMillis) {
        this(redisClient, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    /**
     * Detailed constructor.
     *
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis     lock expiration in miliseconds (default: 60000 msecs)
     */
    public RedisLock(StringRedisTemplate redisClient, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this(redisClient, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
    }

    /**
     * Detailed constructor.
     *
     * @param lockKey              lock key (ex. account:1, ...)
     * @param acquireTimeoutMillis acquire timeout in miliseconds (default: 10000 msecs)
     * @param expiryTimeMillis     lock expiration in miliseconds (default: 60000 msecs)
     * @param uuid                 unique identification of this lock
     */
    public RedisLock(StringRedisTemplate redisClient, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis, UUID uuid) {
        this.redisClient = redisClient;
        this.lockKeyPath = LOCK_KEY_PATH_PREFIX + lockKey;
        this.acquiryTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis + 1;
        this.lockUUID = uuid;
    }

    /**
     * @return lock uuid
     */
    public UUID getLockUUID() {
        return lockUUID;
    }

    /**
     * @return lock key path
     */
    public String getLockKeyPath() {
        return lockKeyPath;
    }

    /**
     * Acquire lock.
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    public synchronized boolean acquire() throws InterruptedException {
        return acquire(redisClient);
    }

    /**
     * Acquire lock.
     *
     * @return true if lock is acquired, false acquire timeouted
     * @throws InterruptedException in case of thread interruption
     */
    protected synchronized boolean acquire(StringRedisTemplate redisClient) throws InterruptedException {

        int timeout = acquiryTimeoutInMillis;
        while (timeout >= 0) {
            final Lock newLock = asLock(System.currentTimeMillis() + lockExpiryInMillis);
            if (redisClient.opsForValue().setIfAbsent(lockKeyPath, newLock.toString())) {
                if (log.isDebugEnabled()) {
                    log.debug("[getLock] setIfAbsent=true lockKeyPath={} newLock={}", lockKeyPath, newLock);
                }
                this.lock = newLock;
                return true;
            }

            final String currentValueStr = redisClient.opsForValue().get(lockKeyPath);
            if (currentValueStr == null || Lock.fromString(currentValueStr).isExpiredOrMine(lockUUID)) {
                String oldValueStr = redisClient.opsForValue().getAndSet(lockKeyPath, newLock.toString());
                if (log.isDebugEnabled()) {
                    log.debug("[isExpiredOrMineLock] isExpiredOrMine lockKeyPath={} oldValueStr={} newLock={} ", lockKeyPath, oldValueStr, newLock.toString());
                }
                if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
                    if (log.isDebugEnabled()) {
                        log.debug("[getLock] isExpiredOrMine lockKeyPath={} oldValueStr={} newLock={} this={}", lockKeyPath, oldValueStr, newLock.toString(), this);
                    }
                    this.lock = newLock;
                    return true;
                }
            }

            timeout -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
            Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);

            if (log.isDebugEnabled()) {
                log.debug("[Wait] lockKeyPath={} , newLock={}", lockKeyPath, newLock);
            }
        }

        return false;
    }

    /**
     * Renew lock.
     *
     * @return true if lock is acquired, false otherwise
     * @throws InterruptedException in case of thread interruption
     */
    public boolean renew() throws InterruptedException {
        final Lock oldLock = Lock.fromString(redisClient.opsForValue().get(lockKeyPath));
        return oldLock.isExpiredOrMine(lockUUID) && acquire(redisClient);

    }

    /**
     * Acquired lock release.
     */
    public synchronized void release() {
        release(redisClient);
    }

    /**
     * Acquired lock release.
     */
    protected synchronized void release(StringRedisTemplate redisClient) {
        if (isLocked()) {
            if (log.isDebugEnabled()) {
                log.debug("[releaseLock] lockKeyPath={} lock={}", lockKeyPath, lock == null ? null : lock.toString());
            }
            final String currentValueStr = redisClient.opsForValue().get(lockKeyPath);
            if (lock != null && currentValueStr != null && currentValueStr.equals(lock.toString())) {
                redisClient.delete(lockKeyPath);
            }
            this.lock = null;
        }
    }

    /**
     * Check if owns the lock
     *
     * @return true if lock owned
     */
    public synchronized boolean isLocked() {
        return this.lock != null;
    }

    /**
     * Returns the expiry time of this lock
     *
     * @return the expiry time in millis (or null if not locked)
     */
    public synchronized long getLockExpiryTimeInMillis() {
        return this.lock.getExpiryTime();
    }


    private Lock asLock(long expires) {
        return new Lock(lockUUID, expires);
    }


    /**
     * redis锁内执行
     */
    @Override
    public synchronized void doInLock(RedisLockFun fun) throws ServiceException {
        acquireAndCheck();

        try {
            fun.execute();
        } finally {
            this.release();
        }
    }

    @Override
    public synchronized <T> T doInLock(RedisLockRetFun<T> fun) throws ServiceException {
        acquireAndCheck();

        try {
           return fun.executeAndRet();
        } finally {
            this.release();
        }
    }

    private void acquireAndCheck(){
        try {
            if (!this.acquire()) {
                log.error("Redis锁获取失败[系统异常] lockKeyPath={}", lockKeyPath);
                throw new SystemException("REDIS_LOCK_FAILED", "Redis锁获取失败");
            }
        } catch (InterruptedException e) {
            log.error("Redis锁中断[系统异常] lockKeyPath={}", lockKeyPath, e);
            throw new SystemException("REDIS_LOCK_INTERRUPTED_ERROR", "Redis锁中断", e);
        }

        if (log.isDebugEnabled()) {
            log.debug("[getLock] Redis锁获取成功 lock={}", lock.toString());
        }
    }
}