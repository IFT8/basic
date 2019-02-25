package me.ift8.basic.log.config.pattern;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import net.logstash.logback.decorate.JsonFactoryDecorator;

public class G7JsonFactoryDecorator implements JsonFactoryDecorator {
    public G7JsonFactoryDecorator() {
    }

    @Override
    public MappingJsonFactory decorate(MappingJsonFactory factory) {
        //禁用jackson对非ascii码字符进行escape编码
        factory.disable(JsonGenerator.Feature.ESCAPE_NON_ASCII);
        return factory;
    }
}