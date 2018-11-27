package me.ift8.basic.constants;

/**
 * Created by IFT8 on 2017/5/16.
 * 数据状态Enum 数据启用与否
 */
public enum DataStatusEnum {
    DEFAULT(0, "默认值"),
    ENABLED(1, "启用"),
    DISABLED(2, "禁止");

    private Integer code;
    private String desc;

    DataStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static DataStatusEnum getByCode(Integer code) {
        if (null != code) {
            for (DataStatusEnum anEnum : DataStatusEnum.values()) {
                if (anEnum.getCode().equals(code)) {
                    return anEnum;
                }
            }
        }
        return DEFAULT;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
