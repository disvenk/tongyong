package com.resto.brand.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	private final static SimpleDateFormat sdfDay = new SimpleDateFormat(
			"yyyy-MM-dd");
	
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat(
	"yyyyMMdd");

	private final static SimpleDateFormat sdfTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

    private final static int XUNBEGIN = 10;

    private final static int XUNMIDDLE = 20;

	/**
	 * 获取YYYY格式
	 * 
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD格式
	 * 
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * 
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * 
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author luguosui
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * 
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

    /**
     * 格式化日期
     *
     * @return
     */
    public static Date fomatDate(String date,String pattern) {
        DateFormat fmt = new SimpleDateFormat(pattern);
        try {
            return fmt.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 获取当前时间时间
     * @return
     */
    public Date time()
    {
        Date date=new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=format.format(date);
        return fomatDate(time,"yyyy-MM-dd HH:mm:ss");
    }

	/**
	 * 校验日期是否合法
	 * 
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	  /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Date beginDate = null;
        java.util.Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);

        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
        // java.util包
        Calendar canlendar = Calendar.getInstance();
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, daysInt);
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }

    /**
     * 得到n天之后的日期
     * 格式为""yyyy-MM-dd
     * @param days
     * @return
     */
    public static String getAfterDayDateStr(String days) {
        int daysInt = Integer.parseInt(days);
        // java.util包
        Calendar canlendar = Calendar.getInstance();
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, daysInt);
        Date date = canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdfd.format(date);

        return dateStr;
    }




    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(Date date,int days) {
        // java.util包
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTime(date);
        // 日期减 如果不够减会将月变动
        canlendar.add(Calendar.DATE, days);
        String[] weeks = new String[]	{"日","一","二","三","四","五","六"};
        int day = canlendar.get(Calendar.DAY_OF_WEEK);
        int week = canlendar.get(Calendar.WEEK_OF_YEAR);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int nowWeek = c.get(Calendar.WEEK_OF_YEAR);
        if(nowWeek<week){
        	return "下周"+weeks[day-1];
        }
        return "星期"+weeks[day-1];
    }

    public static String getAfterDayWeek(int day){
    	return getAfterDayWeek(new Date(), day);
    }
    
    /**
     * 将时间格式化为指定的字符串
     * @param date
     * @param pattern  格式化字符串    例如 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDate(Date date,String pattern){
    	SimpleDateFormat fmt = new SimpleDateFormat(pattern);
		if(date == null){
			return "";
		}
		return fmt.format(date);
    }
    
    


	public static Date getAfterDayDate(Date beginDate, int after) {
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		c.add(Calendar.DAY_OF_YEAR, after);
		return c.getTime();
	}

	public static Date getAfterMinDate(Date date, int min) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, min);
		return c.getTime();
	}

	public static Date getAfterMinDate(int min) {
		return getAfterMinDate(new Date(),min);
	}

	public static Date parseDate(String dataStr, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(dataStr);
	}

	public static Date getDateBegin(Date endDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(endDate);
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND,1);
		return c.getTime();
	}

	public static Date getDateEnd(Date beginDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(beginDate);
		c.set(Calendar.HOUR_OF_DAY,23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND,59);
		return c.getTime();
	}

	public static String getWeek(Date date) {
		return getAfterDayWeek(date,0);
	}

	/**
	 * 得到这周的第一天（默认为星期日）
	 * @param date
	 * @return
	 */
	public static Date getBeginDayWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
        	week_index = 0;
        }
        //根据 date 得到他 的星期天 的日期
        date = DateUtil.getAfterDayDate(date, -week_index);
        return date;
    }

	/**
	 * 获得某个时间 相对于当前的 分钟数
	 * @param time
	 * @return
	 */
	public static int getMinOfDay(Date time) {
		Calendar c = Calendar.getInstance();
		c.setTime(time);
		int hourMin = c.get(Calendar.HOUR_OF_DAY)*60;
		int min = c.get(Calendar.MINUTE);
		return hourMin+min;
	}
	
	/**
	 * 格式化时间，并且返回格式化后的当天开始时间
	 * 如何传入的值为空，则返回当天的开始时间
	 * @param beginDate 需要格式化的时间
	 * @return	返回的时间格式为 yyyy-MM-dd
	 */
	public static Date getformatBeginDate(String beginDate){
		Date date;
		if(beginDate==null || ("").equals(beginDate.trim())){
			date = getDateBegin(new Date());
		}else{
			date = getDateBegin(DateUtil.fomatDate(beginDate));
		}
		return date;
	}
	
	/**
	 * 格式化时间，并且返回格式化后的当天的结束时间
	 * 如何传入的值为空，则返回当天的结束时间
	 * @param endDate 需要格式化的时间
	 * @return	返回的时间格式为 yyyy-MM-dd
	 */
	public static Date getformatEndDate(String endDate){
		Date date;
		if(endDate==null || ("").equals(endDate.trim())){
			date = getDateEnd(new Date());
		}else{
			date = getDateEnd(DateUtil.fomatDate(endDate));
		}
		return date;
	}

    /**
     * 获取昨天的开始时间 格式为 “yyyy-MM-dd HH:mm:ss”
     * @return
     */
    public static String getYesterDayBegin(){

        //获取昨天的的日期
        Date date =  DateUtil.getAfterDayDate(new Date(),-1);
        Date begin = DateUtil.getDateBegin(date);

        //Date 转 String
       return  DateUtil.formatDate(begin,"yyyy-MM-dd HH:mm:ss");

    }

    /**
     * 获取昨天的开始时间 格式为 “yyyy-MM-dd HH:mm:ss”
     * @return
     */
    public static String getYesterDayEnd(){

        //获取昨天的的日期
        Date date =  DateUtil.getAfterDayDate(new Date(),-1);
        Date end = DateUtil.getDateEnd(date);

        //Date 转 String
        return  DateUtil.formatDate(end,"yyyy-MM-dd HH:mm:ss");

    }
    
    /**
     * 获取昨天的的日期
     * yyyy-MM-dd
     * @return
     */
    public static String getYesterDay(){
        Date date =  DateUtil.getAfterDayDate(new Date(),-1);
        return  DateUtil.formatDate(date,"yyyy-MM-dd");
    }

    public static List<Integer> getDayByYesterDay(Date createTime){
        //本月的开始时间 本月结束时间
        String beginMonth = DateUtil.getMonthBeginByYesterDay();
        //当月开始
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginMonth));
        //本日开始时间戳
        Long dayBefore = DateUtil.getDateBegin(DateUtil.getAfterDayDate(new Date(),-1)).getTime();
        //本日结束时间戳
        Long dayAfter = DateUtil.getDateEnd(DateUtil.getAfterDayDate(new Date(),-1)).getTime();

        //本月上旬开始 也就是本月的第一天开始时间戳
        Long firstBeginOfMonth = begin.getTime();
        //本月上旬的结束时间戳
        Long firstEndOfMonth = DateUtil.getDateEnd(DateUtil.getAfterDayDate(begin, 9)).getTime();
        //本月中旬开始时间 -- 上旬结束时间
        Long middleBeginOfMonth = DateUtil.getDateBegin(DateUtil.getAfterDayDate(begin, 10)).getTime();
        //本月中旬结束时间
        Long middelEndOfMonth = DateUtil.getDateEnd(DateUtil.getAfterDayDate(begin, 19)).getTime();
        //本月下旬开始时间  -- 也就是中旬结束时间
        Long lastBeginOfMonth = DateUtil.getDateBegin(DateUtil.getAfterDayDate(begin, 20)).getTime();
        //本月下旬结束时间 -- 如果当天发送数据有下旬那么结束时间就是当天
        Long lastEndOfMonth = dayAfter;
        List<Integer> list = new ArrayList<>();
        //本日之前
        if (createTime.getTime() < dayBefore) {
            list.add(1);
        }
        if (createTime.getTime() < dayAfter && createTime.getTime() > dayBefore) {
            //本日中
            list.add(2);
        }
        if (createTime.getTime() < firstBeginOfMonth) {
            //上旬之前
            list.add(3);
            //本月之前
            list.add(9);
        }
        if (createTime.getTime() > firstBeginOfMonth && createTime.getTime() < firstEndOfMonth) {
            //上旬中
            list.add(4);
        }
        if (createTime.getTime() < middleBeginOfMonth) {
            //中旬之前
            list.add(5);
        }
        if (createTime.getTime() > middleBeginOfMonth && createTime.getTime() < middelEndOfMonth) {
            //中旬中
            list.add(6);
        }
        if (createTime.getTime() < lastBeginOfMonth) {
            //下旬之前
            list.add(7);
        }
        if (createTime.getTime() > lastBeginOfMonth && createTime.getTime() < lastEndOfMonth) {
            list.add(8);
        }
        if (createTime.getTime() > firstBeginOfMonth && createTime.getTime() < dayAfter) {
            list.add(10);
        }
        return list;
    }

    /**
     * 获取当前月的第一天
     * @return
     */
    public  static  String getMonthBegin(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH,1);
        return format.format(c.getTime());
    }

    public static  String getMonthEnd(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return format.format(ca.getTime());
    }

    /**
     * 功能：返回上旬/中旬/下旬
     * 1 ：上旬  2： 中旬  3： 下旬
     * @param date
     * @return
     */
    public static int getEarlyMidLate(Date date) {
        int day=getDay(date);
        int earlyMidLate=0;
        if(1<=day && day<= 10){
            earlyMidLate=1;
        }
        if(11<=day && day<=20){
            earlyMidLate=2;
        }
        if(20<day){
            earlyMidLate=3;
        }
        return earlyMidLate;
    }

    /**
     * 功能：返回上旬/中旬/下旬
     * 判断当前日期是否是上，中，下旬的结束日期 比如 10号  20号 30号
     * 1 ：上旬  2： 中旬  3： 下旬 0.不是
     * @return
     */
    public static int getEarlyMidLateEnd(Date date) {
        int day=getDay(date);
        int earlyMidLate=0;
        if(day== XUNBEGIN){
            earlyMidLate=1;
        }
        if(day== XUNMIDDLE){
            earlyMidLate=2;
        }
        //判断传入的日期是否是最后一天
        if(getMonthEndDate(date)){
            earlyMidLate = 3;
        }
        return earlyMidLate;
    }

    public static Boolean getMonthEndDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(date);
        //设置这个月的第一天
        lastDate.set(Calendar.DATE, 1);
        //第 2 步 - 设置为加上 1 个月
        lastDate.add(Calendar.MONTH, 1);
        //第三步减 去一天
        lastDate.add(Calendar.DATE, -1);
        String format = sdf.format(lastDate.getTime());
        String str = DateUtil.formatDate(date,"yyyy-MM-dd");
        return format.equals(str);

    }




    /**
     * 功能：返回日
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }


    //
    /**
     * 获取昨日时间月的第一天
     * @return
     */
    public  static  String getMonthBeginByYesterDay(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //获取当前月第一天：
        Calendar calendar =Calendar.getInstance();
        calendar.clear();
        calendar.setTime(DateUtil.getAfterDayDate(new Date(),-1));
        calendar.set(Calendar.DATE, 1);
        System.out.println(format.format(calendar.getTime()));
        return format.format(calendar.getTime());
    }

    /**
     * 获取昨日中旬时间
     */

    public  static  String getMiddleOfMonthBeginByYesterDay(){
       return DateUtil.formatDate(DateUtil.getAfterDayDate(DateUtil.fomatDate(DateUtil.getMonthBeginByYesterDay()),10),"yyyy-MM-dd") ;
    }

    /**
     * 获取昨日下旬开时时间
     */

    public  static  String getEndOfMonthBeginByYesterDay(){
        return DateUtil.formatDate(DateUtil.getAfterDayDate(DateUtil.fomatDate(DateUtil.getMonthBeginByYesterDay()),20),"yyyy-MM-dd") ;
    }

    public  static  String getEndOfMonthEndByYesterDay(){
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
       // 设置时间,当前时间不用设置
       calendar.setTime(DateUtil.getAfterDayDate(new Date(),-22));
       // 设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

       // 打印
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

       return  format.format(calendar.getTime());
    }


    public  static Date getDateString(String text){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try{
            date = format.parse(text);
        }catch (Exception e){

        }
        SimpleDateFormat format02 = new SimpleDateFormat("yyyy-MM-dd");
       return fomatDate(format02.format(date));
    }

    /**
     * 根据当前的订单时间来判断 是属于哪个时间段(可能是多属于：是今日之前 也是 本月之前)
     * @param createTime
     * @return
     */
    public static List<Integer> getDayByToday(Date createTime){
        //本月的开始时间 本月结束时间
        String beginMonth = DateUtil.getMonthBegin();
        //当月开始
        Date begin = DateUtil.getDateBegin(DateUtil.fomatDate(beginMonth));
        //本日开始时间戳
        Long dayBefore = DateUtil.getDateBegin(new Date()).getTime();
        //本日结束时间戳
        Long dayAfter = DateUtil.getDateEnd(new Date()).getTime();

        //本月上旬开始 也就是本月的第一天开始时间戳
        Long firstBeginOfMonth = begin.getTime();
        //本月上旬的结束时间戳
        Long firstEndOfMonth = DateUtil.getDateEnd(DateUtil.getAfterDayDate(begin, 9)).getTime();
        //本月中旬开始时间 -- 上旬结束时间
        Long middleBeginOfMonth = DateUtil.getDateBegin(DateUtil.getAfterDayDate(begin, 10)).getTime();
        //本月中旬结束时间
        Long middelEndOfMonth = DateUtil.getDateEnd(DateUtil.getAfterDayDate(begin, 19)).getTime();
        //本月下旬开始时间  -- 也就是中旬结束时间
        Long lastBeginOfMonth = DateUtil.getDateBegin(DateUtil.getAfterDayDate(begin, 20)).getTime();
        //本月下旬结束时间 -- 如果当天发送数据有下旬那么结束时间就是当天
        Long lastEndOfMonth = dayAfter;
        List<Integer> list = new ArrayList<>();
         int xun = DateUtil.getEarlyMidLate(new Date());
         //本旬的开始时间
         Long xunBeginTime;
        //本旬的结束时间
        Long xunEndTime;

         //本旬的开始时间
         if(xun==1){
             //上旬
             xunBeginTime=firstBeginOfMonth;
             xunEndTime=firstEndOfMonth;
         }else if(xun==2){
             //中旬
             xunBeginTime=middleBeginOfMonth;
             xunEndTime=middelEndOfMonth;

         }else {
             //下旬
             xunBeginTime=lastBeginOfMonth;
             xunEndTime=lastEndOfMonth;
         }

        if (createTime.getTime() < dayBefore) {
            list.add(1);//本日之前
        }
        if (createTime.getTime() < dayAfter && createTime.getTime() > dayBefore) {
            list.add(2); //本日中
        }
        if (createTime.getTime() < firstBeginOfMonth) {
            list.add(3); //上旬之前
            list.add(9);//本月之前
        }
        if (createTime.getTime() > firstBeginOfMonth && createTime.getTime() < firstEndOfMonth) {
            list.add(4); //上旬中
        }
        if (createTime.getTime() < middleBeginOfMonth) {
            list.add(5);//中旬之前
        }
        if (createTime.getTime() > middleBeginOfMonth && createTime.getTime() < middelEndOfMonth) {
            list.add(6);//中旬中
        }
        if (createTime.getTime() < lastBeginOfMonth) {
            list.add(7);//下旬之前
        }
        if (createTime.getTime() > lastBeginOfMonth && createTime.getTime() < lastEndOfMonth) {
            list.add(8);//下旬中
        }
        if (createTime.getTime() > firstBeginOfMonth && createTime.getTime() < dayAfter) {
            list.add(10);//本月中
        }
        if(createTime.getTime()>xunBeginTime&&createTime.getTime()<xunEndTime){
             list.add(12);//本旬中
        }

        return list;
    }

    /*
    	获取当前时间 年月日+时分秒+四位随机数
     */
    public static String getRandomSerialNumber(){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdHHmmss");
    	String str = dateFormat.format(new Date());
		Random random = new Random();
		int num = (int)random.nextDouble()*(9999-1000+1)+1000;//获取4位随机数
    	return str+num;
	}


    /**
     * 获取某月的开始时间
     *
     * @param date 日期
     * @return {@link DateTime}
     */
    public static DateTime beginOfMonth(Date date) {
        Calendar calendar = calendar(date);
        calendar = beginOfMonth(calendar);
        return new DateTime(calendar);
    }

    /**
     * 获取某月的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return beginOfDay(calendar);
    }

    /**
     * 获取某天的开始时间
     *
     * @param calendar 日期 {@link Calendar}
     * @return {@link Calendar}
     */
    public static Calendar beginOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }


    /**
     * 转换为Calendar对象
     *
     * @param date 日期对象
     * @return Calendar对象
     */
    public static Calendar calendar(Date date) {
        return calendar(date.getTime());
    }

    /**
     * 转换为Calendar对象
     *
     * @param millis 时间戳
     * @return Calendar对象
     */
    public static Calendar calendar(long millis) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal;
    }

    /**
     * 判断两个日期是否是同一天
     *
     * pos结店 时候判断结店日期和当前日期是否一致
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }



    /**
     * 判断时间是否在时间段内
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     *
     *三个日期都是dateTime类型
     *
     */
    public static   boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        if (date.after(begin) && date.before(end)) {
            return true;
        }else if(nowTime.compareTo(beginTime)==0 || nowTime.compareTo(endTime) == 0 ){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断时间是否在时间段内
     * @param nowTime 比较的时间
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return
     *
     */
    public static   boolean compareDate(Date nowTime, Date beginTime, Date endTime) {
        Boolean flag = false;
        if(nowTime.after(beginTime)&&nowTime.before(endTime)){
            flag = true;
        }

        return flag;

    }

    public static String  getHourTme(){
        Calendar cal = Calendar.getInstance();
        //小时
        int hour=cal.get(Calendar.HOUR);
        //分
        int minute=cal.get(Calendar.MINUTE);
        //秒
        int second=cal.get(Calendar.SECOND);

        return hour+":"+minute+":"+second;
    }






}
