package me.ift8.basic.biz.setting.db.model.dto;

import me.ift8.basic.biz.setting.db.enums.SettingTypeEnum;
import lombok.Data;

/**
 * Created by IFT8 on 2017/7/25.
 */
@Data
public class ConfigDto {
    /**
     * key
     */
    private String code;

    /**
     * 类型
     */
    private SettingTypeEnum type;

    /**
     * 值
     */
    private String value;

    /**
     * value的content(主要用于map结构)
     */
    private String content;

    /**
     * 描述
     */
    private String remark;
}
