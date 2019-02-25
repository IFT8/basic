package me.ift8.basic.job;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by IFT8 on 2019-02-13.
 */
@Slf4j
public abstract class AbstractSimpleJob {

    public void execute(Object shardingContext) throws Exception {
        log.info("AbstractSimpleJob.execute");
        doWork();
    }

    public abstract void doWork() throws Exception;
}
