package me.ift8.basic.webdriver.pool;

import me.ift8.basic.utils.JsonUtils;
import me.ift8.basic.webdriver.config.WebDriverConfig;
import me.ift8.basic.webdriver.util.WebDriverUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
public class WebDriverPool extends GenericObjectPool<RemoteWebDriver> {

    public WebDriverPool(DriverService driverService, WebDriverConfig webDriverConfig) {
        super(new WebDriverPooledObjectFactory(driverService, webDriverConfig));

        log.info("WebDriverPool init param webDriverConfig:{}", webDriverConfig);
    }

    public WebDriverPool(DriverService driverService, WebDriverPoolConfig webDriverPoolConfig, WebDriverConfig webDriverConfig) {
        super(new WebDriverPooledObjectFactory(driverService, webDriverConfig), webDriverPoolConfig);

        log.info("WebDriverPool init param webDriverConfig:{} poolConfig:{}", webDriverConfig, JsonUtils.toJson(webDriverPoolConfig));
    }

    @Override
    public void invalidateObject(RemoteWebDriver webDriver) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("invalidateObject SessionId={}", webDriver.getSessionId().toString());
        }
        super.invalidateObject(webDriver);
        //释放资源
        WebDriverUtils.close(webDriver);
    }

    @Override
    public RemoteWebDriver borrowObject() throws Exception {
        RemoteWebDriver webDriver = super.borrowObject();
        if (log.isDebugEnabled()) {
            log.debug("borrowObject SessionId={}", webDriver.getSessionId().toString());
        }
        return webDriver;
    }

    @Override
    public void returnObject(RemoteWebDriver webDriver) {
        if (log.isDebugEnabled()) {
            log.debug("returnObject SessionId={}", webDriver.getSessionId().toString());
        }
        super.returnObject(webDriver);
    }
}
