package com.kongzhong.finance.basic.log.config.joran;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import com.kongzhong.finance.basic.log.config.pattern.PatternLayout;
import com.kongzhong.finance.basic.log.config.pattern.PatternLayoutEncoder;

public class JoranConfigurator extends ch.qos.logback.classic.joran.JoranConfigurator {

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        addInfo("Setting up default registry.");
        super.addDefaultNestedComponentRegistryRules(registry);

        registry.add(AppenderBase.class, "layout", PatternLayout.class);
        registry.add(UnsynchronizedAppenderBase.class, "layout", PatternLayout.class);
        registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(UnsynchronizedAppenderBase.class, "encoder", PatternLayoutEncoder.class);
        registry.add(OutputStreamAppender.class, "encoder", PatternLayoutEncoder.class);
    }
}
