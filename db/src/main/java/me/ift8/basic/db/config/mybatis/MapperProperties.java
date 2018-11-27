package me.ift8.basic.db.config.mybatis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tk.mybatis.mapper.entity.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for Mybatis.
 *
 * @author Eddú Meléndez
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = MapperProperties.MYBATIS_PREFIX)
public class MapperProperties extends Config {

    public static final String MYBATIS_PREFIX = "mybatis";

    private List<Class> mappers = new ArrayList<>();
}