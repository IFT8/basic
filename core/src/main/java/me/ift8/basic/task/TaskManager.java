package me.ift8.basic.task;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2017/5/8.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskManager {

    private static final Integer THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static final ExecutorService executor = new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(Short.MAX_VALUE));

    public static <T> FutureTask<T> addFutureTask(Callable<T> task) {
        //创建Future任务
        FutureTask<T> futureTask = new FutureTask<>(task);
        //添加到任务队列
        executor.execute(futureTask);
        return futureTask;
    }

    public static void addTask(Runnable task) {
        //添加到任务队列
        executor.execute(task);
    }

    public static void shutdownAndAwait(long timeout, TimeUnit unit) throws InterruptedException {
        executor.shutdown();
        //坐等线程执行结束
        while (!executor.awaitTermination(timeout, unit));
    }

    public static void shutdownAndAwait() throws InterruptedException {
        shutdownAndAwait(100,TimeUnit.MINUTES);
    }
}
