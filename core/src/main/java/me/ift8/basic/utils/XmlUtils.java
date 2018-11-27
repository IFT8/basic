package me.ift8.basic.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

/**
 * Created by IFT8 on 2016/10/15.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtils {
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

    @Getter
    private static final ObjectMapper XML_MAPPER = new XmlMapper()
            //字段为null，自动忽略，不再序列化
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            //XML标签名:使用骆驼命名的属性名，
            .setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE)
            //设置转换模式
            .enable(MapperFeature.USE_STD_BEAN_NAMING);

    /**
     * obj转xml不捕获异常
     */
    public static String toXmlWithinException(Object o, boolean hadHeadInf) throws JsonProcessingException {
        String xml = XML_MAPPER.writeValueAsString(o);
        if (hadHeadInf) {
            return XML_HEAD + xml;
        }
        return xml;
    }

    /**
     * obj转xml
     */
    public static String toXml(Object o, boolean hadHeadInf) {
        try {
            String xml = XML_MAPPER.writeValueAsString(o);
            if (hadHeadInf) {
                return XML_HEAD + xml;
            }
            return xml;
        } catch (Exception e) {
            log.error("转换成XMl异常[系统异常] Object={}", o, e);
        }
        return null;
    }

    /**
     * xml转obj
     */
    public static <T> T fromXml(String xmlContent, Class<T> classOfT) {
        if (xmlContent == null) {
            return null;
        }
        try {
            return XML_MAPPER.readValue(xmlContent, classOfT);
        } catch (Exception e) {
            log.error("XML反序列化失败[系统异常] xmlContent={}", xmlContent, e);
        }
        return null;
    }

    /**
     * xml转obj
     */
    public static <T> T fromXml(String xmlContent, TypeReference<T> typeOfT) {
        if (xmlContent == null) {
            return null;
        }
        try {
            return XML_MAPPER.readValue(xmlContent, typeOfT);
        } catch (Exception e) {
            log.error("XML反序列化失败[系统异常] xmlContent={}", xmlContent, e);
        }
        return null;
    }

    /**
     * xml转obj
     */
    public static <T> T fromXml(String xmlContent, Type type) {
        if (xmlContent == null) {
            return null;
        }
        try {
            return XML_MAPPER.readValue(xmlContent, genJavaType(type));
        } catch (Exception e) {
            log.error("XML反序列化失败[系统异常] xmlContent={}", xmlContent, e);
        }
        return null;
    }

    public static JavaType genJavaType(Type type) {
        return XML_MAPPER.getTypeFactory().constructType(type);
    }

    public static <T> String toXml(T object) {
        return toXml(object, false);
    }
}
