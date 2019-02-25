package me.ift8.basic.log.config.pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.PatternLayoutEncoderBase;
import me.ift8.basic.log.constant.MDCConstants;
import me.ift8.basic.log.converter.RequestIdConverter;

/**
 * Created by IFT8 on 2017/3/30.
 */
public class KPatternLayoutEncoder extends PatternLayoutEncoderBase<ILoggingEvent> {

    @Override
    public void start() {
        buildPatternLayout();
        super.start();
    }

    private void buildPatternLayout() {
        PatternLayout patternLayout = new PatternLayout();
        PatternLayout.defaultConverterMap.put(MDCConstants.REQUEST_ID, RequestIdConverter.class.getName());
        patternLayout.setContext(context);
        patternLayout.setPattern(getPattern());
        patternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        patternLayout.start();
        this.layout = patternLayout;
    }

    @Override
    public String getPattern() {
        String pattern = super.getPattern();
        if (pattern == null || pattern.length() == 0 || "PATTERN_IS_UNDEFINED".equals(pattern)) {
            super.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %le %lo [%thread] %" + MDCConstants.REQUEST_ID + " #ELK# %msg %ex\n");
        }
        return super.getPattern();
    }
}
