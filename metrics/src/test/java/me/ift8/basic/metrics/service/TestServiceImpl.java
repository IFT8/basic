package me.ift8.basic.metrics.service;

import me.ift8.basic.metrics.Metrics;
import me.ift8.basic.metrics.api.TestService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Created by IFT8 on 2017/5/31.
 */
@Service
public class TestServiceImpl implements TestService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TestServiceImpl.class);

    public String apiAspect(String param) {
        log.info(param);
        return "apiAspect ret:" + param;
    }

    @Metrics(name = "test_aspect", logInput = true)
    public String aspect(String param) {
        log.info(param);
        return "aspect ret:" + param;
    }
}
