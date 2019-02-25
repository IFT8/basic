package me.ift8.basic.kafka.listener;

import me.ift8.basic.kafka.config.KafkaConfigProperties;
import me.ift8.basic.mq.constant.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by IFT8 on 2019-01-07.
 */
@Slf4j
@Component
public class DemoMessageHandler extends AbstractMessageHandler<MessageBody<DemoMessage>> {
    @Resource
    private KafkaConfigProperties kafkaConfigProperties;

    @Override
    protected String getHandleTopic() {
        return "great_gps_trace";
    }

    @Override
    protected void doHandle(MessageBody<DemoMessage> message) throws Exception {
        log.info("DemoMessageHandler.doHandle:【{}】", message);
        if(message.getData()!=null){
            String byteStr = new String(message.getData().getByteParam());
            log.info("byteStr:【{}】", byteStr);
        }
    }

    @Override
    protected KafkaConfigProperties getMqConfig() {
        KafkaConfigProperties clone = kafkaConfigProperties.clone();
        clone.getConsumer().setConsumerCount(2);
        return clone;
    }
}
