package me.ift8.basic.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 刘玉雨
 * @version 1.0
 */
public class TimeUnitConverter {
    private TimeUnitConverter(){}
    private final static Map<TimeUnit,String> MAP = new HashMap<>();
    static {
        MAP.put(TimeUnit.SECONDS,"秒");
        MAP.put(TimeUnit.MINUTES,"分钟");
        MAP.put(TimeUnit.HOURS,"小时");
        MAP.put(TimeUnit.DAYS,"天");
    }

    public static String convert(TimeUnit timeUnit){
        return MAP.get(timeUnit);
    }
}
