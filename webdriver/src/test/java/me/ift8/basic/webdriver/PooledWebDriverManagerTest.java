package me.ift8.basic.webdriver;

import me.ift8.basic.task.TaskManager;
import me.ift8.basic.webdriver.config.WebDriverConfig;
import me.ift8.basic.webdriver.pool.WebDriverPoolConfig;
import me.ift8.basic.webdriver.util.WebDriverUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.util.StopWatch;

import java.io.IOException;

/**
 * Created by IFT8 on 2018/1/31.
 */
@Slf4j
public class PooledWebDriverManagerTest {

    private WebDriverConfig webDriverConfig() {
        WebDriverConfig webDriverConfig = new WebDriverConfig();
        webDriverConfig.setIsHeadless(true);
        webDriverConfig.setIsMoble(true);
        webDriverConfig.setDriverPath("/usr/local/bin/chromedriver");
        webDriverConfig.setPageLoadTimeoutSecond(30);
        webDriverConfig.setScriptTimeoutSecond(30);
        return webDriverConfig;
    }

    private WebDriverPoolConfig webDriverPoolConfig() {
        WebDriverPoolConfig webDriverPoolConfig = new WebDriverPoolConfig();
        webDriverPoolConfig.setMaxTotal(16);
        webDriverPoolConfig.setMaxIdle(16);
        return webDriverPoolConfig;
    }

    @Test
    public void create() {
        RemoteWebDriver remoteWebDriver = null;
        try {
            remoteWebDriver = WebDriverUtils.createWebDriver(webDriverConfig());

            remoteWebDriver.get("http://www.baidu.com");
        } finally {
            WebDriverUtils.close(remoteWebDriver);
        }

    }

    @Test
    public void getRemoteWebDriver() throws IOException {
        PooledWebDriverManager pooledWebDriverManager = new PooledWebDriverManager(webDriverConfig());
        RemoteWebDriver remoteWebDriver = pooledWebDriverManager.getRemoteWebDriver();

        remoteWebDriver.get("http://www.baidu.com");

    }

    @Test
    public void pressureDo1() throws InterruptedException, IOException {

        StopWatch start = new StopWatch("无池压测");
        start.start("总耗时");


        for (int i = 0; i < 100; i++) {
            int finalI = i;
            TaskManager.addTask(() -> {
                try {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start("准备WebDriver");
                    RemoteWebDriver driver = WebDriverUtils.createWebDriver(webDriverConfig());

                    stopWatch.stop();

                    stopWatch.start("准备页面");
                    driver.get("https://www.baidu.com/s?wd=" + finalI);
                    Thread.sleep(3000);
                    stopWatch.stop();

                    log.info("index: {} done {}", finalI, stopWatch.prettyPrint());

                    driver.quit();
                } catch (Exception e) {
                    log.error(" {} err", finalI, e);
                }
            });
        }

        TaskManager.shutdownAndAwait();
        start.stop();
        log.info("{}", start.prettyPrint());
    }


    @Test
    public void pressureDo2() throws InterruptedException, IOException {
        PooledWebDriverManager pooledWebDriverManager = new PooledWebDriverManager(webDriverPoolConfig(), webDriverConfig());

        StopWatch start = new StopWatch("池化压测");
        start.start("总耗时");

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            TaskManager.addTask(() -> {
                try {
                    StopWatch stopWatch = new StopWatch();
                    stopWatch.start("准备WebDriver");
                    pooledWebDriverManager.execute(driver -> {
                        stopWatch.stop();

                        stopWatch.start("准备页面");
                        driver.get("https://www.baidu.com/s?wd=" + finalI);
                        Thread.sleep(3000);
                        stopWatch.stop();

                        log.info("index: {} done {}", finalI, stopWatch.prettyPrint());

                        return finalI;
                    });
                } catch (Exception e) {
                    log.error(" {} err", finalI, e);
                }
            });
        }

        TaskManager.shutdownAndAwait();
        start.stop();
        log.info("{}", start.prettyPrint());
    }

    @Test
    public void execute() throws Exception {
        PooledWebDriverManager pooledWebDriverManager = new PooledWebDriverManager(webDriverConfig());

        Object result = pooledWebDriverManager.execute((webDriver) -> {
            webDriver.get("http://www.baidu.com");

            return webDriver.getPageSource();
        });

        log.info("{}", result);
    }
}