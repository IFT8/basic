package me.ift8.basic.log.converter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.DynamicConverter;
import me.ift8.basic.log.constant.MDCConstants;
import org.slf4j.MDC;

import java.util.Map;

public class MetaConverter extends DynamicConverter<ILoggingEvent> {
    @Override
    public String convert(ILoggingEvent event) {
        String requestId = getFullTraceId(event);
        return "[" + format(requestId) + "]";
    }

    private String getFullTraceId(ILoggingEvent event) {
        //异步从event的MDC获取
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        String transactionId = mdcPropertyMap.get(MDCConstants.PP_TRANSACTION_ID);
        String spanId = mdcPropertyMap.get(MDCConstants.PP_SPAN_ID);
        return transactionId == null ? MDC.get(MDCConstants.PP_TRANSACTION_ID) : transactionId + "|" + spanId;
    }

    private static String format(Object obj) {
        if (obj == null) {
            return FormatConstants.PLACE_HOLDER;
        } else {
            String ret = obj.toString();
            if (FormatConstants.EMPTY.equals(ret) || FormatConstants.BLANK.equals(ret)) {
                return FormatConstants.PLACE_HOLDER;
            } else {
                return ret;
            }
        }
    }
}
