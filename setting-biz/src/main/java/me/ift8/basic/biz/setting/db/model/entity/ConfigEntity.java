package me.ift8.basic.biz.setting.db.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IFT8 on 2017/05/24.
 */
@Data
@NoArgsConstructor
@Table(name = "`config`")
public class ConfigEntity {
    /**
     * key
     */
    private String code;

    /**
     * 类型 1 String 2 Set 3 Map
     */
    private Integer type;

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

    /**
     *  创建时间
     */
    private Date createdAt;

    /**
     *  最后更新时间
     */
    private Date updatedAt;

    public ConfigEntity(String code) {
        this.code = code;
    }

    public static final class Constants {
        public static final String FIELD_ID = "id";

        public static final String FIELD_CODE = "code";

        public static final String FIELD_TYPE = "type";

        public static final String FIELD_VALUE = "value";

        public static final String FIELD_CONTENT = "content";

        public static final String FIELD_REMARK = "remark";

        public static final String FIELD_CREATED_AT = "createdAt";

        public static final String FIELD_UPDATED_AT = "updatedAt";
    }
}