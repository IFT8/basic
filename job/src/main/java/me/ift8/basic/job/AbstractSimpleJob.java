package me.ift8.basic.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.job.annotation.JobSchedule;
import me.ift8.basic.metrics.MetricsUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by chenfeng on 2016/12/16.
 */
@Slf4j
public abstract class AbstractSimpleJob implements SimpleJob {
    private boolean useMetricsUtils = false;

    @Resource
    private MetricsUtils metricsUtils;

    @PostConstruct
    public void init() {
        this.useMetricsUtils = metricsUtils != null;
    }

    @Override
    public void execute(ShardingContext shardingContext) {
        JobSchedule jobSchedule = getClass().getAnnotation(JobSchedule.class);
        String jobName = jobSchedule.name() + "(" + jobSchedule.description() + ")";
        long begin = System.currentTimeMillis();

        workAndMetric(jobName, begin);
    }

    private void workAndMetric(String jobName, Long begin) {
        try {
            log.info("开始执行 jobName=[{}]", jobName);
            doWork();
            log.info("结束执行 jobName=[{}]", jobName);

            if (this.useMetricsUtils) {
                metricsUtils.success(this.getClass(), "execute", jobName, begin);
            }

        } catch (ServiceException se) {
            log.warn("执行 jobName=[{}] [业务异常]：{}", jobName, se.getErrorMessage());

            if (this.useMetricsUtils) {
                metricsUtils.serviceFail(this.getClass(), "execute", jobName, begin);
            }

        } catch (Throwable e) {
            log.error("执行 jobName=[{}] [系统异常]：", jobName, e);

            if (this.useMetricsUtils) {
                metricsUtils.systemFail(this.getClass(), "execute", jobName, begin);
            }
        }
    }

    public abstract void doWork() throws Exception;
}
