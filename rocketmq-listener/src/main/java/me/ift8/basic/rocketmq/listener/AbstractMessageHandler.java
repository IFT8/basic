package me.ift8.basic.rocketmq.listener;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.rocketmq.config.RocketMqConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

/**
 * Created by zhuye on 02/06/2017.
 */
@Slf4j
public abstract class AbstractMessageHandler<T extends MessageBody> {

    private static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    protected abstract String getHandleTag();

    protected abstract void doHandle(T message) throws Exception;

    /**
     * 默认使用传入的
     */
    protected RocketMqConfig getMqConfig() {
        return null;
    }

    @SuppressWarnings("unchecked")
    protected T getMessageBody(MessageExt message) throws IOException {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return objectMapper.readValue(message.getBody(), (Class<T>) type.getActualTypeArguments()[0]);
    }

    public boolean handle(T message) {
        String handleTag = getHandleTag();

        try {
            log.debug("tag:{} 收到消息: {}", handleTag, message);
            doHandle(message);
            log.debug("tag:{} 消息[处理完成] msg: {}", handleTag, message);
        } catch (ServiceException se) {
            log.warn("tag:{} 消息[处理失败] [业务异常] msg: {} error: {}", handleTag, message, se.getErrorMessage());
            return false;
        } catch (Exception ex) {
            log.error("tag:{} 消息[处理失败] [系统异常] msg: {}", handleTag, message, ex);
            return false;
        }

        return true;
    }
}
