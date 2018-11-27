package me.ift8.basic.webdriver.config;

import lombok.Data;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Data
public class WebDriverConfig {
    private Boolean isHeadless;
    /**
     * UA 默认 Iphone 5
     */
    private Boolean isMoble;
    /**
     * 默认不显示图片
     */
    private Boolean displayImage;
    private String driverPath;
    private Integer scriptTimeoutSecond;
    private Integer pageLoadTimeoutSecond;
}
