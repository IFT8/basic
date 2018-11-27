import com.google.common.collect.Lists;
import me.ift8.basic.exception.ServiceException;
import me.ift8.basic.redis.lock.RedisLock;
import me.ift8.basic.redis.lock.RedisReturnLock;
import me.ift8.basic.task.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by IFT8 on 2017/5/8.
 */
@Slf4j
public class RedisLockTest {

    private StringRedisTemplate redisTemplate = redisTemplate();

    StringRedisTemplate redisTemplate() {
        return new StringRedisTemplate(jedisConnectionFactory());
    }

    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }


    private RedissonClient redissonClient = Redisson.create();

    @Test
    public void lock_case01() throws InterruptedException {

        List<FutureTask<Boolean>> taskList = Lists.newArrayList();

        for (int i = 0; i < 100; i++) {
            FutureTask<Boolean> futureTask = TaskManager.addFutureTask(this::doInLock);
            taskList.add(futureTask);
        }

        while (true) {
            if (taskList.stream().filter(FutureTask::isDone).count() == taskList.size()) {
                log.info("done");
                return;
            }
            Thread.sleep(100L);
        }
    }

    private boolean doInLock() {
        RedisLock redisLock = new RedisLock(redisTemplate, "key_adghf");

        StopWatch stopWatch = new StopWatch();
        try {
            redisLock.doInLock(() -> {
                stopWatch.start();

                long start = System.currentTimeMillis();

                log.info("doInLock start={}", start);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopWatch.stop();
                log.info("doInLock finish start={} {}", start, stopWatch.prettyPrint());

            });
        } catch (ServiceException e) {
            log.error("doInLock ", e);
        }

        return true;
    }


    private boolean redissonDoInLock() {
        StopWatch stopWatch = new StopWatch();
        RLock lock = redissonClient.getLock("anyLock");
        try {
            // 3. 尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (res) {    //成功
                // do your business
                stopWatch.start();

                long start = System.currentTimeMillis();

                log.info("doInLock start={}", start);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopWatch.stop();
                log.info("doInLock finish start={} {}", start, stopWatch.prettyPrint());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }

    @Test
    public void lock_case02() throws InterruptedException {
        List<FutureTask<Boolean>> taskList = Lists.newArrayList();

        for (int i = 0; i < 100; i++) {
            FutureTask<Boolean> futureTask = TaskManager.addFutureTask(this::redissonDoInLock);
            taskList.add(futureTask);
        }

        while (true) {
            if (taskList.stream().filter(FutureTask::isDone).count() == taskList.size()) {
                log.info("done");
                return;
            }
            Thread.sleep(100L);
        }
    }

    @Test
    public void lock_case03() throws InterruptedException {
        List<FutureTask<Boolean>> taskList = Lists.newArrayList();
        RedissonClient redissonClient = Redisson.create();

        RedisReturnLock redissonLock = new RedisReturnLock(redissonClient, "ke1y_adgh1f");

        for (int i = 0; i < 50; i++) {
            FutureTask<Boolean> futureTask = TaskManager.addFutureTask(() -> {
                StopWatch stopWatch = new StopWatch();

                redissonLock.doInLock(() -> {
                    stopWatch.start();

                    long start = System.currentTimeMillis();

                    log.info("doInLock start={}", start);
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    stopWatch.stop();
                    log.info("doInLock finish start={} {}", start, stopWatch.prettyPrint());

                });

                return true;
            });
            taskList.add(futureTask);
        }

        while (true) {
            if (taskList.stream().filter(FutureTask::isDone).count() == taskList.size()) {
                log.info("done");
                return;
            }
            Thread.sleep(100L);
        }
    }

    @Test
    public void aaa() throws ServiceException {
        RedissonClient redissonClient = Redisson.create();

        RedisReturnLock redissonLock = new RedisReturnLock(redissonClient, "ke1y_adgh1f");

        redissonLock.doInLock(()->{
            log.info("redissonLock 1");

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            RedisReturnLock redissonLock2 = new RedisReturnLock(redissonClient, "ke1y_adgh1f");

            Integer integer = redissonLock2.doInLock(() -> {

                log.info("redissonLock 2");

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 1;
            });

            System.out.println(integer);

        });

    }
}
