package me.ift8.basic.webdriver.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by IFT8 on 2017/5/19.
 */
public class WebDriverPoolConfig extends GenericObjectPoolConfig {
    public WebDriverPoolConfig() {
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认
        setTimeBetweenEvictionRunsMillis(60000L);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        setMinEvictableIdleTimeMillis(1800000L);
    }
}
