package me.ift8.basic.utils;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.exception.ServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

/**
 * Created by IFT8 on 2017/5/19.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PreconditionsUtils {

    /**
     * 对象不能为空
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static <T> void notNull(T reference) throws ServiceException {
        notNull(reference, null);
    }

    public static <T> void notNull(T reference, String msg) throws ServiceException {
        if (reference == null) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }

    /**
     * 字串不能为空
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static <T extends String> void notBlank(T reference) throws ServiceException {
        notBlank(reference, null);
    }

    public static <T extends String> void notBlank(T reference, String msg) throws ServiceException {
        if (reference == null || "".equals(reference)) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }

    /**
     * 集合不能为空
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static <T extends Collection> void notEmpty(T reference) throws ServiceException {
        notEmpty(reference, null);
    }

    public static <T extends Collection> void notEmpty(T reference, String msg) throws ServiceException {
        if (reference == null || reference.isEmpty()) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }

    /**
     * 集合不能为空
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static <T extends Map> void notEmpty(T reference) throws ServiceException {
        notEmpty(reference, null);
    }

    public static <T extends Map> void notEmpty(T reference, String msg) throws ServiceException {
        if (reference == null || reference.isEmpty()) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }

    /**
     * 条件不为true
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static void notTrue(boolean expression) throws ServiceException {
        notTrue(expression, null);
    }

    public static void notTrue(boolean expression, String msg) throws ServiceException {
        if (!expression) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }

    /**
     * 条件为true
     *
     * @throws ServiceException MISSING_PARAM("MISSING_PARAM", "缺少参数")
     */
    public static void isTrue(boolean expression) throws ServiceException {
        isTrue(expression, null);
    }

    public static void isTrue(boolean expression, String msg) throws ServiceException {
        if (expression) {
            throw ErrorMessage.MISSING_PARAM.getServiceException(msg);
        }
    }
}
