package com.kongzhong.finance.basic.log.config.pattern;


import com.kongzhong.finance.basic.log.converter.MetaConverter;

public class PatternLayout extends ch.qos.logback.classic.PatternLayout {
    static {
        defaultConverterMap.put("meta", MetaConverter.class.getName());
    }
}
