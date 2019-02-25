package me.ift8.basic.kafka.sender;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.fun.DoSomethingFun;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.mq.constant.MessageContext;
import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Slf4j
public class MessageSender {
    private KafkaProducer<String, String> mqProducer;

    private boolean recordMsgLog;
    private boolean recordFinishLog;

    public MessageSender(KafkaProducer<String, String> mqProducer, boolean recordMsgLog, boolean recordFinishLog) {
        this.mqProducer = mqProducer;
        this.recordMsgLog = recordMsgLog;
        this.recordFinishLog = recordFinishLog;
    }

    /**
     * 同步发MQ
     */
    public void sendMQWithJson(String topic, String key, MessageBody msgBody) {
        sendAndMetricsMq(topic, key, msgBody, () -> {
            appenderContex(msgBody, topic, key);
            String msgJson = msg2Json(topic, key, msgBody);

            Future<RecordMetadata> send = mqProducer.send(new ProducerRecord<>(topic, key, msgJson));

            try {
                send.get();
            } catch (Exception e) {
                throw ErrorMessage.SYS_ERROR.getSystemException(e);
            }
        });
    }

    /**
     * 异步发MQ
     */
    public void asyncSendMQWithJson(String topic, String key, MessageBody msgBody) {
        appenderContex(msgBody, topic, key);
        String msgJson = msg2Json(topic, key, msgBody);

        if (recordMsgLog) {
            log.debug("发送MQ消息 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody);
        }

        mqProducer.send(new ProducerRecord<>(topic, key, msgJson), (metadata, exception) -> {
            if (exception != null) {
                log.error("发送MQ消息出错 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody, exception);
                return;
            }

            if (recordFinishLog) {
                log.debug("发送MQ消息成功 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody);
            }
        });
    }

    private void sendAndMetricsMq(String topic, String key, MessageBody msgBody, DoSomethingFun sendFun) {
        try {
            if (recordMsgLog) {
                log.debug("发送MQ消息 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody);
            }

            sendFun.doSomething();

            if (recordFinishLog) {
                log.debug("发送MQ消息成功 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody);
            }

        } catch (Exception ex) {
            log.error("发送MQ消息出错 topic:【{}】key:【{}】 msgBody:【{}】", topic, key, msgBody, ex);
        }
    }

    private String msg2Json(String topic, String key, Object obj) {
        try {
            return JsonUtils.toJsonWithinException(obj);
        } catch (IOException e) {
            log.error("发送MQ消息[序列化异常] topic:【{}】key:【{}】 msgBody:【{}】", topic, key, obj);
            throw ErrorMessage.SYS_ERROR.getSystemException("发送MQ消息[序列化异常]");
        }
    }

    /**
     * MQ上下文追加
     */
    private void appenderContex(MessageBody msgBody, String topic, String key) {
        MessageContext context = msgBody.getContext();
        if (context == null) {
            context = new MessageContext();
            msgBody.setContext(context);
        }
        context.setTimestamp(System.currentTimeMillis());
        context.setKey(key);
        context.setTopic(topic);
    }

}
