```
.
├── common-biz (业务公共代码)
│   └── me.ift8.basic
│                ├── constants
│                │   └── ResponseCodeEnum.java (Result code码Enum)
│                ├── model
│                │   ├── Parsable.java (对象转换接口 目标转换成this 默认实现依赖SpringBeanCopy)
│                │   ├── Transferable.java (对象转换接口 this转换成目标对象)
│                │   └── http
│                │       └── Result.java (controller 统一包装返回体)
│                └── utils
│                    └── HttpRequestInfoUtils.java (请求参数toJson)
│            
├── constant (基本 定义 异常 )
│   └── me.ift8.basic
│                ├── constants
│                │   ├── DataStatusEnum.java (数据启用状态)
│                │   └── ErrorMessage.java (基本异常码)
│                └── exception
│                        ├── ServiceException.java (业务异常 Exception)
│                        └── SystemException.java (系统异常 RuntimeException)
│     
├── core (核心 引入groovy lang3 !!与业务无关!! !!与服务容器(servlet,netty)无关!!)
│   └── me.ift8.basic
│                ├── metric
│                │   ├── MetricsAspect.java (打点日志记录 需要ACJ编译 )
│                │   └── annotation
│                │       └── Metrics.java (打点注解)
│                ├── task
│                │   └── TaskManager.java (本地线程任务池)
│                ├── encryption
│                │    └── Encryption.java (加解密公共组件)
│                │    └── BCrypt.java (BCrypt 算法实现，使用 PasswordUtil 调用即可)
│                └── utils
│                      ├── BeanMapper.java (依赖SpringBean)
│                      ├── DateUtils.java (Java8 时间封装)
│                      ├── DefaultValue.java (基本类型 默认值)
│                      ├── JsonUtils.java (Jackson)
│                      ├── XmlUtils.java (JacksonXML 需自行引入)
│                      ├── UUIDUtils.java (UUID生成 去除'-' )
│                      ├── PasswordUtil.java (校验加密过的密码，返回true/false)
│                      └── PreconditionsUtils.java (前置参数判断 抛出业务异常)
│    
├── metrics (InfluxDb打点)
│   └── me.ift8.basic.metrics
│                        ├── MetricsUtils.java (工具类，入口)
│                        └── MetricsClient.java (内部使用)
│
├── http-client (apche httpClient 池封装)
│   └── me.ift8.basic.http.client
│                             ├── HttpUtils.java (HTTP工具类)
│                             └── PoolingHttpClientManager.java
│    
├── ftp-client (apche ftpClient 池化封装)
│   └── me.ift8.basic.ftp.client
│                            ├── FTPCallback.java
│                            ├── PooledFtpManager.java
│                            ├── client
│                            │   ├── FtpClientConfig.java
│                            │   └── FtpClientFactory.java
│                            ├── pool
│                            │   ├── FtpClientPool.java
│                            │   └── FtpPoolConfig.java
│                            └── util
│                                └── FtpUtils.java (FTP工具类)
│    
├── log
│   └── me.ift8.basic.log
│                     ├── Logger.java
│                     ├── LoggerFactory.java
│                     ├── appender
│                     │   └── KAsyncAppender.java
│                     ├── config
│                     │   ├── KBasicConfigurator.java
│                     │   ├── joran
│                     │   │   └── KJoranConfigurator.java
│                     │   └── pattern
│                     │       └── KPatternLayoutEncoder.java
│                     ├── constant
│                     │   └── MDCConstants.java
│                     ├── converter
│                     │   ├── FormatConstants.java
│                     │   └── MetaConverter.java
│                     └── impl
│                         └── LoggerWrapper.java
│
│                     └── resources (直接include的配置)
│                         ├── k-base.xml (包含 k-console-appender k-file-appender)
│                         ├── k-console-appender.xml (控制台Appender)
│                         └── k-file-appender.xml (时间分割 异步日志文件Appender)
│
│                     └── test           
│                           └── resource
│                               ├── logback-include-dev.xml (dev Demo)
│                               ├── logback-include-prod.xml (prod Demo)
│                               └── logback.xml (Normal Demo)
│          
├── redis
│   └── me.ift8.basic.redis
│                        └── lock
│                            ├── RedisLock.java (redis分布锁实现)
│                            └── RedisLockFun.java 
│
├── db
│   └── me.ift8.basic.db
│                     ├── BaseMapper2.java (二次封装)
│                     ├── ExTransactional.java
│                     ├── MysqlMergeProvider.java
│                     ├── Order.java
│                     ├── config  (SpringBoot 项目自动配置)
│                     │   ├── druid
│                     │   │   ├── DruidAutoConfiguration.java
│                     │   │   └── DruidProperties.java
│                     │   ├── mybatis
│                     │   │   ├── MapperAutoConfiguration.java
│                     │   │   ├── MapperProperties.java
│                     │   │   ├── PageHelperAutoConfiguration.java
│                     │   │   └── PageHelperProperties.java
│                     │   └── package-info.java
│                     └── ext
│                         ├── Fn.java
│                         ├── Weekend.java
│                         ├── WeekendCriteria.java
│                         ├── reflection
│                         │   ├── ReflectionOperationException.java
│                         │   └── Reflections.java
│                         └── utils
│                             └── StringUtils.java
│
│                     └── resources
│                              └── generatorConfig.xml (自定义代码生成器配置demo https://github.com/IFT8/mybatis-generator)
│
├── setting-biz (依赖tkMybatis 建议配合basic.db使用 )
│   └── me.ift8.basic.biz.setting
│                            ├── cache
│                            │   ├── SettingCacheService.java ()
│                            │   └── SettingCacheServiceImpl.java
│                            ├── config
│                            │   └── SettingAutoConfig.java (自动配置类 继承并加  @Configuration即可 SpringBoot项目无需配置)
│                            └── db
│                                ├── SettingDbService.java
│                                ├── SettingDbServiceImpl.java
│                                ├── mapper
│                                │   └── ConfigMapper.java (需要扫描到本类 @MapperScan("me.ift8.**.mapper") )
│                                └── model
│                                    └── entity
│                                        └── ConfigEntity.java
│ 
│                            └── resources
│                                └── table.sql  (建表语句)
│
│── job (elastic-job 必须配置 jobschedule.registryCenter.server-lists、jobschedule.registryCenter.namespace)
│    └── me.ift8.basic.job
│                                    ├── AbstractSimpleJob.java (继承本类)
│                                    ├── annotation
│                                    │   └── JobSchedule.java (注解 cron配置)
│                                    ├── config
│                                    │   ├── JobScheduleConfig.java (Boot自动配置)
│                                    │   └── JobScheduleProperties.java
│                                    └── handler
│                                        ├── DefaultExecutorServiceHandler.java
│                                        └── DefaultJobExceptionHandler.java
│ 
└── webdriver (包装后的webDrvier)
     └── me.ift8.basic.webdriver
                          ├── ConstructibleCookie.java
                          ├── PooledWebDriverManager.java
                          ├── WebDriverCallback.java
                          ├── config
                          │   └── WebDriverConfig.java
                          ├── pool
                          │   ├── WebDriverPool.java
                          │   ├── WebDriverPoolConfig.java
                          │   └── WebDriverPooledObjectFactory.java
                          └── util
                              └── WebDriverUtils.java
                      
    
```

#### Metrics配置

需要添加配置类，并且在配置文件增加如下配置

配置（测试环境）
```
metrics.url=http://xxx.xxx.xxx.xxx:8086
metrics.username=qa
metrics.password=qa
metrics.database=metrics
common.appId=XxxService 
```
配置类
```

@Configuration
public class MetricsConfig {

    @Value("${common.appId}")
    private String appId;
    @Value("${metrics.url}")
    private String url;
    @Value("${metrics.username}")
    private String username;
    @Value("${metrics.password}")
    private String password;
    @Value("${metrics.database}")
    private String database;

    @Bean
    public MetricsUtils metricsUtils() {
        return new MetricsUtils(appId);
    }

    @Bean
    public MetricsClient metricsClient() {
        return new MetricsClient(url, username, password, database);
    }

}


```


#### MetricsAspec 切面配置(@Deprecated)

需要引入`aspectj-maven-plugin` 并 weaveDependencies 指定 `me.ift8.basic.core`

```
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>aspectj-maven-plugin</artifactId>
                <configuration>
                    <complianceLevel>${java.version}</complianceLevel>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <weaveDependencies>
                        <dependency>
                            <groupId>me.ift8.basic</groupId>
                            <artifactId>core</artifactId>
                        </dependency>
                    </weaveDependencies>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>test-compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```