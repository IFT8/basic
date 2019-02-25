package me.ift8.basic.rocketmq.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

/**
 * Created by IFT8 on 2017/12/18.
 */
@Data
@Slf4j
@Configuration
public class RocketMqConfig {
    @Value("${rocketmq.nameservers}")
    private String nameServers;
    @Value("${rocketmq.topic}")
    private String topic;
    @Value("${rocketmq.producer.group:}")
    private String producerGroupName;
    @Value("${logging.config:}")
    private String loggingPath;

    @Value("${rocketmq.consumer.group:}")
    private String consumerGroupName;
    @Value("${rocketmq.consumer.count:}")
    private int consumerCount = 1;

    private boolean hasMqLogPath = false;
    /**
     * 记录收发日志
     */
    boolean recordMsgLog = false;
    /**
     * 记录完成日志
     */
    boolean recordFinishLog = true;

    private static final List<String> MQ_LOGGER_NAME_LIST = Lists.newArrayList("RocketmqCommon", "RocketmqClient", "RocketmqRemoting");

    @PostConstruct
    public void init() {
        //指定日志路径
        if (StringUtils.hasLength(this.loggingPath)) {
            this.hasMqLogPath = true;
            System.setProperty("rocketmq.client.log.loadconfig", "false");
            System.setProperty("rocketmq.client.log.configFile", this.loggingPath);
        }

        List<String> setMqLogLevel = searchAndSetDefaultMqLogLevel();
        log.info("MqConfig logPath={} setMqLogLevel={} 配置: {} ", this.loggingPath, setMqLogLevel, this.toString());
    }


    /**
     * 搜索Mq日志配置并设置默认级别
     *
     * @return 设置了级别的LoggerName
     */
    private List<String> searchAndSetDefaultMqLogLevel() {
        List<String> hasLoggerNameList = new ArrayList<>();
        if (this.hasMqLogPath) {
            try {

                hasLoggerNameList = searchHasLoggerNameList();

                setDefaultMqLogLevel(hasLoggerNameList);

            } catch (IOException e) {
                log.error("搜索Mq日志配置出错 path={}", this.loggingPath, e);
            }
        }
        return hasLoggerNameList;
    }

    private void setDefaultMqLogLevel(List<String> hasLoggerNameList) {
        List<String> needSetLoggerName = Lists.newArrayList(MQ_LOGGER_NAME_LIST);
        needSetLoggerName.removeAll(hasLoggerNameList);

        needSetLoggerName.forEach(item -> {
            try {
                LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                loggerContext.getLogger(item).setLevel(Level.WARN);

                log.info("设置{} Level.WARN", item);
            } catch (Exception e) {
                log.error("动态修改日志级别出错", e);
            }
        });
    }

    private List<String> searchHasLoggerNameList() throws IOException {
        String loggingPath = this.loggingPath;

        List<String> hasLoggerNameList = new ArrayList<>();

        //ClassPath路径下的
        if (loggingPath.startsWith(CLASSPATH_URL_PREFIX)) {
            loggingPath = loggingPath.substring(CLASSPATH_URL_PREFIX.length());
        }

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(loggingPath)) {
            Stream<String> lines = new BufferedReader(new InputStreamReader(resourceAsStream)).lines();
            lines.parallel().forEach(item -> hasLoggerNameList.addAll(MQ_LOGGER_NAME_LIST.stream().filter(item::contains).collect(Collectors.toList())));
        }

        return hasLoggerNameList;
    }
}
