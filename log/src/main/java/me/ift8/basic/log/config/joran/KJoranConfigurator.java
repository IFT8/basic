package me.ift8.basic.log.config.joran;

import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import me.ift8.basic.log.config.pattern.KPatternLayoutEncoder;

public class KJoranConfigurator extends JoranConfigurator {

    @Override
    protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
        addInfo("Setting up default registry.");
        super.addDefaultNestedComponentRegistryRules(registry);

        registry.add(AppenderBase.class, "encoder", KPatternLayoutEncoder.class);
        registry.add(UnsynchronizedAppenderBase.class, "encoder", KPatternLayoutEncoder.class);
        registry.add(OutputStreamAppender.class, "encoder", KPatternLayoutEncoder.class);
    }
}
