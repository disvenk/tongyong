package com.resto.shop.web.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by disvenk on 17/1/20.
 */
public class DateTimeUtils {
	private static final List<String> formarts = new ArrayList<String>(4);

	static {
		formarts.add("yyyy-MM");
		formarts.add("yyyy-MM-dd");
		formarts.add("yyyy-MM-dd HH:mm");
		formarts.add("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @Description:日期类型转换
	 * @param: [source]
	 * @return: java.util.Date
	 * @author: 王华
	 * @date: 2017-04-11 11:37:33
	 */
	public static Date convertToDate(String source) {
		if (source == null)
			return null;
		String value = source.trim();
		if ("".equals(value)) {
			return null;
		}
		try {
			if (source.matches("^\\d{4}-\\d{1,2}$")) {
				return parseDate(source, formarts.get(0));
			} else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
				return parseDate(source, formarts.get(1));
			} else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
				return parseDate(source, formarts.get(2));
			} else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
				return parseDate(source, formarts.get(3));
			} else {
				throw new IllegalArgumentException("Invalid date value '" + source + "'");
			}
		} catch (Throwable ex) {
			return null;
		}
	}

	/**
	 * @Description:类型转换
	 * @param: [source]
	 * @return: java.sql.Timestamp
	 * @author: 王华
	 * @date: 2017-04-11 11:36:37
	 */
	public static Timestamp convertToTimestamp(String source) {
		Timestamp timestamp = null;
		try {
			String value = source.trim();
			if ("".equals(value))
				return null;
			timestamp = new Timestamp(Long.parseLong(source));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timestamp;
	}


	/**
	 * @Description:获取指定日期的年、月、日等值
	 * @param: [date, field]  field值有：Calendar.YEAR、Calendar.MONTHd 等。
	 * @return: int
	 * @author: 王华
	 * @date: 2017-04-11 13:03:23
	 */
	public static int get(Date date, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//int year = calendar.get(Calendar.YEAR);
		//int month = calendar.get(Calendar.MONTH);
		return field == Calendar.MONTH ? (calendar.get(field) + 1) : calendar.get(field);
	}

	/**
	 * @Description:功能描述：格式化日期
	 * @param: [dateStr, format] 字符型日期,格式
	 * @return: java.util.Date
	 * @author: 王华
	 * @date: 2017-04-11 11:36:03
	 */
	public static Date parseDate(String dateStr, String format) throws ParseException {
		try {
			DateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * @param seconds Date或时间戳
	 * @param format  如：yyyy-MM-dd HH:mm:ss
	 * @Description:时间戳转换成日期格式字符串
	 * @return: java.lang.String
	 * @author: disvenk
	 * @date: 2017/4/15
	 * @time: 15:31
	 */
	public static String parseStr(Object seconds, String format) {
		if (seconds == null || seconds.toString().isEmpty() || seconds.equals("null")) {
			return "";
		}
		if(seconds instanceof Date) {
			Date date = (Date) seconds;
			seconds = date.getTime();
		}
		if (seconds.toString().length() == 10) seconds = seconds+"000";
		if (format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds.toString())));
	}
	public static String parseStr(Object seconds) {
		return parseStr(seconds, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * @Description:时间戳转日期，如果时间戳长度为10，则会先乘以1000
	 * @param: [timespan]
	 * @return: java.lang.String
	 * @author: 王华
	 * @date: 2017-04-11 11:35:36
	 */
	public static String toDateString(Long timespan) {
		if (timespan == null) return null;

		long l = timespan;
		if (timespan.toString().length() == 10)
			l = timespan * 1000;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		return sdf.format(new Date(l));
	}

	/**
	 * @Description:获取指定年月的第一天的 00:00:00,000，以时间戳格式返回
	 * @param: [year, month]
	 * @return: java.lang.Long
	 * @author: 王华
	 * @date: 2017-04-11 11:50:44
	 */
	public static Long getMonthFirstDay(int year, int month) {
		return getDayFirstDay(year, month, 1);
	}

	/**
	 * @Description: 获取指定年月日的开始时间 00:00:00,000，以时间戳格式返回
	 * @param: [year, month, day]
	 * @return: java.lang.Long
	 * @author: disvenk
	 * @date: 2017/4/18
	 */
	public static Long getDayFirstDay(int year, int month, int day) {
		month--;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis();
	}

	/**
	 * @Description: 获取指定年月的最后一天的 23:59:59,999，以时间戳格式返回
	 * @param: [year, month]
	 * @return: java.lang.Long
	 * @author: 王华
	 * @date: 2017-04-11 11:54:16
	 */
	public static Long getMonthLastDay(int year, int month) {
		month++;
		return getDayLastDay(year, month, 1);
	}

	/**
	 * @Description: 获取指定年月日的结束时间 23:59:59,999，以时间戳格式返回
	 * @param: [year, month, day]
	 * @return: java.lang.Long
	 * @author: disvenk
	 * @date: 2017/4/18
	 */
	public static Long getDayLastDay(int year, int month, int day) {
//        month++;
//        Date d = new Date(getDayFirstDay(year, month, day));
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(d);
//        calendar.set(Calendar.MILLISECOND, -1);
//        return calendar.getTimeInMillis();
		month--;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);
		return calendar.getTimeInMillis();
	}


	/**
	 * @Description: 获取指定天前或者天后的开始日期，格式：2016-01-01 00:00:00
	 * @param: [date, day]
	 * @return: java.util.Date
	 * @author: disvenk
	 * @date: 2017/4/12
	 * @time: 17:01
	 */
	public static Date getDateByAddDay(Date date, int day) {
		return getDateByAddDay(date, day, 0 ,0);
	}

	/**
	 * @Description: 获取指定年月日前或后的开始日期
	 * @Author: disvenk
	 * @Date: 2017/11/6 0006
	 */
	public static Date getDateByAddDay(Date date, int day, int month, int year) {
		//DateUtils.addDays(date,day);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.YEAR, year);//把日期往后增加一天.整数往后推,负数往前移动
		calendar.add(calendar.MONTH, month);//把日期往后增加一天.整数往后推,负数往前移动
		calendar.add(calendar.DATE, day);//把日期往后增加一天.整数往后推,负数往前移动
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


	/**
	 * @Description: 根据年份, 月份, 算出当月有多少天
	 * @param:
	 * @return:
	 * @author: wuwq
	 * @date: 2017-6-2
	 * @time: 20:58
	 */
	public static int getMaxDaysOfMonth(int year, int month) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.MONTH, month - 1);//Calendar对象默认一月为0
		int day = time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
		return day;
	}


	/**
	 * @Description: 根据年月日, 得到当天是星期几 (星期天返回7)
	 * @param:
	 * @return:
	 * @author: wuwq
	 * @date: 2017-6-2
	 * @time: 21:08
	 */
	public static int getDayOfWeek(int year, int month, int day) {
		Calendar time = Calendar.getInstance();
		time.clear();
		time.set(Calendar.YEAR, year);
		time.set(Calendar.MONTH, month - 1);
		time.set(Calendar.DAY_OF_MONTH, day - 1);
		int week_index = time.get(Calendar.DAY_OF_WEEK);
		return week_index;
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static String getWeekZOfDate(Date dt) {
		String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}


	public static String dateToString(Date date){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static void main(String[] args) throws ParseException {
		//System.out.println(getDateByAddDay(new Date(), 0));

//		int year = get(new Date(), Calendar.YEAR);
//		int month = get(new Date(), Calendar.MONTH);

		//long l = getMonthFirstDay(year,month);
//        long l = getMonthLastDay(year, month);
//        System.err.println(l);
//
//		SimpleDateFormat sdf = new SimpleDateFormat(formarts.get(2));
//		Date birth =convertToDate("1973-01-07");
		Date date = DateTimeUtils.parseDate("2017-7-7", "yyyy-MM-dd");
//		System.err.println(date.getTime()/1000);
//		System.err.println(parseStr(new Date().getTime()/1000, "yyMMdd"));
//

//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
//		StringBuilder s = new StringBuilder(sdf.format(new Date())).append(new Date().getTime()/1000);
//		System.out.println(new Date().getTime()/1000);
//		System.out.println((new Date().getTime()/1000+"").substring(2));
//		System.out.println(parseStr(new Date(), null));
//		System.out.println(getDateByAddDay(new Date(), 0).getTime());
//		System.out.println(getDateByAddDay(new Date(), -1).getTime());
		System.out.println(getDateByAddDay(new Date(), -30).getTime() / 1000);
		System.out.println(getDateByAddDay(new Date(), -365).getTime() / 1000);
		System.out.println(getDateByAddDay(new Date(), 0, -1, 0).getTime() / 1000);
		System.out.println(getDateByAddDay(new Date(), 0,0,-1).getTime() / 1000);
	}
}
