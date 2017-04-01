package com.kongzhong.finance.basic.log.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by IFT8 on 2017/3/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogEvent extends LoggingEvent {

    public LogEvent(String fqcn, Logger logger, Level level, String message, Throwable throwable, Object[] argArray) {
        super(fqcn, logger, level, message, throwable, argArray);
    }

    private String appId;
    private String requestId;
}
