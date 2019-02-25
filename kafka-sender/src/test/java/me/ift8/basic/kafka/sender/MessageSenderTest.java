package me.ift8.basic.kafka.sender;

import me.ift8.basic.mq.constant.MessageBody;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IFT8 on 2018-12-20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = {"me.ift8"})
public class MessageSenderTest {
    @Resource
    private MessageSender messageSender;

    @Test
    public void sendMq() {
        MessageBody<DemoMessage> messageBody = new MessageBody<>();
        messageBody.setData(makeDemoMessage());

        messageSender.sendMQWithJson("great_device_data", "10086", messageBody);
    }

    private DemoMessage makeDemoMessage() {
        DemoMessage demoMessage = new DemoMessage();
        demoMessage.setMsgId(1001);
        demoMessage.setBigDecimal(BigDecimal.valueOf(12.0380));
        demoMessage.setMsgDesc("msgDesc哦");
        demoMessage.setStringList(Lists.newArrayList("s1", "s2", "s3"));

        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        demoMessage.setMap(map);

        demoMessage.setByteParam("测试ABC123".getBytes(Charset.defaultCharset()));
        return demoMessage;
    }

    @Test
    public void sendBytesMsg() {
        MessageBody<byte[]> messageBody = new MessageBody<>();
        messageBody.setData("asjdlajs".getBytes());

        messageSender.sendMQWithJson("great_device_data", "10086", messageBody);
    }

    @Test
    public void sendListMsg() {
        MessageBody<List<DemoMessage>> messageBody = new MessageBody<>();
        DemoMessage demoMessage = makeDemoMessage();
        demoMessage.setMsgId(2);
        messageBody.setData(Lists.newArrayList(makeDemoMessage(), demoMessage));

        messageSender.asyncSendMQWithJson("great_device_data", "10086", messageBody);
    }

    @Test
    public void sendMapMsg() {
        MessageBody<Map<String, DemoMessage>> messageBody = new MessageBody<>();
        Map<String, DemoMessage> map = new HashMap<>();
        map.put("key1", makeDemoMessage());
        DemoMessage demoMessage = makeDemoMessage();
        demoMessage.setMsgId(2);
        map.put("key2", demoMessage);

        messageBody.setData(map);

        messageSender.asyncSendMQWithJson("great_device_data", "10086", messageBody);
    }
}