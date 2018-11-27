package me.ift8.basic.model.http;

import me.ift8.basic.constants.ResponseCodeEnum;
import me.ift8.basic.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IFT8 on 17/4/5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<E> {
    /**
     * ∙成功恒为 SUCCESS
     * ∙系统异常恒为 SYSTEM_FAIL
     * ∙业务异常为 FAIL 或者对应业务异常码
     */
    private String code;
    /**
     * ∙成功恒为 '成功'
     * ∙系统异常恒为 '系统异常'
     * ∙业务异常为 错误信息
     */
    private String msg;
    /**
     * 额外的数据
     */
    private E data;

    public static <E> Result<E> success() {
        return success(null);
    }

    public static <E> Result<E> success(E data) {
        return new Result<>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getDesc(), data);
    }

    public static <E> Result<E> fail(ServiceException e) {
        return new Result<>(e.getErrorCode(), e.getErrorMessage(), null);
    }

    public static <E> Result<E> fail(String msg) {
        return new Result<>(ResponseCodeEnum.FAIL.getCode(), msg, null);
    }

    public static <E> Result<E> systemFail() {
        return new Result<>(ResponseCodeEnum.SYSTEM_FAIL.getCode(), ResponseCodeEnum.SYSTEM_FAIL.getDesc(), null);
    }

    public static <E> Result<E> systemFail(Throwable e) {
        return new Result<>(ResponseCodeEnum.SYSTEM_FAIL.getCode(), e.toString(), null);
    }
}
