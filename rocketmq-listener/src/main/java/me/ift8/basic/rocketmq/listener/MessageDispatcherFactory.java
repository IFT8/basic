package me.ift8.basic.rocketmq.listener;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.rocketmq.config.RocketMqConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by IFT8 on 2017/12/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDispatcherFactory {

    public static List<DefaultMQPushConsumer> createDefaultMQPushConsumer(RocketMqConfig defaultMqConfig, List<AbstractMessageHandler> handlers) throws Exception {
        if (handlers == null || handlers.isEmpty()) {
            throw ErrorMessage.SYS_ERROR.getSystemException("没有任何消息处理程序！");
        }

        boolean moreThan = handlers.stream().map(AbstractMessageHandler::getHandleTag)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream().anyMatch(p -> p.getValue() > 1);
        if (moreThan) {
            throw ErrorMessage.SYS_ERROR.getSystemException("消息处理程序不能处理相同的Tag！");
        }

        //统计Group内Consumer数量
        int groupConsumerSum = handlers.stream().filter(fItem -> Objects.nonNull(fItem.getMqConfig()))
                .mapToInt(item -> item.getMqConfig().getConsumerCount())
                .sum();

        int totalConsumerCount = handlers.size() + groupConsumerSum;
        List<DefaultMQPushConsumer> consumers = new ArrayList<>(totalConsumerCount);

        for (AbstractMessageHandler handler : handlers) {
            //没有就取默认配置
            RocketMqConfig mqConfig = Optional.ofNullable(handler.getMqConfig()).orElse(defaultMqConfig);
            int consumerCount = mqConfig.getConsumerCount();
            for (int i = 0; i < consumerCount; i++) {
                DefaultMQPushConsumer consumer = createAndStartListening(handler, mqConfig);
                consumers.add(consumer);
            }
        }

        //打印注册信息
        handlers.forEach(item -> {
            String topic = item.getHandleTag();
            int consumerCount = Optional.ofNullable(item.getMqConfig()).orElse(defaultMqConfig).getConsumerCount();
            log.info("MessageHandler【{}】注册 topic:【{}】 consumerCount:【{}】", item.getClass().getSimpleName(), topic, consumerCount);
        });

        return consumers;
    }

    @SuppressWarnings("unchecked")
    private static DefaultMQPushConsumer createAndStartListening(AbstractMessageHandler handler, RocketMqConfig mqConfig) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(mqConfig.getConsumerGroupName());
        consumer.setConsumeMessageBatchMaxSize(1);
        consumer.setNamesrvAddr(mqConfig.getNameServers());
        String handleTag = handler.getHandleTag();
        consumer.subscribe(mqConfig.getTopic(), handleTag);

        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            MessageExt msg = msgs.get(0);
            String tag = msg.getTags();
            if (handleTag.equals(tag)) {
                MessageBody messageBody;
                try {
                    messageBody = handler.getMessageBody(msg);
                } catch (IOException e) {
                    log.error("tag:【{}】 mag:【{}】", tag, msg);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                if (handler.handle(messageBody)) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } else {
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            } else {
                log.error("忽略消息：{}", tag);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        return consumer;
    }
}
