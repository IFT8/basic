package me.ift8.basic.constants;

import lombok.Getter;

import java.util.Arrays;

/**
 * Created by IFT8 on 2017/4/7.
 */
public enum ResponseCodeEnum {
    RES_CODE_SUCCESS("0", "RES_SUCCESS", "成功"),
    RES_CODE_FAIL("1", "RES_FAIL", "失败"),
    ERROR_DEFAULT("10000", "ERROR_DEFAUL", "系统异常"),
    PARAM_FAIL("10001", "PARAM_ERROR", "参数错误");

    @Getter
    private final String code;
    @Getter
    private final String partnerCode;
    @Getter
    private final String desc;


    ResponseCodeEnum(String code, String innerCode, String desc) {
        this.code = code;
        this.partnerCode = innerCode;
        this.desc = desc;
    }


    public static ResponseCodeEnum parse(String code) {
        return Arrays.stream(values()).filter(item -> item.code.equals(code))
                .findFirst()
                .orElseThrow(() -> ErrorMessage.MISSING_PARAM.getSystemException(String.format("找不到code:%s", code)));
    }
}
