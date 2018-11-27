package me.ift8.basic.biz.setting.db.enums;

/**
 * Created by IFT8 on 2017/7/18.
 */
public enum SettingTypeEnum {
    VALUE(1, "值"),
    LIST(2, "列表"),
    MAP(3, "Map");

    private Integer code;
    private String desc;

    SettingTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static SettingTypeEnum getByCode(Integer code) {
        if (null != code) {
            for (SettingTypeEnum anEnum : SettingTypeEnum.values()) {
                if (anEnum.getCode().equals(code)) {
                    return anEnum;
                }
            }
        }
        return null;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
}
