package me.ift8.basic.mq.constant.utils;

import me.ift8.basic.mq.constant.MessageContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by IFT8 on 2019-02-22.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MqContextUtils {

    public static MessageContext buildMessageContextBySimAndTopic(String sim, String topic) {
        return buildMessageContext(null, sim, null, topic, null);
    }

    public static MessageContext buildMessageContext(String appId, String sim, String ipPort, String topic, String key) {
        MessageContext messageContext = new MessageContext();
        messageContext.setAppId(appId);
        messageContext.setTopic(topic);
        messageContext.setKey(key);
        messageContext.setTerminalNo(sim);
        messageContext.setTimestamp(System.currentTimeMillis());
        if (ipPort != null) {
            String[] deviceIPPort = ipPort.replace("/", "").split(":");
            if (deviceIPPort.length == 2) {
                messageContext.setDeviceIp(deviceIPPort[0]);
                messageContext.setDevicePort(Integer.valueOf(deviceIPPort[1]));
            }
        }
        return messageContext;
    }
}
