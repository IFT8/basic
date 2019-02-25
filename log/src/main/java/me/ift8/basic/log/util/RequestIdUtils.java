package me.ift8.basic.log.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.ift8.basic.log.constant.MDCConstants;
import org.slf4j.MDC;

import java.util.Map;

/**
 * Created by IFT8 on 2019-02-19.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestIdUtils {
    private static final String NULL_REPLACE_CHARSETS = "^";
    private static final String JOIN_CHARSETS = "|";

    public static String getPinpointFullTraceId(ILoggingEvent event) {
        //异步从event的MDC获取
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        String transactionId = mdcPropertyMap.get(MDCConstants.PP_TRANSACTION_ID);
        String spanId = mdcPropertyMap.get(MDCConstants.PP_SPAN_ID);
        return transactionId == null ? MDC.get(MDCConstants.PP_TRANSACTION_ID) : transactionId + "|" + spanId;
    }
}
