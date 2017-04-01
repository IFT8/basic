package com.kongzhong.finance.basic.log.config.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;

/**
 * Created by IFT8 on 2017/3/30.
 */
public class PatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
        super.start();
    }


    @Override
    public String getPattern() {
        String pattern = super.getPattern();
        if (pattern == null || pattern.length() == 0) {
            super.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %le %lo[%thread]: %meta ## %msg %ex\n");
        }
        return super.getPattern();
    }
}
