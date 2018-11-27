package me.ift8.basic;


import me.ift8.basic.constants.ErrorMessage;
import me.ift8.basic.exception.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by IFT8 on 2017/5/15.
 * tweeter的snowflake 移植到Java:
 * (a) id构成: 42位的时间前缀 + 10位的节点标识 + 12位的sequence避免并发的数字(12位不够用时强制得到新的时间前缀)
 * 注意这里进行了小改动: snowkflake是5位的datacenter加5位的机器id; 这里变成使用10位的机器id
 * (b) 对系统时间的依赖性非常强，需关闭ntp的时间同步功能。当检测到ntp时间调整后，将会拒绝分配id
 * 来源网络，做如下修改
 * 1. 各个依赖方自行指定 机器Id(10位 0~1023) && `时间标记点` && 去 `数据中心数据位`
 * 2. 业务需要 序列号 不依赖时间戳 循环自增(12位 0~4095)
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdWorker {
    //机器Id 必须设置
    private long workerId = -1L;
    // 时间起始标记点，作为基准，一般取系统的最近时间
    private long epoch = 1494929004000L;
    // 机器标识位数
    private static final long WORKER_ID_BITS = 10L;
    //机器ID最大值: 1023
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    //增序列号起始0
    private long sequence = 0L;
    //序列号自增位数
    private static final long SEQUENCE_BITS = 12L;
    //机器Id偏移量
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;                          // 12位
    //时间戳偏移量
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;    // 22位
    //序列号最大值
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);                  // 12位 111111111111 4095
    private long lastTimestamp = -1L;

    /**
     * 初始化
     *
     * @param workerId 机器Id
     * @param epoch    时间基数 写死
     */
    public IdWorker(long workerId, long epoch) {
        this.epoch = epoch;
        this.workerId = workerId;
        validate(workerId);
        if (log.isDebugEnabled()) {
            log.debug("IdWorker [init] workerId={} epoch={}", workerId, epoch);
        }
    }

    private void validate(long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw ErrorMessage.SYS_ERROR.getSystemException("workerId 不能小于0 且不能大于 " + MAX_WORKER_ID);
        }
    }

    private synchronized long nextId() {
        long timestamp = timeGen();
        //sequence自增 和sequenceMask相与一下，去掉高位循环
        this.sequence = (this.sequence + 1) & SEQUENCE_MASK;
        
        // 如果上一个timestamp与新产生的相等
        if (this.lastTimestamp == timestamp) {
            if (log.isDebugEnabled()) {
                log.debug("[sequence设置] 最后生成时间={}", lastTimestamp);
            }

            //判断是否溢出, 与sequenceMask相与，sequence就等于0
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);// 重新生成timestamp
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("sequence={}", this.sequence);
        }

        if (timestamp < this.lastTimestamp) {
            log.error("Id生成失败 [系统时钟异常] 最后生成时间={}", lastTimestamp);
            throw new SystemException("IDWORKER_TIMESTAMP_ERROR", "Id生成失败！！！");
        }

        this.lastTimestamp = timestamp;
        return ((timestamp - this.epoch) << TIMESTAMP_LEFT_SHIFT) //时间戳部分
                | (this.workerId << WORKER_ID_SHIFT)             //机器标识部分
                | this.sequence;                                    //序列号部分
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        if (log.isDebugEnabled()) {
            log.debug("[timestamp 设置] 最后生成时间={}", lastTimestamp);
        }

        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    public long generateId() {
        return this.nextId();
    }
}
