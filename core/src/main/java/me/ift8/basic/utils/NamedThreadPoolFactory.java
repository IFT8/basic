package me.ift8.basic.utils;

import me.ift8.basic.utils.pool.GForkJoinPool;
import me.ift8.basic.utils.pool.GThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by IFT8 on 2018-12-13.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NamedThreadPoolFactory {

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String name, RejectedExecutionHandler rejectedExecutionHandler) {
        return new GThreadPoolExecutor(poolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, createNamedThreadFactory(name), rejectedExecutionHandler);
    }

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, String name) {
        return new GThreadPoolExecutor(poolSize, poolSize, keepAliveTime, unit,
                workQueue, createNamedThreadFactory(name));
    }

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, BlockingQueue<Runnable> workQueue, String name, RejectedExecutionHandler rejectedExecutionHandler) {
        return new GThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                workQueue, createNamedThreadFactory(name), rejectedExecutionHandler);
    }

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, BlockingQueue<Runnable> workQueue, String name) {
        return namedThreadPoolExecutor(poolSize, workQueue, name, new ThreadPoolExecutor.AbortPolicy());
    }

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, int queueSize, String name) {
        return namedThreadPoolExecutor(poolSize, queueSize, name, new ThreadPoolExecutor.AbortPolicy());
    }

    public static GThreadPoolExecutor namedThreadPoolExecutor(int poolSize, int queueSize, String name, RejectedExecutionHandler rejectedExecutionHandler) {
        return namedThreadPoolExecutor(poolSize, new LinkedBlockingQueue<>(queueSize), name, rejectedExecutionHandler);
    }

    public static GThreadPoolExecutor simpleNamedThreadPoolExecutor(int poolSize, String name) {
        return namedThreadPoolExecutor(poolSize, Short.MAX_VALUE, name, new ThreadPoolExecutor.AbortPolicy());
    }

    public static GForkJoinPool namedForkJoinPool(int parallelism, String name) {
        return new GForkJoinPool(parallelism, pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName(name + "-worker-" + worker.getPoolIndex());
            return worker;
        }, null, false);
    }

    private static ThreadFactory createNamedThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name + "-thread-%d").build();
    }
}
