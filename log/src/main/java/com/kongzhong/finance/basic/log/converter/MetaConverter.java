package com.kongzhong.finance.basic.log.converter;

import ch.qos.logback.core.pattern.DynamicConverter;
import com.kongzhong.finance.basic.log.impl.LogEvent;

public class MetaConverter extends DynamicConverter<LogEvent> {

    @Override
    public String convert(LogEvent event) {
        StringBuilder message = new StringBuilder();
        message.append("[");
        message.append(format(event.getAppId())).append(FormatConstants.ARROW);
        message.append(format(event.getRequestId()));
        message.append("]");
        return message.toString();
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
