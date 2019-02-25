package me.ift8.basic.kafka.config;


import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.utils.BeanMapper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Slf4j
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaConfigProperties implements Cloneable {
    private static final String DEFAULT_DESERIALIZER = StringDeserializer.class.getName();
    private static final String DEFAULT_SERIALIZER = StringSerializer.class.getName();

    String servers;
    String securityProtocol = "SASL_PLAINTEXT";
    String keySerializer = DEFAULT_SERIALIZER;
    String valueSerializer = DEFAULT_SERIALIZER;
    String acks = "all";
    int batchSize = 16 * 1024;
    int lingerMs = 1;
    int retries = 0;
    String groupId;
    String realm;
    String kdc;
    String principal;
    String keyTabHex;
    Sasl sasl = new Sasl();
    Consumer consumer = new Consumer();
    /**
     * 记录收发日志
     */
    boolean recordMsgLog = false;
    /**
     * 记录完成日志
     */
    boolean recordFinishLog = true;

    String loginConfigPath = "/data/kafka_auth/jaas.conf";
    String krb5Path = "/data/kafka_auth/krb5.conf";

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Sasl {
        String kerberosServiceName = "kafka";
        String mechanism = "GSSAPI";
    }

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Consumer {
        boolean enableAutoCommit = false;
        int autoCommitIntervalMs = 1000;
        String autoOffsetReset = "earliest";
        int sessionTimeoutMs = 30000;
        int heartbeatIntervalMs = 10000;
        String keyDeserializer = DEFAULT_DESERIALIZER;
        String valueDeserializer = DEFAULT_DESERIALIZER;
        int threads = 5;
        int pollRecords = 5;
        long timeout = 5000;
        long waitIntervalMs = 500;
        /**
         * 确保消息都消费(但是可能造成重复消费)
         */
        boolean ensureConsume = false;
        /**
         * 消费者数量(提高吞吐)
         */
        int consumerCount = 1;
        /**
         * 屏蔽消费(测试使用)
         */
        boolean suppressAllConsumer = false;
        /**
         * 记录完成日志
         */
        boolean recordSleepLog = false;
    }

    @PostConstruct
    public void init() {
        //配置验签文件地址
        System.setProperty("java.security.auth.login.config", this.loginConfigPath);
        //配置验签文件地址
        System.setProperty("java.security.krb5.conf", this.krb5Path);
        //环境域名，固定项
        if (this.realm != null) {
            System.setProperty("java.security.krb5.realm", this.realm);
        }
        //环境kdc服务器，固定项
        if (this.kdc != null) {
            System.setProperty("java.security.krb5.kdc", this.kdc);
        }
        //kafka用户
        if (this.principal != null) {
            System.setProperty("java.security.auth.login.generator.kafka.client.principal", this.principal);
        }
        //keytab文件内容，从GMQ平台获取
        if (this.keyTabHex != null) {
            System.setProperty("java.security.auth.login.generator.kafka.client.keyTabHex", this.keyTabHex);
        }

        log.info("[config] KafkaConfig 初始化: {}", this);

        if (this.servers == null) {
            throw ErrorMessage.MISSING_PARAM.getSystemException("Kafka Broker未配置");
        }
    }

    @Override
    public KafkaConfigProperties clone() {
        KafkaConfigProperties deepCopy = BeanMapper.map(this, KafkaConfigProperties.class);
        Consumer consumer = BeanMapper.map(this.getConsumer(), Consumer.class);
        deepCopy.setConsumer(consumer);
        Sasl sasl = BeanMapper.map(this.getSasl(), Sasl.class);
        deepCopy.setSasl(sasl);

        return deepCopy;
    }
}
