package me.ift8.basic.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by IFT8 on 2017/5/23.
 */
@FunctionalInterface
public interface WebDriverCallback<T> {
    T execute(RemoteWebDriver webDriver) throws Exception;
}
