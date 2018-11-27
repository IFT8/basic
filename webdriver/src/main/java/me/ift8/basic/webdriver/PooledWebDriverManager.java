package me.ift8.basic.webdriver;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.webdriver.config.WebDriverConfig;
import me.ift8.basic.webdriver.pool.WebDriverPool;
import me.ift8.basic.webdriver.pool.WebDriverPoolConfig;
import me.ift8.basic.webdriver.util.WebDriverUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PooledWebDriverManager {

    private WebDriverPool webDriverPool;
    private DriverService driverService;

    public PooledWebDriverManager(WebDriverConfig webDriverConfig) throws IOException {
        init(webDriverConfig);
        this.webDriverPool = new WebDriverPool(this.driverService, webDriverConfig);
    }

    public PooledWebDriverManager(WebDriverPoolConfig webDriverPoolConfig, WebDriverConfig webDriverConfig) throws IOException {
        init(webDriverConfig);
        this.webDriverPool = new WebDriverPool(this.driverService, webDriverPoolConfig, webDriverConfig);
    }

    public void init(WebDriverConfig webDriverConfig) throws IOException {
        this.driverService = WebDriverUtils.createDriverService(webDriverConfig);
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
    }

    /**
     * 从Pool取PooledObject
     */
    public RemoteWebDriver getRemoteWebDriver() {
        try {
            return webDriverPool.borrowObject();
        } catch (Exception e) {
            log.error("从Pool获取RemoteWebDriver失败[系统异常] ", e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }

    /**
     * 无效化PooledObject
     */
    private void invalidateWebDriver(RemoteWebDriver webDriver) {
        try {
            log.warn("无效化 webDriver");
            webDriverPool.invalidateObject(webDriver);
        } catch (Exception e) {
            log.error("无效化webDriver失败[系统异常] ", e);
            throw ErrorMessage.SYS_ERROR.getSystemException(e);
        }
    }

    /**
     * webDriver内执行
     *
     * @return fun的返回值
     * @throws Exception fun的异常 以及可能的IOException
     */
    public <T> T execute(WebDriverCallback<T> fun) throws Exception {
        RemoteWebDriver webDriver = getRemoteWebDriver();
        try {
            return fun.execute(webDriver);
        } catch (IOException e) {
            log.error("WebDriverCallback[执行失败]  ", e);
            //无效化
            invalidateWebDriver(webDriver);
            webDriver = null;
            throw e;
        } finally {
            //返回Pool
            if (webDriver != null) {
                webDriverPool.returnObject(webDriver);
            }
        }
    }

    /**
     * 退出时候结束进程 释放资源
     */
    public void exit() {
        this.webDriverPool.clear();
        WebDriverUtils.close(this.driverService);
    }
}
