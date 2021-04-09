package vip.itchen.support;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author alabimofa
 */
public class DateUtil {

    public static final String TIME_FULL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String YMD_FULL_FORMAT = "yyyy-MM-dd";

    public static final String YMD_SHORT_FORMAT = "yyyyMMdd";

    /**
     * 日期格式化 通用方法
     * @param date 日期
     * @param format 日期格式
     * @return 字符串日期
     */
    public static String getDateFormat(Date date ,String format) {
        return DateFormatUtils.format(date, format);
    }

    /**
     * 当前时间 格式化 yyyyMMdd
     * @return 格式化后的时间
     */
    public static String getCurrentYmd() {
        return getDateFormat(new Date(), YMD_SHORT_FORMAT);
    }

    /**
     * 字符串转换日期
     * @param ymdStr 格式："yyyyMMdd"
     * @return
     */
    public static Date parseYmd(String ymdStr) {
        try {
            return DateUtils.parseDate(ymdStr, YMD_SHORT_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 字符串转换日期时间
     * @param strDatetime 格式："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static Date parseYmdHms(String strDatetime) {
        try {
            return DateUtils.parseDate(strDatetime, TIME_FULL_FORMAT);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 格式化日期 切换为新格式的日期
     * @param dateStr 格式化日期
     * @param format 格式
     * @param afterFormat 新的格式
     * @return 新格式的日期
     */
    public static String formatChange(String dateStr, String format, String afterFormat) {
        try {
            Date date = DateUtils.parseDate(dateStr, format);
            return getDateFormat(date, afterFormat);
        } catch (ParseException e) {
            return "";
        }
    }

    /**
     * 获取某天的开始时间，即 "00:00:00"
     *
     * @param date 时间
     * @return 时间
     */
    public static Date getStartOfDay(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某天的结束时间，即 "23:59:59"
     *
     * @param date 时间
     * @return 时间
     */
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDate = localDate.withHour(23).withMinute(59).withSecond(59);
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取年份
     * @param date 日期
     * @return 年
     */
    public static Integer getYear(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    /**
     * 为时间添加天数
     *
     * @param date 时间
     * @param day  天数
     * @return 时间
     */
    public static Date plusDay(Date date, Long day) {
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDate = localDate.plusDays(day);
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 为时间添加分钟
     *
     * @param date    时间
     * @param minutes 分钟数
     * @return 时间
     */
    public static Date plusMinutes(Date date, Long minutes) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusMinutes(minutes);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 为时间添加小时
     *
     * @param date  时间
     * @param hours 小时数
     * @return 时间
     */
    public static Date plusHours(Date date, Long hours) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusHours(hours);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 月份增减
     *
     * @param date  时间
     * @param month 月数
     * @return 时间
     */
    public static Date plusMonth(Date date, Integer month) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.plusMonths(month);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取两个日期间间隔的天数 （第一个日期小于第二个日期）
     *
     * @param first  第一个日期
     * @param second 第二个日期
     * @return 天数
     */
    public static Integer betweenDay(Date first, Date second) {
        LocalDate firstL = first.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate secondL = second.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long between = secondL.toEpochDay() - firstL.toEpochDay();
        return (int) between;
    }

    /**
     * 获取两个日期几个的秒数
     *
     * @param first  第一个时间
     * @param second 第二个时间
     * @return 秒数
     */
    public static Long betweenSeconds(Date first, Date second) {
        Instant firstI = first.toInstant();
        Instant secondI = second.toInstant();
        return Duration.between(firstI, secondI).getSeconds();
    }

    /**
     * 计算两个日期相差多少秒
     *
     * @param lateDate   晚一点的时间
     * @param beforeDate 早一点的时间
     */
    public static int calLastedTime(Date beforeDate, Date lateDate) {
        long late = lateDate.getTime();
        long before = beforeDate.getTime();
        int c = (int) ((late - before) / 1000);
        return c;
    }

    /**
     * 计算当前月最多有多少天
     *
     * @param date 时间
     * @return 天数
     */
    public static Integer maxDaysOfMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return localDate.getDayOfMonth();
    }

    /**
     * 获取一个月的第一天
     *
     * @param date 日期
     * @return 日期
     */
    public static Date firstDayOfMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.with(TemporalAdjusters.firstDayOfMonth());
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 两个日期相差多少分钟
     * @param date From日期
     * @param date2 To日期
     * @return 分钟数
     */
    public static int getMinDiffer(Date date ,Date date2){
        long from = date.getTime();
        long to = date2.getTime();
        return  (int) ((to - from)/(1000 * 60));
    }

    /**
     * 为时间添加秒
     *
     * @param date 时间
     * @param seconds 秒
     * @return 时间
     */
    public static Date plusSeconds(Date date, long seconds) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusSeconds(seconds);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void main(String[] args) {

    }

}
