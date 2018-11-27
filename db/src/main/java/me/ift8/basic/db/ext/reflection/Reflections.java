package me.ift8.basic.db.ext.reflection;

import me.ift8.basic.db.ext.Fn;
import me.ift8.basic.db.ext.utils.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author Frank
 */
public class Reflections {
    private Reflections(){}

    public static String fnToFieldName(Fn fn){
        try {
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(fn);
            String           getter           = serializedLambda.getImplMethodName();
            return StringUtils.toCamel(getter.replace("get", ""));
        } catch (ReflectiveOperationException e) {
            throw new ReflectionOperationException(e);
        }
    }
}
