package me.ift8.basic.kafka.listener;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.kafka.config.KafkaConfigProperties;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Slf4j
public abstract class AbstractMessageHandler<T extends MessageBody> {

    protected abstract String getHandleTopic();

    protected abstract void doHandle(T message) throws Exception;

    /**
     * 默认使用传入的
     */
    protected KafkaConfigProperties getMqConfig() {
        return null;
    }

    @SuppressWarnings("unchecked")
    protected T getMessageBody(ConsumerRecord<String, String> record) throws IOException {
        String value = record.value();

        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return JsonUtils.fromJsonWithinThrowable(value, type.getActualTypeArguments()[0]);
    }

    public boolean handle(boolean recordFinishLog, String key, T messageBody) {
        long begin = System.currentTimeMillis();
        String topic = getHandleTopic();

        try {
            doHandle(messageBody);
            if (recordFinishLog) {
                log.debug("topic:{} 消息[处理完成] key:【{}】耗时:【{}】 msg:【{}】", topic, key, (System.currentTimeMillis() - begin), messageBody);
            }

        } catch (ServiceException se) {
            log.warn("topic:{} 消息[处理失败] [业务异常] key:【{}】 msg:【{}】 error: {}", topic, messageBody, se.getErrorMessage());
            return false;
        } catch (Exception ex) {
            log.error("topic:{} 消息[处理失败] [系统异常] key:【{}】 msg:【{}】", topic, key, messageBody, ex);
            return false;
        }
        return true;
    }
}
