package me.ift8.basic.db.ext.utils;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

/**
 * @author Frank
 */
public class StringUtils {
    private StringUtils(){}

    public static String toCamel(String pascal){
        return UPPER_CAMEL.to(LOWER_CAMEL,pascal);
    }
}
