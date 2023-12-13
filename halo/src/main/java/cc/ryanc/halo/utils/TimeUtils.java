package cc.ryanc.halo.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author ZhangHui
 * @version 1.0
 * @className TimeUtils
 * @description 时间类的一些工具
 * @date 2019/12/25
 */
public class TimeUtils {

    /**
     * Date转LocalDateTime
     *
     * @param date
     * @return java.time.LocalDateTime
     * @author ZhangHui
     * @date 2019/12/25
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static Date localDateTimeToDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());

    }

    /**
     * 格式化LocalDateTime
     *
     * @param date
     * @return java.lang.String
     * @author ZhangHui
     * @date 2019/12/25
     */
    public static String formatLocalDateTime(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

    /**
     * 将格式化time转换成LocalDateTime
     *
     * @param time
     * @return java.time.LocalDateTime
     * @author ZhangHui
     * @date 2019/12/25
     */
    public static LocalDateTime parseLocalDateTime(String time) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, df);
    }


}
