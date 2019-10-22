package com.resto.shop.web.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    public static final String DEF_PATTERN = "yyyy-MM-dd HH:mm:ss";//默认时间格式

    public static final String DATE_PATTERN = "yyyy-MM-dd";//日期格式

    public static final long MILLISECOND_OF_DAY = 86400000l;//一天的毫秒数

    //启始日期或 表示为 NULL 1970-01-01 08:00:00
    public static final Date START_NULL_DATE = parseDate("1970-01-01 08:00:00", DATE_PATTERN);


    /**
     * 方法说明：获得指定格式当前系统时间字符串
     *
     * @param pattern
     * @return
     * @throws Exception
     */
    public static String getCDateString(String pattern) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(new Date());
    }

    /**
     * 方法说明：获得指定格式当前系统时间字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateTime() {
        String result = "";
        try {
            result = getCDateString("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 方法说明：获得指定格式当前系统时间字符串 格式：yyyy-MM-dd HH:mm:ss
     *
     * @return
     * @throws Exception
     */
    public static String getCurrentDateTimeByPattern(String pattern) {
        String result = "";
        try {
            result = getCDateString(pattern);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static long getDatetime(String pattern, String dt) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.parse(dt).getTime();
    }

    public static String getDateForLong(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(c.getTime());
    }

    public static String getDateForLong(String pattern, long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(c.getTime());
    }

    public static int getTwoDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);

        return day1 - day2;
    }

    /**
     * 按指定格式格式化日期
     *
     * @param d
     * @param parttern
     * @return
     */
    public static String formatDateStr(Date d, String parttern) {
        SimpleDateFormat df = new SimpleDateFormat(parttern);
        return df.format(d);
    }

    /**
     * 默认格式时间转字符串
     *
     * @param d
     * @return
     */
    public static String defFormatDateStr(Date d) {
        return formatDateStr(d, DEF_PATTERN);
    }

    /**
     * 解析Date字符串为Date
     *
     * @param dataStr
     * @param pattern
     * @return
     * @throws Exception
     */
    public static Date parseDate(String dataStr, String pattern) {

        if (StringUtils.isEmpty(pattern)) {
            throw new IllegalArgumentException("parseDate pattern is not null");
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dataStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date buildByHMSE(Date time, int h, int m, int s, int ms) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
        calendar.set(Calendar.MILLISECOND, ms);
        return calendar.getTime();
    }

    public static Date getDateBeforeOrAfter(Date date, int pre) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, pre);
        return c.getTime();
    }

    public static Date getBeforOrAfterByHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, hour);
        return c.getTime();
    }

    public static Date getBeforOrAfterByMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 获取口岸申报时间段
     * 根据Diamond上配置的 起始时分秒 获取当前时间的 年月日-时分秒，并向后推24小时，算出结束时间的年月日-时分秒
     *
     * @param timeFm
     * @return 返回 起始时间 和 结束时间 数组
     */
    public static Date[] getPortTimeBw(Date nowTime, String timeFm) {
        String[] timeArray = timeFm.split(":");
        Date[] dates = new Date[2];
        //当前时间的截止时间
        Date nowfmTime = DateUtil.buildByHMSE(nowTime, Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]), Integer.valueOf(timeArray[2]), 0);
        if (nowTime.after(nowfmTime)) {
            dates[0] = nowfmTime;
            dates[1] = DateUtil.getDateBeforeOrAfter(dates[0], 1);
        } else {
            dates[1] = nowfmTime;
            dates[0] = DateUtil.getDateBeforeOrAfter(dates[1], -1);
        }
        return dates;
    }

    /**
     * 获取当前时间后一个小时的时间
     *
     * @return
     */
    public static Date getOneHoursAgoOnCurrentDate() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.HOUR_OF_DAY, 1);
        return cal.getTime();
    }


    /**
     * 两个时间的相差的天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDateBetween(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            int d1OfY = c1.get(Calendar.DAY_OF_YEAR);
            int d2OfY = c2.get(Calendar.DAY_OF_YEAR);
            return d2OfY - d1OfY;
        } else {
            c1.set(Calendar.HOUR_OF_DAY, 0);
            c1.set(Calendar.MINUTE, 0);
            c1.set(Calendar.SECOND, 0);
            c1.set(Calendar.MILLISECOND, 0);

            c2.set(Calendar.HOUR_OF_DAY, 0);
            c2.set(Calendar.MINUTE, 0);
            c2.set(Calendar.SECOND, 0);
            c2.set(Calendar.MILLISECOND, 0);
            return (c2.getTimeInMillis() - c1.getTimeInMillis()) / MILLISECOND_OF_DAY;
        }
    }

    public static long getDiffMinute(Date nowDate, Date endDate) {
        long nm = 1000 * 60;
        long diff = endDate.getTime() - nowDate.getTime();
        long min = diff / nm;
        return min;
    }

    /**
     * 查询当前时间是星期几
     */
    public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int day = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (day < 0) {
            day = 0;
        }
        return day;
    }

    /**
     * 查询当前时间是本年的第几周
     */
    public static int getWeekOfYear(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        if (week < 0) {
            week = 0;
        }
        return week;
    }

    public static void main(String[] args) {
        Date d1 = DateUtil.parseDate("2016-05-22 19:00:00", DateUtil.DEF_PATTERN);
        Date d2 = DateUtil.parseDate("2016-05-23 19:00:00", DateUtil.DEF_PATTERN);

        long diffMinute = getDiffMinute(d1, d2);

        System.out.println(diffMinute);
    }

    /**
     * 将日期格式的字符串转成另一个日期格式的字符串
     *
     * @return
     */
    public static String convertStringToDateString(String str, String fmFormat, String toFormat) {
        try {
            if (StringUtils.isEmpty(str) || StringUtils.isBlank(str)) {
                return null;
            }
            String time = str;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(fmFormat);
            // SimpleDateFormat的parse(String time)方法将String转换为Date
            Date date = simpleDateFormat.parse(time);
            simpleDateFormat = new SimpleDateFormat(toFormat);
            // SimpleDateFormat的format(Date date)方法将Date转换为String
            String formattedTime = simpleDateFormat.format(date);
            return formattedTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
