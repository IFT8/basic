package me.ift8.basic.job.handler;

import com.dangdang.ddframe.job.executor.handler.ExecutorServiceHandler;
import com.dangdang.ddframe.job.util.concurrent.ExecutorServiceObject;

import java.util.concurrent.ExecutorService;

/**
 * Created by chenfeng on 2016/12/3.
 * 默认线程池服务处理器.
 */
public class DefaultExecutorServiceHandler  implements ExecutorServiceHandler {
    @Override
    public ExecutorService createExecutorService(final String jobName) {
        return new ExecutorServiceObject("inner-job-" + jobName, Runtime.getRuntime().availableProcessors() * 2).createExecutorService();
    }
}
