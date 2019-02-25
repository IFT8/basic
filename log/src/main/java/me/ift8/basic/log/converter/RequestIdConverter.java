package me.ift8.basic.log.converter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.DynamicConverter;
import me.ift8.basic.log.util.RequestIdUtils;

public class RequestIdConverter extends DynamicConverter<ILoggingEvent> {

    @Override
    public String convert(ILoggingEvent event) {
        String requestId = RequestIdUtils.getPinpointFullTraceId(event);
        return "[" + format(requestId) + "]";
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
