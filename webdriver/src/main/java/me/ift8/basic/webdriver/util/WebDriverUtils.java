package me.ift8.basic.webdriver.util;

import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.utils.UUIDUtils;
import me.ift8.basic.webdriver.config.WebDriverConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2018/1/31.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebDriverUtils {
    public static final String FILE_URL_PROTOCOL = "file://";


    /**
     * 查找元素忽略异常
     *
     * @return 找不到返回null
     */
    public static WebElement findElementWithoutThrowable(WebDriver driver, By selector) {
        try {
            return driver.findElement(selector);
        } catch (NoSuchElementException ignore) {
        }
        return null;
    }

    /**
     * 截图并保存
     *
     * @param webDriver           webDriver
     * @param bizTag              业务标识(追加在文件名)
     * @param parentDirectoryPath 存放的目录路径
     */
    public static void saveScreenshot(RemoteWebDriver webDriver, String bizTag, String parentDirectoryPath) {
        if (webDriver == null) {
            log.warn("webDriver == null");
            return;
        }

        File screenshotAs = webDriver.getScreenshotAs(OutputType.FILE);

        LocalDateTime now = LocalDateTime.now();
        String date = now.toLocalDate().toString();
        String time = now.getHour() + "";

        String path = parentDirectoryPath + "/" + date + "/" + time + "/" + UUID.randomUUID().toString() + "_" + bizTag + ".jpg";

        log.info("saveScreenshot url={} path={} page={}", webDriver.getCurrentUrl(), path, webDriver.getPageSource());
        try {
            FileUtils.copyFile(screenshotAs, new File(path));
        } catch (IOException e) {
            log.error("saveScreenshot[异常]", e);
        }
    }

    /**
     * 保存html并返回文件Url
     */
    public static String saveAndGetFileUrl(String html, String parentDirectoryPath) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        String localDate = now.toLocalDate().toString();
        String uuid = UUIDUtils.generateUUID();

        //一定要html结尾 不然有些WebDriver(PhantomJSDriver)识别不了
        String filePath = parentDirectoryPath + "/" + localDate + "/" + uuid + ".html";

        File file = new File(filePath);
        FileUtils.writeStringToFile(file, html, Charset.forName("UTF-8"));

        return FILE_URL_PROTOCOL + file.getAbsolutePath();
    }

    /**
     * 解析文件Url为文件路径
     *
     * @param fileUrl e.g file:///data/xx
     * @return e.g /data/xx
     */
    public static String parseFileUrl2AbsolutePath(String fileUrl) {
        if (!fileUrl.startsWith(FILE_URL_PROTOCOL)) {
            throw ErrorMessage.MISSING_PARAM.getSystemException("不支持的协议格式");
        }
        return fileUrl.substring(FILE_URL_PROTOCOL.length());
    }

    public static DriverService createDriverService(WebDriverConfig webDriverConfig) throws IOException {
        File driverFile = new File(webDriverConfig.getDriverPath());

        DriverService service = new ChromeDriverService.Builder()
                .usingDriverExecutable(driverFile)
                .usingAnyFreePort()
                .build();

        service.start();
        return service;
    }

    public static RemoteWebDriver createWebDriver(WebDriverConfig webDriverConfig) {
        RemoteWebDriver driver;

        DesiredCapabilities caps = makeDesiredCapabilities(webDriverConfig);

        driver = new ChromeDriver(caps);

        if (webDriverConfig.getScriptTimeoutSecond() != null) {
            driver.manage().timeouts().setScriptTimeout(webDriverConfig.getScriptTimeoutSecond(), TimeUnit.SECONDS);
        }

        if (webDriverConfig.getScriptTimeoutSecond() != null) {
            driver.manage().timeouts().pageLoadTimeout(webDriverConfig.getScriptTimeoutSecond(), TimeUnit.SECONDS);
        }

        return driver;
    }

    public static RemoteWebDriver createWebDriver(DriverService driverService, WebDriverConfig webDriverConfig) {
        RemoteWebDriver driver;

        DesiredCapabilities caps = makeDesiredCapabilities(webDriverConfig);

        driver = new RemoteWebDriver(driverService.getUrl(), caps);

        if (webDriverConfig.getScriptTimeoutSecond() != null) {
            driver.manage().timeouts().setScriptTimeout(webDriverConfig.getScriptTimeoutSecond(), TimeUnit.SECONDS);
        }

        if (webDriverConfig.getScriptTimeoutSecond() != null) {
            driver.manage().timeouts().pageLoadTimeout(webDriverConfig.getScriptTimeoutSecond(), TimeUnit.SECONDS);
        }
        return driver;
    }

    /**
     * 根据Configs生成配置
     */
    private static DesiredCapabilities makeDesiredCapabilities(WebDriverConfig webDriverConfig) {
        DesiredCapabilities caps = DesiredCapabilities.chrome();

        ChromeOptions options = new ChromeOptions();
        // ignore certificate errors
        options.addArguments("test-type");
        // 默认 headless mode
        if (!Boolean.FALSE.equals(webDriverConfig.getIsHeadless())) {
            options.addArguments("headless");
        }
        options.addArguments("disable-gpu");
        //默认IPhone5
        if (!Boolean.FALSE.equals(webDriverConfig.getIsMoble())) {
            options.addArguments("--user-agent=Apple Iphone 5");
        }

        //禁止下载加载图片
        if (!Boolean.TRUE.equals(webDriverConfig.getDisplayImage())) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            options.setExperimentalOption("prefs", prefs);
        }

        caps.setCapability(ChromeOptions.CAPABILITY, options);
        return caps;
    }


    public static void close(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                log.warn("WebDriver quit exception:", e);
            }
            driver = null;
        }
    }

    public static void close(DriverService driverService) {
        if (driverService != null) {
            try {
                driverService.stop();
            } catch (Exception e) {
                log.warn("DriverService stop exception:", e);
            }
            driverService = null;
        }
    }
}
