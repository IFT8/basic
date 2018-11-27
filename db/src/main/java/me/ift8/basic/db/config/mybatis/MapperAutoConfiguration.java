package me.ift8.basic.db.config.mybatis;

import me.ift8.basic.db.BaseMapper2;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Mapper 配置
 *
 * @author liuzh
 */
@Slf4j
@Configuration
@ConditionalOnBean(SqlSessionFactory.class)
@EnableConfigurationProperties(MapperProperties.class)
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class MapperAutoConfiguration {

    @Autowired
    private MapperProperties properties;

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void postContruct() {
        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(properties);
        if (!properties.getMappers().isEmpty()) {
            for (Class mapper : properties.getMappers()) {
                //提前初始化MapperFactoryBean,注册mappedStatements
                applicationContext.getBeansOfType(mapper);
                mapperHelper.registerMapper(mapper);
            }
        } else {
            //BaseKzMapper默认
            //提前初始化MapperFactoryBean,注册mappedStatements
            applicationContext.getBeansOfType(BaseMapper2.class);
            mapperHelper.registerMapper(BaseMapper2.class);
        }
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
            mapperHelper.processConfiguration(sqlSessionFactory.getConfiguration());
        }
        log.info("[config] 注册 tkMtBatis mappers={}", properties.getMappers());
    }
}