package me.ift8.basic.rocketmq.sender;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.mq.constant.MessageBody;
import me.ift8.basic.mq.constant.MessageContext;
import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.io.IOException;

/**
 * Created by IFT8 on 2017/12/19.
 */
@Slf4j
public class MessageSender {
    private DefaultMQProducer mqProducer;
    private boolean recordMsgLog;
    private boolean recordFinishLog;

    public MessageSender(DefaultMQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public MessageSender(DefaultMQProducer mqProducer, boolean recordMsgLog, boolean recordFinishLog) {
        this.mqProducer = mqProducer;
        this.recordMsgLog = recordMsgLog;
        this.recordFinishLog = recordFinishLog;
    }

    public boolean sendMQ(String topic, String tags, MessageBody msgBody) {
        return sendMQ(topic, tags, msgBody, null);
    }

    public boolean sendMQ(String topic, String tags, MessageBody msgBody, Integer delayTimeLevel) {
        long begin = System.currentTimeMillis();
        Message msg = buildMessage(msgBody, topic, tags);

        if (delayTimeLevel != null) {
            msg.setDelayTimeLevel(delayTimeLevel);
        }

        if (this.recordMsgLog) {
            log.debug("发送MQ消息 topic:【{}】tags:【{}】 msgBody:【{}】", topic, tags, msgBody);
        }
        try {
            SendResult result = mqProducer.send(msg);

            if (result.getSendStatus() == SendStatus.SEND_OK) {
                if (this.recordFinishLog) {
                    log.info("发送MQ消息成功 topic:【{}】tags:【{}】，发送内容：{}", topic, tags, msgBody);
                }
                return true;

            } else {
                log.error("发送MQ消息失败 topic:【{}】tags:【{}】result:{} 发送内容：{}", topic, tags, result, msgBody);
                return false;
            }

        } catch (Exception ex) {
            log.error("发送MQ消息出错 msg={}", msgBody, ex);
            return false;
        }
    }

    /**
     * MQ上下文追加
     */
    private Message buildMessage(MessageBody msgBody, String topic, String tags) {
        String msgJson;
        try {
            msgJson = JsonUtils.toJsonWithinException(msgBody);
        } catch (IOException e) {
            log.error("发送MQ消息[序列化异常]：Body:{}", msgBody);
            throw ErrorMessage.SYS_ERROR.getSystemException("发送MQ消息[序列化异常]");
        }

        Message msg = new Message(topic, tags, msgJson.getBytes());

        MessageContext context = msgBody.getContext();
        if (context == null) {
            context = new MessageContext();
            msgBody.setContext(context);
        }
        context.setTimestamp(System.currentTimeMillis());
        context.setKey(tags);
        context.setTopic(topic);

        return msg;
    }
}
