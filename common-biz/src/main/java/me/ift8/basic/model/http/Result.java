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
     * !!! zuul转发规则 对应 本结构体和code
     *
     * ∙成功恒为 RES_CODE_SUCCESS -> 0
     * ∙系统异常恒为 ERROR_DEFAULT -> 10000
     * ∙业务异常为 RES_CODE_FAIL-> 1 或者对应业务异常码
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
        return new Result<>(ResponseCodeEnum.RES_CODE_SUCCESS.getCode(), ResponseCodeEnum.RES_CODE_SUCCESS.getDesc(), data);
    }

    public static <E> Result<E> fail(ServiceException e) {
        return new Result<>(e.getErrorCode(), e.getErrorMessage(), null);
    }

    public static <E> Result<E> fail(String msg) {
        return new Result<>(ResponseCodeEnum.RES_CODE_FAIL.getCode(), msg, null);
    }

    public static <E> Result<E> systemFail() {
        return new Result<>(ResponseCodeEnum.ERROR_DEFAULT.getCode(), ResponseCodeEnum.ERROR_DEFAULT.getDesc(), null);
    }

    public static <E> Result<E> systemFail(Throwable e) {
        return new Result<>(ResponseCodeEnum.ERROR_DEFAULT.getCode(), e.toString(), null);
    }

    public boolean isSuccess(){
        return ResponseCodeEnum.RES_CODE_SUCCESS.getCode().equals(this.code);
    }
}
