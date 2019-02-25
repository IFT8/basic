package me.ift8.basic.kafka.sender;

import me.ift8.basic.kafka.config.KafkaConfigProperties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IFT8 on 2018/12/20.
 */
@Configuration
public class SenderConfig {
    @Resource
    private KafkaConfigProperties mqConfig;

    private Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>(16);

        // 指定连接broker列表
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, mqConfig.getServers());
        // 接入协议，sasl认证
        String securityProtocol = mqConfig.getSecurityProtocol();
        if (StringUtils.hasText(securityProtocol)) {
            props.put("security.protocol", securityProtocol);
        }
        props.put("sasl.mechanism", mqConfig.getSasl().getMechanism());

        // 为kafka提供认证服务名称
        props.put(SaslConfigs.SASL_KERBEROS_SERVICE_NAME, mqConfig.getSasl().getKerberosServiceName());

        // 对消息的key和value序列化
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, mqConfig.getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, mqConfig.getValueSerializer());
        // 回令类型
        props.put(ProducerConfig.ACKS_CONFIG, mqConfig.getAcks());
        // 批量提交大小
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, mqConfig.getBatchSize());
        // 延迟时间
        props.put(ProducerConfig.LINGER_MS_CONFIG, mqConfig.getLingerMs());
        // 重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, mqConfig.getRetries());
        return props;
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        return new KafkaProducer<>(producerConfigs());
    }

    @Bean
    public MessageSender messageSender() {
        return new MessageSender(kafkaProducer(), mqConfig.isRecordMsgLog(), mqConfig.isRecordFinishLog());
    }
}