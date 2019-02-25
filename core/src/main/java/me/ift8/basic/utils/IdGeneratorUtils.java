package me.ift8.basic.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.UUID;

/**
 * 分布式全局唯一自增Id生成器，可用于订单号和业务主键生成。
 * Created by chenfeng on 2017/5/18.
 */
@Slf4j
public final class IdGeneratorUtils {
    private static final long workerId;//每台机器分配不同的id
    private static final long epoch = 1488877650000L;   // 时间起始标记点，作为基准，一般取系统的最近时间
    private static final long workerIdBits = 10L;      // 机器标识位数
    private static final long maxWorkerId = ~(-1L << workerIdBits);// 机器ID最大值: 1023
    private static long sequence = 0L;                   // 0，并发控制
    private static final long sequenceBits = 12L;      //毫秒内自增位

    private static final long workerIdShift = sequenceBits;                             // 12
    private static final long timestampLeftShift = sequenceBits + workerIdBits;// 22
    private static final long sequenceMask = ~(-1L << sequenceBits);                 // 4095,111111111111,12位
    private static long lastTimestamp = -1L;

    /*
     * 根据机器IP和进程Id生成workerId，分别取ip地址的二进制的后5位和进程Id的二进制后5位组合成10位。
     */
    static {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        Integer pid;
        try {
            pid = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get Process Id!");
        }
        log.info("pid: {}", pid);
        byte[] ipAddressByteArray = address.getAddress();
        workerId =  (long) ((ipAddressByteArray[ipAddressByteArray.length - 1] & 0B11111) << 5 | ((pid & 0B11111)));
        log.info("workerId: {}", workerId);
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
    }

    private static synchronized long generateIdInternal() {
        long timestamp = timeGen();
        if (lastTimestamp == timestamp) { // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环); 对新的timestamp，sequence从0开始
            sequence = sequence + 1 & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);// 重新生成timestamp
            }
        } else {
            sequence = 0;
        }

        if (timestamp < lastTimestamp) {
            UUID uuid = UUID.randomUUID();
            return uuid.getMostSignificantBits();
        }

        lastTimestamp = timestamp;
        return timestamp - epoch << timestampLeftShift | workerId << workerIdShift | sequence;
    }

    public static synchronized String generateId() {
        long id = generateIdInternal();
        String time = DateUtils.getDateYymmddFormatter().format(LocalDate.now());
        return String.format("%s%d", time, id);
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }
}
