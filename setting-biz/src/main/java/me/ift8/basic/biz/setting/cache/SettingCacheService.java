package me.ift8.basic.biz.setting.cache;


import java.util.Set;

/**
 * Created by IFT8 on 16/8/16.
 */
public interface SettingCacheService {
    String LOCK_SUFFIX = "_lock";

    /**
     * 获取配置值
     *
     * @param settingCode 对应原先RedisKey
     */
    String get(final String settingCode);

    Integer getInteger(final String settingCode);
    Long getLong(final String settingCode);

    /**
     * getBoolean(null)    = null
     * getBoolean("true")  = Boolean.TRUE
     * getBoolean("T")     = Boolean.TRUE
     * getBoolean("false") = Boolean.FALSE
     * getBoolean("f")     = Boolean.FALSE
     * getBoolean("No")    = Boolean.FALSE
     * getBoolean("n")     = Boolean.FALSE
     * getBoolean("on")    = Boolean.TRUE
     * getBoolean("ON")    = Boolean.TRUE
     * getBoolean("off")   = Boolean.FALSE
     * getBoolean("oFf")   = Boolean.FALSE
     * getBoolean("yes")   = Boolean.TRUE
     * getBoolean("Y")     = Boolean.TRUE
     * getBoolean("blue")  = null
     * getBoolean("true ") = null
     * getBoolean("ono")   = null
     */
    Boolean getBoolean(final String settingCode);

    /**
     * value是否存在于集合中
     *
     * @param settingCode 对应原先RedisKey
     */
    Boolean sismember(final String settingCode, final String value);

    /**
     * hget
     *
     * @param settingCode 对应原先RedisKey
     * @param field       field
     */
    String hget(final String settingCode, final String field);

    /**
     * smembers Set全部元素
     */
    Set<String> smembers(final String settingCode);
}
