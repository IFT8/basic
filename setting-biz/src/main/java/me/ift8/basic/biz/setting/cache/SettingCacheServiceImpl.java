package me.ift8.basic.biz.setting.cache;


import com.google.common.collect.Lists;
import me.ift8.basic.biz.setting.db.SettingDbService;
import me.ift8.basic.redis.lock.RedisLock;
import me.ift8.basic.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IFT8 on 16/8/16.
 */
@Slf4j
public class SettingCacheServiceImpl implements SettingCacheService {

    private StringRedisTemplate redisClient;
    private SettingDbService settingDbService;

    public SettingCacheServiceImpl(StringRedisTemplate redisClient, SettingDbService settingDbService) {
        this.redisClient = redisClient;
        this.settingDbService = settingDbService;
    }

    /**
     * 获取配置值
     *
     * @param settingCode 对应原先RedisKey
     */
    @Override
    public String get(final String settingCode) {
        //先取缓存
        final String[] result = {getValueByCache(settingCode)};
        if (result[0] != null) {
            return result[0];
        }

        try {
            RedisLock lock = new RedisLock(redisClient, settingCode + LOCK_SUFFIX);
            lock.doInLock(() ->
                    //取不到 则取Db
                    result[0] = loadAndCacheValueByDb(settingCode)
            );
        } catch (Exception e) {
            log.error("[Redis异常]", e);
        }
        return result[0];
    }

    @Override
    public Integer getInteger(String settingCode) {
        String value = get(settingCode);
        return StringUtils.isEmpty(value) ? null : Integer.valueOf(value);
    }

    @Override
    public Long getLong(String settingCode) {
        String value = get(settingCode);
        return StringUtils.isEmpty(value) ? null : Long.valueOf(value);
    }

    @Override
    public Boolean getBoolean(String settingCode) {
        String value = get(settingCode);
        return StringUtils.isEmpty(value) ? null : BooleanUtils.toBoolean(value);
    }

    /**
     * value是否存在于集合中
     *
     * @param settingCode 对应原先RedisKey
     */
    @Override
    public Boolean sismember(final String settingCode, final String value) {
        final Boolean[] sismember = {null};
        try {
            sismember[0] = sismemberByRedis(settingCode, value);
        } catch (Exception e) {
            log.error("redis查询设置失败【系统异常】,settingCode={}", settingCode, e);
        }
        if (sismember[0] != null) {
            return sismember[0];
        }

        try {
            RedisLock lock = new RedisLock(redisClient, settingCode + LOCK_SUFFIX);
            lock.doInLock(() ->
                    //调用Db查
                    sismember[0] = sismemberAndCacheByDb(settingCode, value)
            );
        } catch (Exception e) {
            log.error("[Redis异常]", e);
        }

        return sismember[0];
    }

    /**
     * hget
     *
     * @param settingCode 对应原先RedisKey
     * @param field       field
     */
    @Override
    public String hget(final String settingCode, final String field) {
        // 读缓存
        final String[] content = {hgetByRedis(settingCode, field)};
        if (content[0] != null) {
            return content[0];
        }

        // key存在则返回
        if (redisClient.hasKey(settingCode)) {
            return null;
        }

        try {
            RedisLock lock = new RedisLock(redisClient, settingCode + LOCK_SUFFIX);
            lock.doInLock(() ->
                    //调用Db查
                    content[0] = hgetAndCacheByDb(settingCode, field)
            );
        } catch (Exception e) {
            log.error("[Redis异常]", e);
        }

        return content[0];
    }

    /**
     * smembers Set全部元素
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<String> smembers(String settingCode) {
        final Set<String>[] smembers = new Set[]{new HashSet<String>()};
        try {
            smembers[0] = smembersByRedis(settingCode);
        } catch (Exception e) {
            log.error("redis查询设置失败【系统异常】,settingCode={}", settingCode, e);
        }
        if (smembers[0] != null) {
            return smembers[0];
        }

        try {
            RedisLock lock = new RedisLock(redisClient, settingCode + LOCK_SUFFIX);
            lock.doInLock(() ->
                    //调用Db查
                    smembers[0] = smembersAndCacheByDb(settingCode)
            );
        } catch (Exception e) {
            log.error("[Redis异常]", e);
        }

        return smembers[0];
    }

    /*  是否在redis集合内  */
    private Boolean sismemberByRedis(final String settingCode, final String value) {
        Boolean result = redisClient.boundSetOps(settingCode).isMember(value);
        //还需要再判断是否存在 && 不存在key 从库里拿
        if (!result && !redisClient.hasKey(settingCode)) {
            result = null;
        }
        return result;
    }

    /*  redis集合  */
    private Set<String> smembersByRedis(final String settingCode) {
        Set<String> result = redisClient.boundSetOps(settingCode).members();
        //还需要再判断是否存在 && 不存在key 从库里拿
        if (result.isEmpty() && !redisClient.hasKey(settingCode)) {
            result = null;
        }
        return result;
    }

    /*  调用Db查询设置列表并缓存   */
    private Boolean sismemberAndCacheByDb(final String settingCode, final String value) {
        try {
            //二次check
            Boolean sismemberByRedis = sismemberByRedis(settingCode, value);
            if (sismemberByRedis != null) {
                return sismemberByRedis;
            }
            long start = System.currentTimeMillis();
            //读Db
            List<String> result = settingDbService.getList(settingCode);
            long readEndTime = System.currentTimeMillis();
            //缓存
            cacheList(settingCode, result);
            log.info("调用Db查询设置成功 ,settingCode={},result={},size={},readMs={},cacheMs={}",
                    settingCode, JsonUtils.toJson(result), result.size(), readEndTime - start, System.currentTimeMillis() - readEndTime);
            return result != null && result.contains(value);
        } catch (Exception e) {
            log.error("调用Db查询设置【系统异常】,settingCode={}", settingCode, e);
        }
        return null;
    }

    /*  调用Db查询设置列表并缓存   */
    private Set<String> smembersAndCacheByDb(final String settingCode) {
        try {
            //二次check
            Set<String> smembers = smembersByRedis(settingCode);
            if (smembers != null) {
                return smembers;
            }
            long start = System.currentTimeMillis();
            //读Db
            List<String> result = settingDbService.getList(settingCode);
            long readEndTime = System.currentTimeMillis();
            //缓存
            cacheList(settingCode, result);
            log.info("调用Db查询设置成功 ,settingCode={},result={},size={},readMs={},cacheMs={}",
                    settingCode, JsonUtils.toJson(result), result.size(), readEndTime - start, System.currentTimeMillis() - readEndTime);
            return new HashSet<>(result);
        } catch (Exception e) {
            log.error("调用Db查询设置【系统异常】,settingCode={}", settingCode, e);
        }
        return null;
    }

    /*  缓存设置值
          value为null时变空字串
    */
    private void cacheValue(final String settingCode, String value) {
        try {
            if (value == null) {
                value = "";
            }
            redisClient.opsForValue().set(settingCode, value);
        } catch (Exception e) {
            log.error("redis缓存设置失败【系统异常】,settingCode={},value={}", settingCode, JsonUtils.toJson(value), e);
        }
    }

    /* 缓存集合 */
    private void cacheList(final String settingCode, List<String> valueList) {
        try {
            //缓存
            if (valueList == null || valueList.isEmpty()) {
                valueList = Lists.newArrayList("");
            }
            redisClient.opsForSet().add(settingCode, valueList.toArray(new String[]{}));
        } catch (Exception e) {
            log.error("redis缓存设置失败【系统异常】,settingCode={},value={}", settingCode, JsonUtils.toJson(valueList), e);
        }
    }

    /* 从缓存取值 */
    private String getValueByCache(final String settingCode) {
        try {
            return redisClient.opsForValue().get(settingCode);
        } catch (Exception e) {
            log.error("redis查询设置失败【系统异常】,settingCode={}", settingCode, e);
        }
        return null;
    }

    /* 载入Db配置并缓存 */
    private String loadAndCacheValueByDb(final String settingCode) {
        try {
            //再check一次
            String resultByCache = getValueByCache(settingCode);
            if (resultByCache != null) {
                return resultByCache;
            }

            long start = System.currentTimeMillis();
            String result = settingDbService.getString(settingCode);
            long readEndTime = System.currentTimeMillis();

            //缓存
            cacheValue(settingCode, result);

            log.info("调用Db查询设置成功, settingCode={},result={},readMs={},cacheMs={}", settingCode, result, readEndTime - start, System.currentTimeMillis() - readEndTime);
            return result;
        } catch (Exception e) {
            log.error("调用Db查询设置失败【系统异常】,settingCode={}", settingCode, e);
        }
        return null;
    }

    private String hgetAndCacheByDb(String settingCode, String settingValue) {
        try {
            //二次check
            // 读缓存
            String content = hgetByRedis(settingCode, settingValue);
            if (content != null) {
                return content;
            }

            // key存在则返回
            if (redisClient.hasKey(settingCode)) {
                return null;
            }

            //读Db
            Map<String, String> result = settingDbService.getSettingDataMap(settingCode);

            log.info("调用getSettingDataMap查询设置成功 ,settingCode={},result={},size={}", settingCode, JsonUtils.toJson(result), result.size());
            //缓存
            cacheMap(settingCode, result);
            log.info("缓存DataMap成功,settingCode={},result={}", settingCode, JsonUtils.toJson(result));
            return result.get(settingValue);
        } catch (Exception e) {
            log.error("调用Db查询设置【系统异常】,settingCode={}", settingCode, e);
        }
        return null;
    }

    private void cacheMap(String settingCode, Map<String, String> dataMap) {
        if (dataMap != null && dataMap.size() != 0) {
            redisClient.opsForHash().putAll(settingCode, dataMap);
        } else {
            redisClient.opsForHash().put(settingCode, "", "");
        }
    }

    private String hgetByRedis(String settingCode, String settingValue) {
        return (String) redisClient.opsForHash().get(settingCode, settingValue);
    }
}
