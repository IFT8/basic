package me.ift8.basic.utils.pool;

import me.ift8.basic.trace.core.Trace;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Created by IFT8 on 2019-02-19.
 */
@Slf4j
public class GThreadPoolExecutor extends ThreadPoolExecutor {
    public GThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public GThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public GThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public GThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }


    @Override
    public Future<?> submit(Runnable task) {
        Runnable wrap = Trace.wrapTraceRunnable(task);
        return super.submit(wrap);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        Runnable wrap = Trace.wrapTraceRunnable(task);
        return super.submit(wrap, result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        Callable<T> wrap = Trace.wrapTraceCallable(task);
        return super.submit(wrap);
    }
}
