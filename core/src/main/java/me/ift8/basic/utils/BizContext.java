package me.ift8.basic.utils;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 刘玉雨
 *
 * @version 1.0
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BizContext {

    private final static ThreadLocal<Map<String,Object>> CONTEXT = new ThreadLocal<>();

    public static void put(String name,Object value){
        Map<String, Object> currentContext = CONTEXT.get();
        if(currentContext == null){
            CONTEXT.set(Maps.newHashMap());
            currentContext = CONTEXT.get();
        }
        currentContext.put(name,value);
    }

    public static <T> T get(String name){
        Map<String, Object> currentContext = CONTEXT.get();
        if(currentContext == null){
            return null;
        }
        return (T) currentContext.get(name);
    }

    public static void remove(String name){
        Map<String, Object> currentContext = CONTEXT.get();
        if(currentContext != null){
            currentContext.remove(name);
        }
    }

    public static boolean contains(String name){
        Map<String, Object> currentContext = CONTEXT.get();
        if(currentContext != null){
            return currentContext.containsKey(name);
        }else{
            return false;
        }
    }

}
