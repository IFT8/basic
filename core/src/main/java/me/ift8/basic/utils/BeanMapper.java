package me.ift8.basic.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.ift8.basic.exception.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IFT8 on 2017/3/30.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class BeanMapper {

    public static Map<String, Object> bean2Map(Object obj) {
        return bean2Map(obj, null, null, false);
    }

    public static Map<String, Object> bean2Map(Object obj, List<String> includes, List<String> excludes) {
        return bean2Map(obj, includes, excludes, false);
    }

    public static Map<String, Object> bean2Map(Object obj, List<String> includes) {
        return bean2Map(obj, includes, null, false);
    }

    public static Map<String, Object> bean2Map(Object obj, List<String> includes, boolean includeNull) {
        return bean2Map(obj, includes, null, includeNull);
    }

    public static Map<String, Object> bean2Map(Object obj, List<String> includes, List<String> excludes, boolean includeNull) {
        if (obj == null) {
            return Maps.newLinkedHashMap();
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(obj);
        PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
        Map<String, Object> map = new LinkedHashMap<>();
        for (PropertyDescriptor aDescriptor : descriptor) {
            String name = aDescriptor.getName();
            //不包含字段
            if (excludes != null && excludes.contains(name)) {
                continue;
            }

            //包含字段
            if (includes == null || includes.contains(name)) {
                Object propertyValue = beanWrapper.getPropertyValue(name);
                //是否包含空值
                if (propertyValue == null) {
                    if (includeNull) {
                        map.put(name, "");
                    }
                } else {
                    map.put(name, propertyValue);
                }
            }
        }
        //移除class属性
        map.remove("class");
        return map;
    }


    public static <B, D> List<D> mapList(List<B> source, Class<D> cla) {
        if (source == null) {
            return Lists.newArrayList();
        }
        List<D> listD = new ArrayList<>();
        for (B b : source) {
            D d = map(b, cla);
            listD.add(d);
        }
        return listD;
    }

    public static <S, D> D map(S source, Class<D> cla) {
        if (source == null) {
            return null;
        }
        try {
            D target = cla.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("BeanCopy[系统异常]", e);
            throw new SystemException("BEAN_COPY_SYSTEM_EXCEPTION", "BeanCopy系统异常", e);
        }
    }


}
