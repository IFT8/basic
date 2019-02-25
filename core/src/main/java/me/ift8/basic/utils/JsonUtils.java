package me.ift8.basic.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by IFT8 on 2017/3/30.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {
    @Getter
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            //未知枚举当做NULL
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            //未知字段忽略
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            //空对象
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            //J8时间
            .registerModule(new JavaTimeModule())
            .registerModule(new Jdk8Module());

    /**
     * obj转json
     */
    public static String toJson(Object o) {
        try {
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.error("Json序列化失败[系统异常] Object={}", o, e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return OBJECT_MAPPER.readValue(json, classOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败[系统异常] json={}", json, e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, TypeReference<T> typeOfT) {
        try {
            return OBJECT_MAPPER.readValue(json, typeOfT);
        } catch (Exception e) {
            log.error("Json反序列化失败[系统异常] json={}", json, e);
        }
        return null;
    }

    /**
     * json转obj
     */
    public static <T> T fromJson(String json, Type type) {
        try {
            return OBJECT_MAPPER.readValue(json, genJavaType(type));
        } catch (Exception e) {
            log.error("Json反序列化失败[系统异常] json={}", json, e);
        }
        return null;
    }

    /**
     * json转obj不捕获异常
     */
    public static <T> T fromJsonWithinThrowable(String json, TypeReference<T> typeOfT) throws IOException {
        return OBJECT_MAPPER.readValue(json, typeOfT);
    }

    /**
     * json转obj不捕获异常
     */
    public static <T> T fromJsonWithinThrowable(String json, Class<T> classOfT)  throws IOException{
        return OBJECT_MAPPER.readValue(json, classOfT);
    }

    /**
     * json转obj不捕获异常
     */
    public static <T> T fromJsonWithinThrowable(String json, Type type) throws IOException {
        return OBJECT_MAPPER.readValue(json, genJavaType(type));
    }

    /**
     * obj转json不捕获异常
     */
    public static String toJsonWithinException(Object o) throws IOException {
        return OBJECT_MAPPER.writeValueAsString(o);
    }

    public static JavaType genJavaType(Type type) {
        return OBJECT_MAPPER.getTypeFactory().constructType(type);
    }
}
