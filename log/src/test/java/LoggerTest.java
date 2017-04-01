import com.kongzhong.finance.basic.log.Logger;
import com.kongzhong.finance.basic.log.LoggerFactory;
import org.junit.Test;

/**
 * Created by IFT8 on 2017/3/30.
 */
public class LoggerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);

    @Test
    public void error() {
        LOGGER.error("a", "b", null);
    }

    @Test
    public void info() {
        LOGGER.info("info");
    }

    @Test
    public void placeholder() {
        LOGGER.info("info={}","placeholder");
    }

    @Test
    public void exception() {
        LOGGER.error("e", new Exception("asdfsaasfasf"));
    }
}
