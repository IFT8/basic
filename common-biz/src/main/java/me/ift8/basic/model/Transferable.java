package me.ift8.basic.model;

import me.ift8.basic.utils.BeanMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by IFT8 on 2017/5/12.
 */
public interface Transferable<T> {
    /**
     * this 转换成 目标对象
     */
    @SuppressWarnings("unchecked")
    default T transform() {
        Type[] genericInterfaces = this.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            ParameterizedType genericInterfaceParameterizedType = (ParameterizedType) genericInterface;

            //当前接口
            Type interfaceType = genericInterfaceParameterizedType.getRawType();
            if (Transferable.class == interfaceType) {
                //唯一就一个泛型参数
                Type[] typeArguments = genericInterfaceParameterizedType.getActualTypeArguments();
                Class<T> tClass = (Class<T>) typeArguments[0];

                return BeanMapper.map(this, tClass);
            }
        }

        return null;
    }

    default <O> O transform(Class<O> clazz) {
        return BeanMapper.map(this, clazz);
    }
}
