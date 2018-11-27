package me.ift8.basic.webdriver.pool;

import me.ift8.basic.webdriver.config.WebDriverConfig;
import me.ift8.basic.webdriver.util.WebDriverUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

/**
 * Created by IFT8 on 2017/5/19.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebDriverPooledObjectFactory implements PooledObjectFactory<RemoteWebDriver> {

    private static final String BLANK_URI = "about:blank";

    private WebDriverConfig webDriverConfig;
    private DriverService driverService;

    public WebDriverPooledObjectFactory(DriverService driverService, WebDriverConfig webDriverConfig) {
        this.webDriverConfig = webDriverConfig;
        this.driverService = driverService;
    }

    @Override
    public PooledObject<RemoteWebDriver> makeObject() {
        RemoteWebDriver webDriver = WebDriverUtils.createWebDriver(driverService, webDriverConfig);
        if (log.isDebugEnabled()) {
            log.debug("makeObject SessionId={}", webDriver.getSessionId().toString());
        }
        return new DefaultPooledObject<>(webDriver);
    }


    @Override
    public void destroyObject(PooledObject<RemoteWebDriver> pooledObject) {
        RemoteWebDriver webDriver = pooledObject.getObject();

        if (webDriver == null) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("destroyObject SessionId={}", webDriver.getSessionId().toString());
        }
        WebDriverUtils.close(webDriver);
    }

    @Override
    public boolean validateObject(PooledObject<RemoteWebDriver> pooledObject) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<RemoteWebDriver> pooledObject) {
    }

    @Override
    public void passivateObject(PooledObject<RemoteWebDriver> pooledObject) {
        RemoteWebDriver webDriver = pooledObject.getObject();
        if (log.isDebugEnabled()) {
            log.debug("passivateObject SessionId={}", webDriver.getSessionId().toString());
        }
        webDriver.get(BLANK_URI);
    }
}
