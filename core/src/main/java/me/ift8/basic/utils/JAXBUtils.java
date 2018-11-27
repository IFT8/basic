package me.ift8.basic.utils;

import me.ift8.basic.constants.ErrorMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by IFT8 on 2016/10/15.
 */
@Slf4j
@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JAXBUtils {

    public static <T> String toXml(T object, boolean hadHeadInfo) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            // 将对象转变为xml Object------XML
            // 指定对应的xml文件
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);//是否格式化生成的xml串
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, !hadHeadInfo);//是否省略xml头信息

            // 将对象转换为对应的XML文件
            marshaller.marshal(object, byteArrayOutputStream);
            //转化为字符串返回
            return new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8"));
        } catch (Exception e) {
            log.error("转换成XMl异常 param={}", JsonUtils.toJson(object), e);
            throw ErrorMessage.SYS_ERROR.getSystemException();
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> T fromXml(String xmlContent, Class<T> clazz) {
        if (xmlContent == null) {
            return null;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // xml转换为对象 XML------Object
            InputStream inputStream;
            inputStream = new ByteArrayInputStream(xmlContent.getBytes());
            Unmarshaller um = context.createUnmarshaller();
            return (T) um.unmarshal(inputStream);
        } catch (JAXBException e) {
            log.error("xml转换成对象异常 param={}", xmlContent, e);
            throw ErrorMessage.SYS_ERROR.getSystemException();
        }
    }
}
