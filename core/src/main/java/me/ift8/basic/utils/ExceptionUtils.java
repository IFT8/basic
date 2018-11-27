package me.ift8.basic.utils;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.exception.SystemException;
import lombok.NoArgsConstructor;

/**
 * @author 刘玉雨
 * @version 1.0
 */
@NoArgsConstructor
public class ExceptionUtils {

    public static ServiceException newServiceException(String code){
        String message = MessageResourceUtils.getMessage(code);
        return new ServiceException(code,message);
    }
    public static ServiceException newServiceException(String code,String message){
        return new ServiceException(code,message);
    }

    public static SystemException newSystemException(String code){
        String message = MessageResourceUtils.getMessage(code);
        return new SystemException(code,message);
    }
    public static SystemException newSystemException(String code,String message){
        return new SystemException(code,message);
    }

}
