package com.yxhl.platform.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 */
public class DateHelper extends DateUtils {

    /**
     * 日期格式
     */
    private static final String[] PARSE_PATTERNS = {
        "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
        "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
        "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    };

    /**
     * 日期格式
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 日期时间格式
     */
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 日期时间格式
     */
    private static final String DEFAULT_HHmmss_FORMAT = "HH:mm:ss";

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     *
     * @return the date
     */
    public static String getDate() {
        return getDate(DEFAULT_DATE_FORMAT);
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     *
     * @param pattern the pattern
     * @return the date
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, DEFAULT_DATE_FORMAT);
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     *
     * @param date the date
     * @return the string
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     *
     * @return the time
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }
    
    public static String getTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }
    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     *
     * @return the date time
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     *
     * @return the year
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     *
     * @return the month
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     *
     * @return the day
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }
    
    /**
     * 拼装年月日时分秒
     * @return
     */
    public static String getConcatYmdhms() {
        return formatDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     *
     * @return the week
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     *
     * @param str the str
     * @return the date
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date 对比日期
     * @return long long
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date 对比日期
     * @return long long
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date 对比日期
     * @return long long
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis 毫秒数
     * @return 天, 时:分:秒.毫秒
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = timeMillis / (60 * 60 * 1000) - day * 24;
        long min = timeMillis / (60 * 1000) - day * 24 * 60 - hour * 60;
        long s = timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        long sss = timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000;
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before 开始日期
     * @param after  结束日期
     * @return 天数 distance of two date
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (double) (1000 * 60 * 60 * 24);
    }

    /**
     * 获取东八区当前时间
     *
     * @return Date est 8 date
     */
    public static Date getEst8Date() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);
        dateFormat.setTimeZone(tz);
        return parseDate(dateFormat.format(date));
    }
    
    /**
     * 获得两个日期相隔月份数
     * @param before 较小的日期
     * @param after 较大的日期
     * @return 相差月份数
     */
	public static int getMonthSpace(String before, String after){
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(parseDate(before));
		c2.setTime(parseDate(after));
		result = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		int month = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12;
		return Math.abs(month + result);
	}
	
	/**
     * 获得两个日期相隔月份数
     * @param before 较小的日期
     * @param after 较大的日期
     * @return 相差月份数
     */
	public static int getMonthSpace(Date before, Date after){
		int result = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(before);
		c2.setTime(after);
		result = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
		int month = (c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR)) * 12;
		return Math.abs(month + result);
	}
	
	/**
	 * 获得今天日期，格式：yyyy-MM-dd
	 */
	public static Date getTodayDate() {
		return parseDate(formatDate(new Date(),"yyyy-MM-dd"));
	}
	
	/**
	 * 判断时间是否在时间段内
	 * @param time 要比较的日期
	 * @param beginTime 时间段开始日期
	 * @param endTime 时间段结束日期
	 * @return true：在时间段内；false：不在时间段
	 */
	public static boolean containDate(Date time, Date beginTime, Date endTime) {
	    Calendar date = Calendar.getInstance();
	    date.setTime(time);
	    Calendar begin = Calendar.getInstance();
	    begin.setTime(beginTime);
	    Calendar end = Calendar.getInstance();
	    end.setTime(endTime);
	    if (date.after(begin) && date.before(end)) {
	        return true;
	    }else if(time.compareTo(beginTime)==0 || time.compareTo(endTime) == 0 ){
	    	return true;
	    }else {
	        return false;
	    }
	}
	
	/**
	 * 获取两个时间段的交集
	 * @param srcBegin 时间段1的开始日期
	 * @param srcEnd 时间段1的结束日期
	 * @param targetBegin 时间段2的开始日期
	 * @param targetEnd 时间段2的结束日期
	 * @return
	 */
	public static Map<String, Date> getIntersectionBetDates(Date srcBegin, Date srcEnd, Date targetBegin,Date targetEnd) {
		Map<String, Date> map = new ConcurrentHashMap<String, Date>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long sb = Math.min(srcBegin.getTime(), srcEnd.getTime());
		long se = Math.max(srcBegin.getTime(), srcEnd.getTime());
		long tb = Math.min(targetBegin.getTime(), targetEnd.getTime());
		long te = Math.max(targetBegin.getTime(), targetEnd.getTime());
		
		long rs = 0;
		long re = 0;
		long[] temp = null;
		if(sb == tb && se != te) {
			temp = new long[] {sb,se,te};
			Arrays.sort(temp);
			rs = temp[0];
			re = temp[1];
		}else if(sb != tb && se == te) {
			temp = new long[] {sb,tb,se};
			Arrays.sort(temp);
			rs = temp[1];
			re = temp[2];
		}else if(sb == tb && se == te) {
			rs = sb;
			re = se;
		}else {
			temp = new long[] {sb,se,tb,te};
			Arrays.sort(temp);
			rs = temp[1];
			re = temp[2];
		}
		
		System.out.println("交集的开始时间是：" + sdf.format(rs) + "-结束时间是：" + sdf.format(re));
		map.put("start_date", DateHelper.parseDate(sdf.format(rs)));
		map.put("end_date", DateHelper.parseDate(sdf.format(re)));
		return map;
	}
	
	/**
	 * 相隔天数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1,Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) // 同一年
		{
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else // 不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);
		} else // 不同年
		{
			System.out.println("判断day2 - day1 : " + (day2 - day1));
			return day2 - day1;
		}
	}
	
	/**
	 * 返回输入月份第一天的日期
	 * 例：输入2018-09，返回2018-09-01
	 * @param monthStr 月份 yyyy-MM
	 * @return
	 */
	public static String getFirstDateOfMonth(String monthStr) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(monthStr));
		return formatDate(c.getTime(),DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 返回输入月份最后一天的日期
	 * 例：输入2018-09，返回2018-09-30
	 * @param monthStr 月份 yyyy-MM
	 * @return
	 */
	public static String getLastDateOfMonth(String monthStr) {
		Calendar c = Calendar.getInstance();
		c.setTime(parseDate(monthStr));
		c.roll(Calendar.MONDAY, 1);
		c.add(Calendar.DATE, -1); //减一天
		return formatDate(c.getTime(),DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 获得a分钟前 / a分钟后的时间
	 * @param a
	 * @return
	 * @throws ParseException
	 */
	public static String getOldOrNewTime(int a) throws ParseException {
		Calendar cal = Calendar.getInstance();
	    cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formatDateTime(new Date())));
	    cal.add(Calendar.MINUTE , a);
	    return formatDateTime(cal.getTime());
	}

	
	 public static int compareDate(String date1, String date2) {
     	DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
 
	public static void main(String[] args) throws ParseException {
		System.out.println(getConcatYmdhms());
		String rundate = "2018-10-08";
		String runTime = "17:46:00";
		System.out.println(getOldOrNewTime(11));
		System.out.println(compareDate(getOldOrNewTime(11), rundate+" "+runTime));
	}
}
