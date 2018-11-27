package me.ift8.basic.job.config;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.utils.PreconditionsUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * Created by chenfeng cast lusu on 2016/11/25.
 */
@Data
@Slf4j
@ConfigurationProperties(prefix = "jobschedule")
public class JobScheduleProperties {
    /**
     * 延时启动时间（单位秒）
     */
    private int startupDelay = 30;
    private RegistryCenterProperties registryCenter;

    @Data
    public static class RegistryCenterProperties {
        private String serverLists;
        private String namespace;
    }

    @PostConstruct
    private void init() throws ServiceException {
        PreconditionsUtils.notBlank(registryCenter.getServerLists());
        PreconditionsUtils.notBlank(registryCenter.getNamespace());
        log.info("Schedule配置信息：{}", this);
    }
}


