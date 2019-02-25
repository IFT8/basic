package me.ift8.basic.trace.mq.utils;

import me.ift8.basic.mq.constant.MessageContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IFT8 on 2019-02-22.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetricMqUtils {

    public static Map<String, String> buildTagMapByMqContext(MessageContext messageContext) {
        Map<String, String> tagMap = new HashMap<>();
        if (messageContext == null) {
            return tagMap;
        }

        String topic = messageContext.getTopic();
        String terminalNo = messageContext.getTerminalNo();
        String deviceIp = messageContext.getDeviceIp();
        String key = messageContext.getKey();
        if (key != null) {
            tagMap.put("key", key);
        }

        if (topic != null) {
            tagMap.put("topic", topic);
        }
        if (terminalNo != null) {
            tagMap.put("terminalNo", terminalNo);
        }
        if (deviceIp != null) {
            tagMap.put("deviceIp", deviceIp);
        }

        return tagMap;
    }
}
