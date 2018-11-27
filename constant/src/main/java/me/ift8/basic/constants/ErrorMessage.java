package me.ift8.basic.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by IFT8 on 17/4/5.
 */
@AllArgsConstructor
public enum ErrorMessage implements IErrorMessage {
    UNKNOW_ERROR("UNKNOW_ERROR", "未知异常"),
    SYS_ERROR("SYSTEM_ERROR", "系统异常"),
    MISSING_PARAM("MISSING_PARAM", "缺少参数"),
    PARTNER_SYS_ERROR("PARTNER_SYS_ERROR", "外部系统异常"),
    TOO_MANY_REQUEST("TOO_MANY_REQUEST", "请求过于频繁，请稍后再试");

    @Getter
    private final String code;
    @Getter
    private final String msg;
}
