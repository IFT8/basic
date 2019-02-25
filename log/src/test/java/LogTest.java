import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Created by IFT8 on 2019-01-16.
 */
@Slf4j
public class LogTest {
    @Test
    public void logInfo() {
        for (int i = 0; i < 100; i++) {
            log.info(i + " ");
        }
    }
}
