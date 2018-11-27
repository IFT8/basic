package me.ift8.basic.job.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import me.ift8.basic.job.annotation.JobSchedule;
import me.ift8.basic.job.handler.DefaultExecutorServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenfeng on 2016/11/14.
 */
@Configuration
@EnableConfigurationProperties({JobScheduleProperties.class})
public class JobScheduleConfig {

    private static final Logger logger = LoggerFactory.getLogger(JobScheduleConfig.class);

    @Autowired(required = false)
    List<SimpleJob> simpleJobs;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private JobScheduleProperties jobScheduleProperties;

    @Bean
    public CoordinatorRegistryCenter setUpRegistryCenter() {
        ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(jobScheduleProperties.getRegistryCenter().getServerLists(), jobScheduleProperties.getRegistryCenter().getNamespace());
        CoordinatorRegistryCenter regCenter = new ZookeeperRegistryCenter(zkConfig);
        return regCenter;
    }


    private JobScheduler buildSimpleJob(String jobName, String cron, String description, boolean disabled, SimpleJob simpleJob) {
        // 定义作业核心配置
        JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, 1)
                .description(description)
                .failover(true)
                .misfire(true)
                .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), DefaultExecutorServiceHandler.class.getCanonicalName())
                .build();
        // 定义SIMPLE类型配置
        SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, simpleJob.getClass().getCanonicalName());
        // 定义Lite作业根配置
        LiteJobConfiguration simpleJobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig)
                .disabled(disabled)
                .overwrite(true)
                .monitorExecution(true).build();
        JobScheduler jobScheduler = new SpringJobScheduler(simpleJob, setUpRegistryCenter(), simpleJobRootConfig, new ElasticJobListener() {

            @Override
            public void beforeJobExecuted(ShardingContexts shardingContexts) {

            }

            @Override
            public void afterJobExecuted(ShardingContexts shardingContexts) {

            }
        });

        return jobScheduler;
    }

    /**
     * 定时器里面的cron表达式写不对怎么办？
     * 在线验证工具：
     * http://cron.qqe2.com/
     * 在线生成工具：
     * http://www.jeasyuicn.com/cron/
     */
    @PostConstruct
    private void init() {
        if (simpleJobs == null || simpleJobs.size() == 0) {
            logger.warn("在Spring容器中没有检测到Job对象，请确保Job对象实现SimpleJob接口，并且应用了@JobSchedule注解");
            return;
        }
        CoordinatorRegistryCenter registryCenter = setUpRegistryCenter();

        registryCenter.init();

        List<JobScheduler> jobSchedulers = scanAndBuildSimpleJobSchedulers();

        registerToSpringBeanFactory(jobSchedulers);
        int startupDelay = jobScheduleProperties.getStartupDelay();
        if (startupDelay > 0) {
            startAllDelayed(jobSchedulers, startupDelay);
        } else {
            startAll(jobSchedulers);
        }
    }


    private List<JobScheduler> scanAndBuildSimpleJobSchedulers() {
        List<JobScheduler> jobSchedulers = new ArrayList<>();
        simpleJobs.stream().filter(job -> {
            boolean implementsSimpleJob = SimpleJob.class.isAssignableFrom(job.getClass());
            boolean hasJobDefine = job.getClass().getAnnotation(JobSchedule.class) != null;
            return implementsSimpleJob && hasJobDefine;
        }).forEach(job -> {
            JobSchedule def = job.getClass().getAnnotation(JobSchedule.class);
            final String name = def.name();
            final String cron = def.cron();
            final String description = def.description();
            final boolean disabled = def.disabled();
            jobSchedulers.add(buildSimpleJob(name, cron, description, disabled, job));
        });
        return jobSchedulers;
    }

    private void registerToSpringBeanFactory(List<JobScheduler> jobSchedulers) {
        for (int i = 0; i < jobSchedulers.size(); i++) {
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            String beanName = JobScheduler.class.getCanonicalName() + "$" + i;
            beanFactory.registerSingleton(beanName, jobSchedulers.get(i));
        }
    }

    private void startAll(List<JobScheduler> jobSchedulers) {
        for (JobScheduler jobScheduler : jobSchedulers) {
            jobScheduler.init();
        }
    }

    private void startAllDelayed(List<JobScheduler> jobSchedulers, final int seconds) {
        logger.info("Will start Job Scheduler in {} seconds", seconds);
        for (JobScheduler jobScheduler : jobSchedulers) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep((long) seconds * 1000L);
                } catch (InterruptedException ex) {
                    ;
                }
                jobScheduler.init();
            });
            t.start();
        }
    }

    @PreDestroy
    public void shutdownDestroy() {
        //销毁调度器
        String[] jobSchedulerNames = applicationContext.getBeanNamesForType(JobScheduler.class);
        if (jobSchedulerNames == null || jobSchedulerNames.length == 0) {
            return;
        }
        for (String jobSchedulerName : jobSchedulerNames) {
            JobScheduler jobScheduler = (JobScheduler) applicationContext.getBean(jobSchedulerName);
            jobScheduler.shutdown();
        }
    }

}
