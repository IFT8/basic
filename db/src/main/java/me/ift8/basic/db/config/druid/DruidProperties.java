package me.ift8.basic.db.config.druid;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 只提供了常用的属性，如果有需要，自己添加
 *
 * @author liuzh
 * @since 2017/2/5.
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DruidProperties {
    private String url;
    private String username;
    private String password;
    private String driverClass;

    private int maxActive;
    private int minIdle;
    private int initialSize;
    private boolean testOnBorrow;
    private long maxWait = 180000L;
    private String initConnectionSqls;
}
