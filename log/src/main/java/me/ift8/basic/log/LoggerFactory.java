package me.ift8.basic.log;

import me.ift8.basic.log.impl.LoggerWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by IFT8 on 2017/3/30.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(name);
        LoggerWrapper loggerWrapper = new LoggerWrapper();
        loggerWrapper.setLogger(logger);
        return loggerWrapper;
    }
}
