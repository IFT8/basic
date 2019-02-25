package me.ift8.basic.utils.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;

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
}
