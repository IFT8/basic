package me.ift8.basic.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by IFT8 on 2017/3/30.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd";
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final Date DATE0 = new Date(0);
    public static final LocalDateTime LOCAL_DATE_TIME0 = date2LocalDateTime(DATE0);

    /**
     * 年月日
     */
    @Getter
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD);
    @Getter
    private static final DateTimeFormatter dateYymmddFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYYMMDD);
    /**
     * 年月日时分秒
     */
    @Getter
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_YYYY_MM_DD_HH_MM_SS);

    /**
     * Date的0点0分0秒
     */
    public static LocalDateTime getStartOfDate(LocalDateTime date) {
        return date == null ? null : date.truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getStartOfDate(Date date) {
        return getStartOfDate(date2LocalDateTime(date));
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date localDate2Date(LocalDate localDate) {
        return localDate == null ? null : Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate date2LocalDate(Date date) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        return date == null ? null : LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * date2LocalDateTime(如果为Date0则返回null)
     * 数据库默认值'1970-01-01 08:00:00'
     */
    public static LocalDateTime date2LocalDateTimeWithDate0ToNull(Date date) {
        return DATE0.equals(date) || date == null ? null : date2LocalDateTime(date);
    }

    /**
     * date2LocalDateTime(如果为Date0则返回null)
     * 数据库默认值'1970-01-01 08:00:00'
     */
    public static Date getDateWithDate0ToNull(Date date) {
        return DATE0.equals(date) || date == null ? null : date;
    }
}
