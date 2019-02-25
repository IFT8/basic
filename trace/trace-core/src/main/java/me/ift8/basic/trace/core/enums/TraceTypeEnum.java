package me.ift8.basic.trace.core.enums;

import me.ift8.basic.constants.ErrorMessage;
import lombok.Getter;

import java.util.Arrays;

/**
 * Created by IFT8 on 2019-02-01.
 */
public enum TraceTypeEnum {
    URL("URL", "URL"),
    LOG("LOG", "LOG"),
    SQL("SQL", "SQL"),
    CALL("Call", "Call"),
    MQ_SEND("MqSend", "MqSend"),
    MQ_LISTEN("MqListen", "MqListen"),
    CONNECTION("Connection", "Connection"),
    SERVICE("Service", "Service"),
    TASK("Task", "Task"),
    POOL("Pool", "POOL"),
    CACHE("Cache", "Cache");

    @Getter
    private final String code;
    @Getter
    private final String desc;

    TraceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TraceTypeEnum parse(String code) {
        return Arrays.stream(values()).filter(item -> item.code.equals(code))
                .findFirst()
                .orElseThrow(() -> ErrorMessage.MISSING_PARAM.getSystemException(String.format("找不到code:%s", code)));
    }
}
