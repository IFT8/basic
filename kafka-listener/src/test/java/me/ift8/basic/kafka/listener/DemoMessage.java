package me.ift8.basic.kafka.listener;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by IFT8 on 2018-12-21.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoMessage {
    Integer msgId;
    String msgDesc;
    BigDecimal bigDecimal;
    List<String> stringList;
    Map<String, String> map;
    byte[] byteParam;
}
