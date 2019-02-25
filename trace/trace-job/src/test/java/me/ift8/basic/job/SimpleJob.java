package me.ift8.basic.job;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Slf4j
public class SimpleJob extends AbstractSimpleJob {

    @Override
    public void doWork() throws Exception {
        log.info("doWork");
    }
}
