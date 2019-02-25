package me.ift8.basic.kafka.listener;

import me.ift8.basic.mq.constant.MessageBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by IFT8 on 2019-01-07.
 */
@Slf4j
@Component
public class DemoStrMessageHandler extends AbstractMessageHandler<MessageBody<Object>> {
    @Override
    protected String getHandleTopic() {
        return "great_device_data";
    }

    @Override
    protected void doHandle(MessageBody<Object> message) throws Exception {
        log.info("DemoMessageHandler.doHandle:【{}】", message);

    }
}
