package me.ift8.basic.biz.setting.db;

import me.ift8.basic.biz.setting.db.mapper.ConfigMapper;
import me.ift8.basic.biz.setting.db.model.entity.ConfigEntity;
import me.ift8.basic.constants.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IFT8 on 2017/5/24.
 */
@Slf4j
public class SettingDbServiceImpl implements SettingDbService {
    private ConfigMapper configMapper;

    public SettingDbServiceImpl(ConfigMapper configMapper) {
        this.configMapper = configMapper;
    }

    @Override
    public String getString(String code) {
        ConfigEntity configEntity = new ConfigEntity(code);
        ConfigEntity one = configMapper.findOne(configEntity);

        return one == null ? null : one.getValue();
    }

    @Override
    public List<String> getList(String code) {
        try {
            ConfigEntity configEntity = new ConfigEntity(code);
            List<ConfigEntity> all = configMapper.findAll(configEntity);

            return all == null ? null : all.stream().map(ConfigEntity::getValue).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("【数据库异常】获取设置失败 settingCode={}", code, e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }

    @Override
    public Map<String, String> getSettingDataMap(String code) {
        try {
            ConfigEntity configEntity = new ConfigEntity(code);
            List<ConfigEntity> all = configMapper.findAll(configEntity);

            return all == null ? null : all.stream().collect(Collectors.toMap(ConfigEntity::getValue, ConfigEntity::getContent));
        } catch (Exception e) {
            log.error("【数据库异常】获取设置失败 settingCode={}", code, e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }
}
