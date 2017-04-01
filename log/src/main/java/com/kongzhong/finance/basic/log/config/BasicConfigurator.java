package com.kongzhong.finance.basic.log.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.Encoder;
import com.kongzhong.finance.basic.log.config.pattern.PatternLayoutEncoder;

/**
 * Created by IFT8 on 2017/3/30.
 */
public class BasicConfigurator extends ch.qos.logback.classic.BasicConfigurator {

    @Override
    public void configure(LoggerContext lc) {
        addInfo("Setting up default configuration.");

        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(lc);
        ca.setName("console");
        Encoder<ILoggingEvent> encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.start();

        ca.setEncoder(encoder);
        ca.start();

        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(ca);
    }
}
