package me.ift8.basic.log.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import me.ift8.basic.log.constant.MDCConstants;
import me.ift8.basic.trace.core.Trace;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.Map;
import java.util.Optional;

/**
 * Created by IFT8 on 2019-02-19.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestIdUtils {
    private static final String NULL_REPLACE_CHARSETS = "^";
    private static final String JOIN_CHARSETS = "|";

    public static String getCatFullTraceId(ILoggingEvent event) {
        //异步从event的MDC获取
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        String traceIdFromMDC = mdcPropertyMap.getOrDefault(MDCConstants.MDC_REQUEST_ID, MDC.get(MDCConstants.MDC_REQUEST_ID));
        if (traceIdFromMDC == null) {
            return getCatFullTraceIdByLocal();
        }
        return traceIdFromMDC;
    }

    public static String getCatFullTraceIdByLocal() {
        String currentMessageId = Optional.ofNullable(Trace.getCurrentMessageIdOnly()).orElse(NULL_REPLACE_CHARSETS);
        String rootId = Optional.ofNullable(Trace.getRootMessageId()).orElse(NULL_REPLACE_CHARSETS);
        String parentId = Optional.ofNullable(Trace.getParentMessageId()).orElse(NULL_REPLACE_CHARSETS);
        return rootId + JOIN_CHARSETS + parentId + JOIN_CHARSETS + currentMessageId;
    }

    public static String getPinpointFullTraceId(ILoggingEvent event) {
        //异步从event的MDC获取
        Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        String transactionId = mdcPropertyMap.get(MDCConstants.PP_TRANSACTION_ID);
        String spanId = mdcPropertyMap.get(MDCConstants.PP_SPAN_ID);
        return transactionId == null ? MDC.get(MDCConstants.PP_TRANSACTION_ID) : transactionId + "|" + spanId;
    }
}
