package me.ift8.basic.rocketmq.listener;

import me.ift8.basic.rocketmq.config.RocketMqConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IFT8 on 2017/12/19.
 */
@Slf4j
@Configuration
@ConditionalOnMissingBean(DefaultMQPushConsumer.class)
public class ListenerConfig {
    @Resource
    private RocketMqConfig mqConfig;
    @Autowired(required = false)
    private List<AbstractMessageHandler> handlers;

    @PostConstruct
    private void init() throws Exception {
        if (!CollectionUtils.isEmpty(handlers)) {
            MessageDispatcherFactory.createDefaultMQPushConsumer(mqConfig, handlers);
        }
        log.info("[] mqConfig={}", mqConfig);
    }
}
