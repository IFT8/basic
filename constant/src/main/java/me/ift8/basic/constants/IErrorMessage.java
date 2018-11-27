package me.ift8.basic.constants;

import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.exception.SystemException;

/**
 * Created by IFT8 on 2017/7/27.
 */
public interface IErrorMessage {

    default ServiceException getServiceException() {
        return new ServiceException(getCode(), getMsg());
    }

    default ServiceException getServiceException(Throwable e) {
        return new ServiceException(getCode(), getMsg(), e);
    }

    default ServiceException getServiceException(String msg) {
        return new ServiceException(getCode(), msg == null ? getMsg() : msg);
    }

    default SystemException getSystemException() {
        return new SystemException(getCode(), getMsg());
    }

    default SystemException getSystemException(Throwable e) {
        return new SystemException(getCode(), getMsg(), e);
    }

    default SystemException getSystemException(String msg) {
        return new SystemException(getCode(), msg);
    }

    String getMsg();

    String getCode();
}
