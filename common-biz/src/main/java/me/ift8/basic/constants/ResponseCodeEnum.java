package me.ift8.basic.constants;

/**
 * Created by IFT8 on 2017/4/7.
 */
public enum ResponseCodeEnum {
    SUCCESS("SUCCESS","成功"),
    FAIL("FAIL","业务异常"),
    SYSTEM_FAIL("SYSTEM_FAIL","系统异常")
    ;

    private String code;
    private String desc;

    ResponseCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ResponseCodeEnum getByCode(String code) {
        if (code != null) {
            for (ResponseCodeEnum o : values()) {
                if (o.getCode().equals(code)) {
                    return o;
                }
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
