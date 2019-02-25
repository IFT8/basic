package me.ift8.basic.mq.constant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by IFT8 on 2018-12-21.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageContext {
    String appId;
    String terminalNo;
    Long timestamp;
    String topic;
    String key;
    String serverIp;
    Integer serverPort;
    String deviceIp;
    Integer devicePort;
    TraceContext traceContext;
}
