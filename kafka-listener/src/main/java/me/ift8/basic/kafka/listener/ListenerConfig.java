package me.ift8.basic.kafka.listener;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.kafka.config.KafkaConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Slf4j
@Configuration
public class ListenerConfig {
    @Resource
    private KafkaConfigProperties kafkaConfigProperties;
    @Autowired(required = false)
    private List<AbstractMessageHandler> handlers;

    @PostConstruct
    private void init() {
        if (!CollectionUtils.isEmpty(handlers) && !kafkaConfigProperties.getConsumer().isSuppressAllConsumer()) {
            MessageDispatcherFactory.createMqConsumerList(kafkaConfigProperties, handlers);
        }
        log.info("[] mqConfig={}", kafkaConfigProperties);
    }

    public static Map<String, Object> consumerConfigs(KafkaConfigProperties mqConfig) {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, mqConfig.getServers());
        // consumerGroupId
        String groupId = mqConfig.getGroupId();
        if (groupId == null) {
            throw ErrorMessage.MISSING_PARAM.getSystemException("Kafka GroupId未配置");
        }
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // 接入协议，sasl认证
        String securityProtocol = mqConfig.getSecurityProtocol();
        if (StringUtils.hasText(securityProtocol)) {
            props.put("security.protocol", securityProtocol);
        }

        // 为kafka提供认证服务名称
        String kerberosServiceName = mqConfig.getSasl().getKerberosServiceName();
        if (StringUtils.hasText(kerberosServiceName)) {
            props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, kerberosServiceName);
        }

        // 是否自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, mqConfig.getConsumer().isEnableAutoCommit());
        // 提交周期
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, mqConfig.getConsumer().getAutoCommitIntervalMs());
        // 自动重置offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, mqConfig.getConsumer().getAutoOffsetReset());
        // 心跳离线超时时间
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, mqConfig.getConsumer().getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, mqConfig.getConsumer().getHeartbeatIntervalMs());
        // key序列化
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, mqConfig.getConsumer().getKeyDeserializer());
        // value序列化
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, mqConfig.getConsumer().getValueDeserializer());
        // 每次最大拉取数量
        props.put("max.poll.records", mqConfig.getConsumer().getPollRecords());
        // 每次最大拉取数量 0.9.0.1没有max.poll.records这个参数，假设1个消息1k
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, mqConfig.getConsumer().getPollRecords() * 1024);
        return props;
    }
}
