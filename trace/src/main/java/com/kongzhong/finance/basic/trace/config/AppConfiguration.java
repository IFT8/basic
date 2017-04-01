package com.kongzhong.finance.basic.trace.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by IFT8 on 2017/3/31.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfiguration {
    private static volatile String appId = System.getProperty("APPID");

    public static String getAppId() {
        return StringUtils.isNotEmpty(appId) ? appId : "unknown";
    }
}
