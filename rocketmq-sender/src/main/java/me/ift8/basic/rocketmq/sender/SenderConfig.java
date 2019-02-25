package me.ift8.basic.rocketmq.sender;

import me.ift8.basic.rocketmq.config.RocketMqConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class SenderConfig {
    @Resource
    private RocketMqConfig mqConfig;
    @Resource
    private DefaultMQProducer mqProducer;

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer rocketmqDemo = new DefaultMQProducer(mqConfig.getProducerGroupName());
        rocketmqDemo.setNamesrvAddr(mqConfig.getNameServers());
        rocketmqDemo.start();
        return rocketmqDemo;
    }

    @Bean
    public MessageSender messageSender() {
        return new MessageSender(mqProducer, mqConfig.isRecordMsgLog(), mqConfig.isRecordFinishLog());
    }
}