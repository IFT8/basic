package me.ift8.basic.biz.setting.db;

import java.util.List;
import java.util.Map;

/**
 * Created by IFT8 on 2017/5/24.
 */
public interface SettingDbService {

    String getString(String settingCode);

    List<String> getList(String settingCode);

    Map<String, String> getSettingDataMap(String settingCode);
}
