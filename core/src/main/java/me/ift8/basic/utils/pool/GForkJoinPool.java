package me.ift8.basic.utils.pool;

import me.ift8.basic.trace.core.Trace;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * Created by IFT8 on 2019-02-19.
 */
@Slf4j
public class GForkJoinPool extends ForkJoinPool {

    public GForkJoinPool() {
    }

    public GForkJoinPool(int parallelism) {
        super(parallelism);
    }

    public GForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
        super(parallelism, factory, handler, asyncMode);
    }

    @Override
    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        return super.submit(task);
    }

    @Override
    public <T> ForkJoinTask<T> submit(Callable<T> task) {
        Callable<T> wrap = Trace.wrapTraceCallable(task);
        return super.submit(wrap);
    }

    @Override
    public <T> ForkJoinTask<T> submit(Runnable task, T result) {
        Runnable wrap = Trace.wrapTraceRunnable(task);
        return super.submit(wrap, result);
    }

    @Override
    public ForkJoinTask<?> submit(Runnable task) {
        Runnable wrap = Trace.wrapTraceRunnable(task);
        return super.submit(wrap);
    }
}
