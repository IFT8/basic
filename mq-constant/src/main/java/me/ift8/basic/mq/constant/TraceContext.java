package me.ift8.basic.mq.constant;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TraceContext {
    String rootTraceId;
    String parentTraceId;
    String traceId;
}
