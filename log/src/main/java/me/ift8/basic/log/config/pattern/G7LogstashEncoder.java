package me.ift8.basic.log.config.pattern;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import me.ift8.basic.log.constant.MDCConstants;
import me.ift8.basic.log.converter.RequestIdConverter;
import net.logstash.logback.composite.CompositeJsonFormatter;
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder;

/**
 * Created by IFT8 on 2019-01-16.
 */
public class G7LogstashEncoder extends LoggingEventCompositeJsonEncoder {

    @Override
    protected CompositeJsonFormatter<ILoggingEvent> createFormatter() {
        CompositeJsonFormatter<ILoggingEvent> formatter = super.createFormatter();
        //默认使用
        formatter.setJsonFactoryDecorator(new G7JsonFactoryDecorator());
        PatternLayout.defaultConverterMap.put(MDCConstants.REQUEST_ID, RequestIdConverter.class.getName());
        return formatter;
    }
}
