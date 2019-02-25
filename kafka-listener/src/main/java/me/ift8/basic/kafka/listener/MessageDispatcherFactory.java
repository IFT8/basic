package me.ift8.basic.kafka.listener;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.kafka.config.KafkaConfigProperties;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.utils.NamedThreadPoolFactory;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by IFT8 on 2017/12/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageDispatcherFactory {

    public static List<Consumer<String, String>> createMqConsumerList(KafkaConfigProperties defaultMqConfig, List<AbstractMessageHandler> handlers) {
        if (handlers == null || handlers.isEmpty()) {
            throw ErrorMessage.SYS_ERROR.getSystemException("没有任何消息处理程序！");
        }

        boolean moreThan = handlers.stream().map(AbstractMessageHandler::getHandleTopic)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream().anyMatch(p -> p.getValue() > 1);
        if (moreThan) {
            throw ErrorMessage.SYS_ERROR.getSystemException("消息处理程序不能处理相同的Topic！");
        }

        //统计Group内Consumer数量
        int groupConsumerSum = handlers.stream().filter(fItem -> Objects.nonNull(fItem.getMqConfig()))
                .mapToInt(item -> item.getMqConfig().getConsumer().getConsumerCount())
                .sum();

        int totalConsumerCount = handlers.size() + groupConsumerSum;
        List<Consumer<String, String>> consumers = new ArrayList<>(totalConsumerCount);

        ThreadPoolExecutor threadPoolExecutor = NamedThreadPoolFactory.simpleNamedThreadPoolExecutor(totalConsumerCount, "MQ_CONSUMER");
        for (AbstractMessageHandler handler : handlers) {
            //没有就取默认配置
            KafkaConfigProperties mqConfig = Optional.ofNullable(handler.getMqConfig()).orElse(defaultMqConfig);
            Map<String, Object> consumerConfigs = ListenerConfig.consumerConfigs(mqConfig);

            int consumerCount = mqConfig.getConsumer().getConsumerCount();

            //开始监听
            for (int i = 0; i < consumerCount; i++) {
                Consumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs);
                consumer.subscribe(Lists.newArrayList(handler.getHandleTopic()));

                threadPoolExecutor.submit(() -> startListening(handler, consumer, mqConfig));

                consumers.add(consumer);
            }
        }

        //打印注册信息
        handlers.forEach(item -> {
            String topic = item.getHandleTopic();
            int consumerCount = Optional.ofNullable(item.getMqConfig()).orElse(defaultMqConfig).getConsumer().getConsumerCount();
            log.info("MessageHandler【{}】注册 topic:【{}】 consumerCount:【{}】", item.getClass().getSimpleName(), topic, consumerCount);
        });


        return consumers;
    }

    @SuppressWarnings("unchecked")
    private static void startListening(AbstractMessageHandler handler, Consumer<String, String> consumer, KafkaConfigProperties mqConfig) {
        long pollTimeOut = mqConfig.getConsumer().getTimeout();
        long waitIntervalMs = mqConfig.getConsumer().getWaitIntervalMs();
        boolean isEnsureConsume = mqConfig.getConsumer().isEnsureConsume();
        boolean isRecordSleepLog = mqConfig.getConsumer().isRecordSleepLog();
        String topic = handler.getHandleTopic();

        boolean needSleep = false;
        while (true) {
            long currentTimeMillis = System.currentTimeMillis();
            int count = 0;
            try {
                ConsumerRecords<String, String> records = consumer.poll(pollTimeOut);
                if (records.isEmpty()) {
                    needSleep = true;
                }
                count = records.count();
                int failCount = 0;

                for (ConsumerRecord<String, String> record : records) {
                    try {
                        if (mqConfig.isRecordMsgLog()) {
                            log.debug("topic:{} 收到消息: {}", topic, record.value());
                        }
                        MessageBody messageBody = handler.getMessageBody(record);
                        boolean success = handler.handle(mqConfig.isRecordFinishLog(), record.key(), messageBody);
                        if (!success) {
                            failCount++;
                        }
                    } catch (Exception e) {
                        if (isEnsureConsume) {
                            throw e;
                        }
                        log.error("topic:【{}】msg:【{}】", topic, record.value(), e);
                        failCount++;
                    }
                }
                if (failCount < 1 || !isEnsureConsume) {
                    consumer.commitSync();
                }
            } catch (Exception e) {
                log.error("topic:【{}】count:【{}】耗时:【{} ms】 poll exception, retry later", topic, count, (System.currentTimeMillis() - currentTimeMillis), e);
                needSleep = true;
            }
            if (count > 0) {
                log.debug("topic:【{}】本次poll消费size:【{}】总耗时:【{} ms】", topic, count, (System.currentTimeMillis() - currentTimeMillis));
            }
            if (needSleep) {
                try {
                    TimeUnit.MICROSECONDS.sleep(waitIntervalMs);
                    needSleep = false;
                    if (isRecordSleepLog) {
                        log.debug("topic:【{}】等待数据好 waitIntervalMs:【{} ms】", topic, waitIntervalMs);
                    }
                } catch (InterruptedException e1) {
                    log.error("unknow exception", e1);
                }
            }
        }
    }
}
