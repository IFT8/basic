package me.ift8.basic.log.appender;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import me.ift8.basic.log.constant.MDCConstants;
import me.ift8.basic.log.util.RequestIdUtils;
import org.slf4j.MDC;

/**
 * Created by IFT8 on 2017/7/29.
 */
public class KAsyncAppender extends AsyncAppender {

    @Override
    protected void append(ILoggingEvent eventObject) {
        super.append(eventObject);
    }
}
