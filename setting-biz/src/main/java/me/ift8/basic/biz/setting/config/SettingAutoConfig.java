package me.ift8.basic.biz.setting.config;

import me.ift8.basic.biz.setting.cache.SettingCacheService;
import me.ift8.basic.biz.setting.cache.SettingCacheServiceImpl;
import me.ift8.basic.biz.setting.db.SettingDbService;
import me.ift8.basic.biz.setting.db.SettingDbServiceImpl;
import me.ift8.basic.biz.setting.db.mapper.ConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Created by IFT8 on 2017/5/24.
 */
@Slf4j
@Configuration
public class SettingAutoConfig {
    @Autowired
    private StringRedisTemplate redisClient;
    @Autowired
    private ConfigMapper configMapper;

    @Bean
    public SettingCacheService settingCacheService() {
        SettingCacheServiceImpl settingCacheService = new SettingCacheServiceImpl(redisClient, settingDbService());
        log.info("[config] 配置SettingCache服务加载完成");
        return settingCacheService;
    }

    @Bean
    public SettingDbService settingDbService() {
        SettingDbServiceImpl settingDbService = new SettingDbServiceImpl(configMapper);
        log.info("[config] 配置SettingDB服务加载完成");
        return settingDbService;
    }
}
