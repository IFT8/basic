package me.ift8.basic.task;

import me.ift8.basic.utils.NamedThreadPoolFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.*;

/**
 * Created by IFT8 on 2017/5/8.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TaskManager {

    private static final Integer THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static final ExecutorService EXECUTOR = NamedThreadPoolFactory.namedThreadPoolExecutor(THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(Short.MAX_VALUE),"TASK_POOL");

    public static <T> FutureTask<T> addFutureTask(Callable<T> task) {
        //创建Future任务
        FutureTask<T> futureTask = new FutureTask<>(task);
        //添加到任务队列
        EXECUTOR.execute(futureTask);
        return futureTask;
    }

    public static void addTask(Runnable task) {
        //添加到任务队列
        EXECUTOR.execute(task);
    }

    public static void shutdownAndAwait(long timeout, TimeUnit unit) throws InterruptedException {
        EXECUTOR.shutdown();
        //坐等线程执行结束
        while (!EXECUTOR.awaitTermination(timeout, unit));
    }

    public static void shutdownAndAwait() throws InterruptedException {
        shutdownAndAwait(100,TimeUnit.MINUTES);
    }
}
