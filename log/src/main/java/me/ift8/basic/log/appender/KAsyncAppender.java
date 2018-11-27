package me.ift8.basic.log.appender;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Created by IFT8 on 2017/7/29.
 */
public class KAsyncAppender extends AsyncAppender {

    @Override
    protected void append(ILoggingEvent eventObject) {
//        String fullTraceId = TraceUtils.getFullTraceId();
//        MDC.put(MDCConstants.MDC_REQUEST_ID, fullTraceId);
        super.append(eventObject);
//        MDC.remove(MDCConstants.MDC_REQUEST_ID);
    }
}
