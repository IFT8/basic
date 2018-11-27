package me.ift8.basic.utils;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author 刘玉雨
 * @version 1.0
 */
public class MessageResourceUtils {
    private MessageResourceUtils(){}

    public static String getMessage(String code, Object[] args, Locale locale){
        ReloadableResourceBundleMessageSource messageResource = SpringUtils.getBean(ReloadableResourceBundleMessageSource.class);
        return messageResource.getMessage(code, args, locale);
    }

    public static String getMessage(String code){
        return getMessage(code,null, Locale.SIMPLIFIED_CHINESE);
    }
}
