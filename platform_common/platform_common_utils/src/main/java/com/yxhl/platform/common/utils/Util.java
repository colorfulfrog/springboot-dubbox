package com.yxhl.platform.common.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 通用工具类 
 * author:tony
 */
public class Util { 

	// 默认日期格式
	private static final String DATE_FMT = "yyyy-MM-dd"; // 日期
	private static final String TIME_FMT = "HH:mm:ss"; // 时间
	private static final String[] IMG_TYPES = {"png", "jpg", "jpeg"};
	private static final String DATE_TIME_FMT = "yyyy-MM-dd HH:mm:ss"; // 日期时间

	// 验证的正则表达式
	private static final String REG_ALPHA = "^[a-zA-Z]+$";

	private static final String REG_ALPHANUM = "^[a-zA-Z0-9]+$";

	private static final String REG_NUMBER = "^\\d+$";

	private static final String REG_INTEGER = "^[-+]?[1-9]\\d*$|^0$/";

	private static final String REG_FLOAT = "[-\\+]?\\d+(\\.\\d+)?$";

	private static final String REG_PHONE = "^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$";

	//	private static final String REG_MOBILE = "^((\\+86)|(86))?(13)\\d{9}$";
	/**手机号正则*/
	private static final String REG_MOBILE = "^((\\+86)|(86))?(13|15|18|17)+\\d{9}$";

	private static final String REG_QQ = "^[1-9]\\d{4,10}$";

	private static final String REG_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

	private static final String REG_ZIP = "^[1-9]\\d{5}$";

	private static final String REG_IP = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	private static final String REG_URL = "^(http|https|ftp):\\/\\/(([A-Z0-9][A-Z0-9_-]*)(\\.[A-Z0-9][A-Z0-9_-]*)+)(:(\\d+))?\\/?/i";

	private static final String REG_CHINESE = "^[\\u0391-\\uFFE5]+$";

	/**车牌号正则*/
	private static final String REG_PLATE_NUM = "^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$";

	/**身份证号正则*/
	private static final String REG_CID_NUM = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";




	/** 可以用于判断Object,String,Map,Collection,String,Array是否为空 */
	public static boolean isNull(Object value) {
		if (value == null) {
			return true;
		} else if(value instanceof String){
			if(((String)value).trim().replaceAll("\\s", "").equals("")||value.equals("null")){
				return true;
			}
		}else if(value instanceof Collection) {
			if(((Collection)value).isEmpty()){
				return true;
			}
		} else if(value.getClass().isArray()) {
			if(Array.getLength(value) == 0){
				return true;
			}
		} else if(value instanceof Map) {
			if(((Map)value).isEmpty()){
				return true;
			}
		}else {
			return false;
		}
		return false;

	}

	public static boolean isNotNull(Object value) {
		return !isNull(value);
	}

	public static boolean isNull(Object value, Object... items){
		if (isNull(value) || isNull(items)) {
			return true;
		}
		for (Object item : items) {
			if (isNull(item)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAlpha(String value) {

		return Pattern.matches(REG_ALPHA, value);
	}

	public static boolean isAlphanum(String value) {

		return Pattern.matches(REG_ALPHANUM, value);
	}

	public static boolean isNumber(String value) {

		return Pattern.matches(REG_NUMBER, value);
	}
	public static boolean isweekend(Date d){
		Calendar ca=Calendar.getInstance();
		ca.setTime(d);
		int day = ca.get(Calendar.DAY_OF_WEEK); 
		if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){ 
			return true;
		}else{
			return false;
		}
	}
	public static Date getMondayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return c.getTime();
	}
	public static Date getFirdayOfThisWeek() {
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 5);
		return c.getTime();
	}


	public static boolean isInteger(String value) {

		return Pattern.matches(REG_INTEGER, value);
	}

	public static boolean isFloat(String value) {

		return Pattern.matches(REG_FLOAT, value);
	}

	public static boolean isPhone(String value) {

		return Pattern.matches(REG_PHONE, value);
	}

	public static boolean isMobile(String value) {

		return Pattern.matches(REG_MOBILE, value);
	}

	public static boolean isPlateNum(String value) {

		return Pattern.matches(REG_PLATE_NUM, value);
	}
	public static boolean isCIDNum(String value) {

		return Pattern.matches(REG_CID_NUM, value);
	}

	public static boolean isEmail(String value) {

		return Pattern.matches(REG_EMAIL, value);
	}

	public static boolean isQQ(String value) {

		return Pattern.matches(REG_QQ, value);
	}

	public static boolean isZip(String value) {

		return Pattern.matches(REG_ZIP, value);
	}

	public static boolean isIP(String value) {

		return Pattern.matches(REG_IP, value);
	}

	public static boolean isURL(String value) {

		return Pattern.matches(REG_URL, value);
	}

	public static boolean isChinese(String value) {

		return Pattern.matches(REG_CHINESE, value);
	}

	/** 验证是否为合法身份证 */
	public static boolean isIdcard(String value) {
		value = value.toUpperCase();
		if (!(Pattern.matches("^\\d{17}(\\d|X)$", value)||Pattern.matches("\\d{15}$", value))) {
			return false;
		}
		int provinceCode = Integer.parseInt(value.substring(0,2));
		if (provinceCode < 11 || provinceCode > 91) {
			return false;
		}
		return true;
	}

	public static boolean isDate(String value) {
		try {
			new SimpleDateFormat().parse(value);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 获取日期 */
	public static Date getCurrentDateTime() {

		return getCurrentDateTime(DATE_TIME_FMT);
	}

	public static Date getCurrentDate(){

		return getCurrentDate(DATE_FMT);
	}

	public static Date getCurrentTime(){

		return getCurrentTime(TIME_FMT);
	}

	public static Date getCurrentDateTime(String fmt) {

		return  dateStrToDate(fmt,getCurrentDateTimeStr(fmt));
	}

	public static Date getCurrentDate(String fmt){

		return  dateStrToDate(fmt,getCurrentDateStr(fmt));
	}
	public static int getCurrYear(){
		Calendar c=Calendar.getInstance();
		return c.get(Calendar.YEAR);//得到年
	}
	public static int getCurrMonth(){
		Calendar c=Calendar.getInstance();
		return c.get(Calendar.MONTH)+1;
	}
	public static int getCurrDay(){
		Calendar c=Calendar.getInstance();
		return c.get(Calendar.DATE);
	}
	public static Date getCurrentTime(String fmt){

		return  dateStrToDate(fmt,getCurrentTimeStr(fmt));
	}

	public static String getCurrentDateTimeStr() {

		return getCurrentDateTimeStr(DATE_TIME_FMT);
	}

	public static String getCurrentTimeStr() {

		return getCurrentTimeStr(TIME_FMT);
	}

	public static String getCurrentDateStr() {

		return getCurrentDateStr(DATE_FMT);
	}

	public static String getCurrentDateTimeStr(String fmt) {

		SimpleDateFormat format = new SimpleDateFormat(fmt);
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
		format.setTimeZone(timeZoneChina);

		String temp = format.format(new Date());

		return temp;
	}

	public static String getCurrentTimeStr(String fmt) {

		SimpleDateFormat format = new SimpleDateFormat(fmt);
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
		format.setTimeZone(timeZoneChina);

		String temp = format.format(new Date());

		return temp;
	}

	public static String getCurrentDateStr(String fmt) {

		SimpleDateFormat format = new SimpleDateFormat(fmt);
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
		format.setTimeZone(timeZoneChina);

		String temp = format.format(new Date());

		return temp;
	}
	public static Date getStartTime(String date){
		Date temp = null;
		try {
			//2013-08-01 00:00:00
			if(date.length()>=10){
				date=date.substring(0, 10)+" 00:00:00";
			}else {
				date=date+" 00:00:00";
			}
			temp = new SimpleDateFormat(DATE_TIME_FMT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}
	public static Date getEndTime(String date){
		Date temp = null;
		try {
			Calendar todayStart = Calendar.getInstance();
			temp = new SimpleDateFormat(DATE_FMT).parse(date);
			todayStart.setTime(temp);
			todayStart.set(Calendar.HOUR_OF_DAY, 23);  
			todayStart.set(Calendar.MINUTE, 59);  
			todayStart.set(Calendar.SECOND, 59);  
			todayStart.set(Calendar.MILLISECOND, 59);  
			return todayStart.getTime();  
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Date getStartTime(Date date){
		Calendar todayStart = Calendar.getInstance();  
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR, 0);  
		todayStart.set(Calendar.MINUTE, 0);  
		todayStart.set(Calendar.SECOND, 0);  
		todayStart.set(Calendar.MILLISECOND, 0);  
		return todayStart.getTime();  
	}
	public static Date getEndTime(Date date){
		Calendar todayStart = Calendar.getInstance();  
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR_OF_DAY, 23);  
		todayStart.set(Calendar.MINUTE, 59);  
		todayStart.set(Calendar.SECOND, 59);  
		todayStart.set(Calendar.MILLISECOND, 57);  
		return todayStart.getTime();
	}
	public static Date getAddHourse(Date date,int num){
		Calendar todayStart = Calendar.getInstance();  
		todayStart.setTime(date);
		todayStart.set(Calendar.HOUR_OF_DAY,todayStart.get(Calendar.HOUR_OF_DAY )+num );  
		return todayStart.getTime();
	}
	public static String dateToDateStr(Date date) {

		String temp = new SimpleDateFormat(DATE_TIME_FMT).format(date);

		return temp;
	}

	public static String dateToDateStr(String fmt, Date date) {

		String temp = new SimpleDateFormat(fmt).format(date);

		return temp;
	}

	/** 转换为日期对象 */
	public static Date dateStrToDate(String date) {
		Date temp = null;
		try {
			temp = new SimpleDateFormat(DATE_FMT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}
	public static Date dateTimeStrToDateTime(String date) {
		Date temp = null;
		try {
			temp = new SimpleDateFormat(DATE_TIME_FMT).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}
	public static int dayForHour(Date  pTime)  {  
		Calendar cal = new GregorianCalendar();  
		cal.setTime(pTime);  
		return cal.get(Calendar.HOUR_OF_DAY);  
	} 
	public static int dayForWeek(String pTime)  {  
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
		Date tmpDate=null;
		try {
			tmpDate = format.parse(pTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		Calendar cal = new GregorianCalendar();  
		cal.setTime(tmpDate);  
		return cal.get(Calendar.DAY_OF_WEEK);  
	} 
	public static int dayForWeek(Date date)  {  
		Calendar cal = new GregorianCalendar();  
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);  
	}  
	public static Date dateStrToDate(String fmt, String date) {
		Date temp = null;
		try {
			temp = new SimpleDateFormat(fmt).parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/** 格式化日期 */
	public static String formatDateTime(Date date) {

		return formatDateTime(DATE_TIME_FMT,date);
	}

	public static String formatDateTime(String fmt, Date date) {
		if (isNull(fmt) || isNull(date)) {
			return null;
		}
		String temp = new SimpleDateFormat(fmt).format(date);

		return temp;
	}

	public static String formatTime(Date date){
		return formatTime(TIME_FMT,date);
	}

	public static String formatTime(String fmt, Date date) {
		if (isNull(fmt) || isNull(date)) {
			return null;
		}
		String temp = new SimpleDateFormat(fmt).format(date);

		return temp;
	}

	public static String formatDate(Date date){
		return formatDate(DATE_FMT,date);
	}
	public static String formatDate(String fmt, Date date) {
		if (isNull(fmt) || isNull(date)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat(fmt);
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");
		format.setTimeZone(timeZoneChina);

		String temp = format.format(new Date());

		return temp;
	}

	public static String formatNumber(String fmt, Object value) {
		if (isNull(fmt) || isNull(value)) {
			return null;
		}
		String temp = new DecimalFormat(fmt).format(value);

		return temp;
	}

	/**交换两个日期 */
	public static void changeDate(Date date1,Date date2){
		if (isNull(date1,date2)) {
			return;
		}
		//判断两个时间的大小
		Calendar c1 =Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 =Calendar.getInstance();
		c2.setTime(date2);
		if (c1.after(c2)) {
			date1 = c2.getTime();
			date2 = c1.getTime();
		}
	}

	/** 比较两个日期相差的年数 */
	public static int compareYear(Date date1,Date date2){
		if (isNull(date1)||isNull(date2)) {
			return 0;
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		if (c1.equals(c2)) {
			return 0;
		}

		if (c1.after(c2)) {
			Calendar temp = c1;
			c1 = c2;
			c2 = temp;
		}
		//计算差值
		int result = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
		return result;
	}

	/** 比较两个日期相差的天数 */
	public static int compareDay(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		Calendar d1 = Calendar.getInstance();
		d1.setTime(date1);
		Calendar d2 = Calendar.getInstance();
		d2.setTime(date2);
		if (d1.after(d2)) {
			java.util.Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(java.util.Calendar.DAY_OF_YEAR) - d1.get(java.util.Calendar.DAY_OF_YEAR);
		int y2 = d2.get(java.util.Calendar.YEAR);
		if (d1.get(java.util.Calendar.YEAR) != y2) {
			d1 = (java.util.Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(java.util.Calendar.DAY_OF_YEAR);
				d1.add(java.util.Calendar.YEAR, 1);
			} while (d1.get(java.util.Calendar.YEAR) != y2);
		}
		return days;

	}

	/** 比较两个日期相差的月数 */
	public static int compareMonth(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		int iMonth = 0;
		int flag = 0;
		try {
			Calendar objCalendarDate1 = Calendar.getInstance();
			objCalendarDate1.setTime(date1);

			Calendar objCalendarDate2 = Calendar.getInstance();
			objCalendarDate2.setTime(date2);

			if (objCalendarDate2.equals(objCalendarDate1))
				return 0;
			if (objCalendarDate1.after(objCalendarDate2)) {
				Calendar temp = objCalendarDate1;
				objCalendarDate1 = objCalendarDate2;
				objCalendarDate2 = temp;
			}

			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))
				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR))* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)- objCalendarDate1.get(Calendar.MONTH);
			else
				iMonth = objCalendarDate2.get(Calendar.MONTH)- objCalendarDate1.get(Calendar.MONTH) - flag;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iMonth;
	}
	public static String getCurrdatetimeYYYYMMDD(){
		return formatDate("yyyyMMdd",new Date());
	}
	/** 比较两个日期相差的秒数 */
	public static long compareTime(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		Calendar c = Calendar.getInstance();

		c.setTime(date1);
		long l1 = c.getTimeInMillis();

		c.setTime(date2);
		long l2 = c.getTimeInMillis();

		return (l2 - l1) / 1000;
	}
	/** 比较两个日期相差的分钟 */
	public static long compareFZ(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;

		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		long l1 = c.getTimeInMillis();
		c.setTime(date2);
		long l2 = c.getTimeInMillis();
		return (l2 - l1) / 1000/60;
	}
	/** 比较两个日期相差的分钟 */
	public static long compareFZ2(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return 0;
		
		Calendar c = Calendar.getInstance();
		c.setTime(date1);
		long l1 = c.getTimeInMillis();
		c.setTime(date2);
		long l2 = c.getTimeInMillis();
		return (l2 - l1) / 1000;
	}
	//设置时间
	private static Date addDateTime(Date date,int type,int num){
		if (date == null) {
			return null;
		}
		//初始化日历对象
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		//根据类型添加
		switch(type){
		case 1:		//添加年
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+num);
			break;
		case 2:		//添加月
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+num);
			break;
		case 3:		//添加日
			cal.set(Calendar.DATE, cal.get(Calendar.DATE)+num);
			break;
		case 4:		//添加时
			cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)+num);
			break;
		case 5:		//添加分
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE)+num);
			break;
		case 6:		//添加秒
			cal.set(Calendar.SECOND, cal.get(Calendar.SECOND)+num);
			break;
		}

		//返回操作结果
		return cal.getTime();
	}

	/** 添加年、月、日、时、分、秒 */
	public static Date addYear(Date date,int num){
		return addDateTime(date,1,num);
	}
	public static Date addMonth(Date date,int num){
		return addDateTime(date,2,num);
	}
	public static Date addDate(Date date,int num){
		return addDateTime(date,3,num);
	}
	/** 添加年、月、日 */
	public static Date addYMD(Date date,int year,int month,int day){

		return addYear(addMonth(addDate(date,day),month),year);
	}
	public static Date addHour(Date date,int num){
		return addDateTime(date,4,num);
	}
	public static Date addMinute(Date date,int num){
		return addDateTime(date,5,num);
	}
	public static Date addSecond(Date date,int num){
		return addDateTime(date,6,num);
	}
	/** 添加时、分、秒 */
	public static Date addHMS(Date date,int hour,int minute,int second){

		return addHour(addMinute(addSecond(date,second),minute),hour);
	}

	/** MD5加密 (32位)*/
	public static String md5(String value) {
		StringBuilder result = new StringBuilder();

		try {
			// 实例化MD5加载类
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			// 得到字节数据
			byte[] data = md5.digest(value.getBytes("UTF-8"));

			result.append(byte2hex(data));

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回结果
		return result.toString().toUpperCase();
	}
	public static String gencode(long id){
		id=id+1;
		DecimalFormat df=new DecimalFormat("000000");
		return df.format(id);
	}
	public static byte[] convertBASE64Decode(String data) {
		byte[] temp = null;

		BASE64Decoder decoder = new BASE64Decoder();

		try {
			temp = decoder.decodeBuffer(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return temp;
	}

	public static String convertBASE64Encode(byte[] data) {
		String temp = null;

		BASE64Encoder encoder = new BASE64Encoder();
		temp = encoder.encode(data);

		return temp;
	}

	public static String byte2hex(byte[] data) {

		StringBuilder result = new StringBuilder();

		for (byte b : data) {
			// 将二进制转换成字符串
			String temp = Integer.toHexString(b & 0XFF);
			// 追加加密后的内容
			if (temp.length() == 1) { // 判断字符长度
				result.append("0").append(temp);
			} else {
				result.append(temp);
			}
		}

		return result.toString();
	}

	public static String subString(String str,int len){

		return subString(str,len,null);
	}

	public static String subString(String str, int len, String replaceChar) {

		return subString(str,0,len,replaceChar);
	}

	public static String subString(String str, int startIndex, int len, String replaceChar) {
		String temp = str;

		if (!isNull(str) && str.length() > len) {
			temp = str.substring(startIndex, len+startIndex) + (isNull(replaceChar) ? "" : replaceChar);
		}

		return temp;
	}

	public static String htmlEncode(String value) {
		String result = "";
		if (!isNull(value)) {
			result = value.replaceAll("&", "&amp;").replaceAll(">", "&gt;").replaceAll("<", "&lt;").replaceAll("\"", "&quot;").replaceAll(" ", "&nbsp;").replaceAll("\r?\n", "<br/>");
		}
		return result;
	}

	public static String htmlDecode(String value) {
		String result = "";
		if (!isNull(value)) {
			result = value.replaceAll("&amp;", "&").replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"").replace("&nbsp;", " ");
		}
		return result;
	}

	/** 字符串编码(默认使用UTF-8) */
	public static String stringEncode(String value){
		return stringEncode(value,"UTF-8");
	}

	public static String stringEncode(String value,String encoding){
		String result = null;
		if (!isNull(value)) {
			try {
				if (isNull(encoding)) {
					encoding = "UTF-8";
				}
				result = new String(value.getBytes("ISO-8859-1"),encoding);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 格式式化字符串
	 * 允许使用{0}{1}...作为为占位符
	 * @param value 要格式化的字符串
	 * @param args 点位符的值
	 * @return 格式化后的字符串
	 */
	public static String stringFormat(String value, Object... args) {
		// 判断是否为空
		if (isNull(value) || isNull(args)) {
			return value;
		}
		String result = value;
		Pattern p = Pattern.compile("\\{(\\d+)\\}");
		Matcher m = p.matcher(value);
		while (m.find()) {
			// 获取{}里面的数字作为匹配组的下标取值
			int index = Integer.parseInt(m.group(1));
			// 这里得考虑数组越界问题，{1000}也能取到值么？？
			if (index < args.length) {
				// 替换，以{}数字为下标，在参数数组中取值
				result = result.replace(m.group(), args[index].toString());
			}
		}
		return result;
	}

	public static String leftPad(String value, int len, char c) {
		if (isNull(value, len, c)) {
			return value;
		}
		int v = len - value.length();
		for (int i = 0; i < v; i++)
			value = c + value;
		return value;
	}

	public static String rightPad(String value, int len, char c) {
		if (isNull(value, len, c)) {
			return value;
		}
		int v = len - value.length();
		for (int i = 0; i < v; i++)
			value += c;
		return value;
	}

	/** 处理对象的String类型的属性值进行html编码 */
	public static void objectHtmlEncode(Object object) {
		if (!isNull(object)) {
			Method[] mList = object.getClass().getMethods();
			for (Method method : mList) {
				// 方法名
				String mName = method.getName();
				// 得到方法的方法值类型
				String mRetrunType = method.getReturnType().getSimpleName();
				// 得到方法的参数个数
				int mParamSize = method.getParameterTypes().length;
				// 判断方法值是否是String并参数个数为0
				if (mRetrunType.equals("String") && mParamSize == 0) {
					// 判断方法是否是以get开头
					if (mName.startsWith("get")) {
						// 得到相对应的set方法
						Method setMethod = null;
						String setMethodName = "set" + mName.substring(3);
						// 只有一个String类型的参数
						Class[] paramClass = { String.class };
						try {
							setMethod = object.getClass().getMethod(setMethodName, paramClass);
							// 判断set方法的返回值是否为空
							if (!setMethod.getReturnType().getSimpleName().equals("void")) {
								continue; // 查看下一个方法
							}
						} catch (SecurityException e) {
							continue; // 查看下一个方法
						} catch (NoSuchMethodException e) {
							continue; // 查看下一个方法
						}
						Object[] params = null;
						try {
							// 得到方法的值
							String mValue = method.invoke(object, params).toString();
							// 对值进行html编码
							mValue = htmlEncode(mValue);
							// 编码后重新赋值
							params = new Object[] { mValue };
							setMethod.invoke(object, params);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/** 根据属性名得到属性值(entity中必需存在get,set相应方法) */
	public static Object getPropValue(Object entity,String propName){
		Object result = null;
		//判断对象和属性名是否为空
		if (isNull(entity)||isNull(propName)) {
			return result;
		}else{
			try {
				//调用方法得到get方法值
				Method getMethod = entity.getClass().getMethod(propName.trim());
				result = getMethod.invoke(entity	);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return result;
	}



	/** 随机密码 */
	public static String genRandPwd(){
		return getRandNum(6);
	}
	public static String genRandNumber(){
		Random ne=new Random();//实例化一个random的对象ne
		int x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
		return x+"";
	}
	/** 随机数字 */
	public static String getRandNum(int len){
		String result = "";
		String temp = genUUID().replaceAll("[A-Z]+", "");

		int tempLen = temp.length();
		if(len > 32){
			len = 32;
		}
		if (tempLen < len) {
			result = rightPad(temp,len,'0');
		}else{
			for (int i = 0; i < len; i++) {
				int rnd = new Random().nextInt(tempLen);
				result += temp.charAt(rnd);
			}
		}

		return result;
	}
	/** 随机EncodingAESKey */
	public static String getRandEncodingAESKey(){
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";     
		Random random = new Random();     
		StringBuffer sb = new StringBuffer();     
		for (int i = 0; i < 43; i++) {     
			int number = random.nextInt(base.length());     
			sb.append(base.charAt(number));     
		}     
		return sb.toString(); 
	}
	// 随机会员注册推荐码
	public static String getRandRegistRC(int len){
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";     
		Random random = new Random();     
		StringBuffer sb = new StringBuffer();
		if(len < 4)len = 4;
		if(len > 10)len = 10;
		for (int i = 0; i < len; i++) {     
			int number = random.nextInt(base.length());     
			sb.append(base.charAt(number));     
		}     
		return sb.toString(); 
	}

	/** 保存文件 */
	public static void saveToFile(File target, String distPath) throws IOException {
		if (isNull(target,distPath)) {
			return;
		}
		File distFile = new File(distPath);
		//确保文件所在的文件夹都存在
		distFile.getParentFile().mkdirs();

		//输入流
		InputStream is = new BufferedInputStream(new FileInputStream(target));
		//输出流
		OutputStream os = new BufferedOutputStream(new FileOutputStream(distFile));
		//每次读取的大小
		byte[] size = new byte[1024];
		//流长度
		int len = 0;
		//循环读取
		while((len = is.read(size)) != -1){
			os.write(size, 0, len);
		}
		os.flush();
		os.close();
		is.close();
	}

	public static void saveToFile(InputStream target, String distPath) throws IOException {
		if (isNull(target,distPath)) {
			return;
		}
		File distFile = new File(distPath);
		//确保文件所在的文件夹都存在
		distFile.getParentFile().mkdirs();

		//输入流
		InputStream is = new BufferedInputStream(target);
		//输出流
		OutputStream os = new BufferedOutputStream(new FileOutputStream(distFile));
		//每次读取的大小
		byte[] size = new byte[1024];
		//流长度
		int len = 0;
		//循环读取
		while((len = is.read(size)) != -1){
			os.write(size, 0, len);
		}
		os.flush();
		os.close();
		is.close();
	}

	/** 得到Cookie */
	public static Cookie getCookie(String name, HttpServletRequest request){
		for(Cookie cookie : request.getCookies()){
			if (cookie.getName().equals(name)) {
				return cookie;
			}
		}
		return null;
	}

	/** 创建Cookie */
	public static void setCookie(String name, String value, Integer maxAge, HttpServletResponse response){
		Cookie cookie =new Cookie(name,value);
		if (!isNull(maxAge)) {
			cookie.setPath("/");
			cookie.setMaxAge(maxAge.intValue());
		}
		response.addCookie(cookie);
	}

	/** 创建Cookie */
	public static void setCookie(String name, String value, HttpServletResponse response){
		setCookie(name, value, null, response);
	}

	/** 删除Cookie */
	public static void delCookie(String name, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static String processJsonStr(String jsonStr){
		return jsonStr.replaceAll("\r\n", "").replaceAll("\n", "").replace("\"", "'");
	}

	public static boolean saveToFile(byte[] dataBuf, String filePath){
		if(dataBuf==null || filePath==null)
			return false;

		boolean ret = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			out.write(dataBuf);
			ret = true;
		}
		catch (Exception e){

		}
		finally{
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
		}

		return ret;

	}


	public static boolean saveToFileEx(byte[] dataBuf, String filePath, boolean append){
		if(dataBuf==null || filePath==null)
			return false;

		boolean ret = false;
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath, append);
			out.write(dataBuf);
			ret = true;
		}
		catch (Exception e){

		}
		finally{
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}
		}

		return ret;

	}

	public static boolean downloadFile(String urlPath, String localPath) {
		if(urlPath==null || urlPath.equals(""))
			return false;

		HttpURLConnection conn = null;
		InputStream in = null;
		OutputStream out = null;
		byte[] rcvBuf = null;
		try {		
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setDefaultUseCaches(false);
			conn.setRequestProperty("Content-type", "application/octet-stream");
			out = conn.getOutputStream();

			out.flush();
			out.close();

			String message = conn.getResponseMessage();
			//			System.out.println("ResponseCode : " + conn.getResponseCode());
			//			System.out.println("Message : " + message);

			in = conn.getInputStream();

			int len = conn.getContentLength(); 
			if (len > 0) { 
				int actual = 0; 
				int bytesRead = 0 ; 
				rcvBuf = new byte[len]; 
				while ((bytesRead < len) && (actual != -1)) { 
					actual = in.read(rcvBuf, bytesRead, len - bytesRead); 
					bytesRead += actual;
				}  
			}
			saveToFile(rcvBuf, localPath);

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out = null;
			}		    	
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in = null;
			}

			if(conn != null){
				conn.disconnect();
				conn = null;
			}

		}
		return true;
	}
	public static String replaceString(String src, String dest, String r){
		if (src == null || src.length() == 0)
			return "";
		int index = src.indexOf(dest);
		int pos = 0;
		StringBuffer ret = new StringBuffer();
		while (index >= 0)
		{
			ret.append(src.substring(pos, index)).append(r);
			pos = index + dest.length();
			index = src.indexOf(dest, index + dest.length());
		}
		if (pos < src.length())
		{
			ret.append(src.substring(pos));
		}
		return ret.toString();
	}

	public static boolean unzipFile(String zipFile, String zipToPath){
		if(zipFile==null || zipFile.equals(""))
			return false;

		boolean bRet = true;
		ZipInputStream zipIn = null;
		try {
			File file = new File(zipFile);
			if(!file.exists())
				return false;

			File path=new File(zipToPath);
			if(!path.exists())
				path.mkdir();

			zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));

			ZipEntry zipentry = null;
			while((zipentry=zipIn.getNextEntry()) != null){			   
				if (zipentry.isDirectory()){
					String name=zipentry.getName();
					name=name.substring(0,name.length()-1);
					File f=new File(zipToPath+File.separator+name);
					f.mkdir();
				}
				else{
					String name=zipentry.getName();
					if(name.lastIndexOf('\\') >= 0)
						name = name.substring(name.lastIndexOf('\\'));
					else if(name.lastIndexOf('/') >= 0)
						name = name.substring(name.lastIndexOf('/'));

					File f=new File(zipToPath+"/"+name);
					f.createNewFile();
					FileOutputStream out=new FileOutputStream(f);
					int b;
					while ((b=zipIn.read()) != -1)
						out.write(b);
					out.close();
				}

			}
		}catch(Exception ex){
			bRet = false;
			ex.printStackTrace();
		}finally{
			if(zipIn != null){
				try {
					zipIn.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				zipIn = null;
			}
		}

		return bRet;
	}

	public static byte[] readFile(String file) { 
		FileInputStream fis = null; 
		ByteArrayOutputStream bos = null; 
		try { 
			fis = new FileInputStream(file); 
			bos = new ByteArrayOutputStream(); 
			int bytesRead; 
			byte buffer[] = new byte[1024 * 1024]; 
			while ((bytesRead = fis.read(buffer)) != -1) { 
				bos.write(buffer, 0, bytesRead); 
				Arrays.fill(buffer, (byte) 0); 
			} 
		} catch (IOException e1) { 
			e1.printStackTrace(); 
		} finally { 
			try { 
				if (bos != null) 
					bos.close(); 
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
		} 
		return bos.toByteArray(); 
	}
	/**
	 * 检查上传的图片类型是否是支持的类型
	 * @param content
	 * @return
	 */
	public static boolean checkImage(String content){
		if(content == null || content.equals(""))
			return false;

		for( String str : IMG_TYPES){
			if(content.contains(str))
				return true;
		}
		return false;
	}
	/** 生成随机UUID */
	public static String genUUID(){
		UUID uuid = UUID.randomUUID();
		String temp = uuid.toString();
		return temp.replaceAll("-", "").toUpperCase();
	}
	public static String getNextWeekMonday(){
		Date d=addDate(Util.getCurrentDate(), 5);
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的
		return formatDate(cal.getTime());
	}
	public static String getNextFirdayMonday(){
		Date d=addDate(Util.getCurrentDate(), 5);
		Calendar cal=Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY); //获取本周一的
		return formatDate(cal.getTime());
	}
	/**季度获取开始时间*/
	public static String getBeginTimeBySeason(Integer year,Integer season){
		if(season.toString().equals("1")){
			return year+"-1-1";
		}if(season.toString().equals("2")){
			return year+"-4-1";
		}if(season.toString().equals("3")){
			return year+"-7-1";
		}if(season.toString().equals("4")){
			return year+"-10-1";
		}
		return null;
	}
	/**季度获取截止时间*/
	public static String getEndTimeBySeason(Integer year,Integer season){
		if(season.toString().equals("1")){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH, 2);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return year+"-3-"+lastDay;
		}if(season.toString().equals("2")){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH, 5);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return year+"-6-"+lastDay;
		}if(season.toString().equals("3")){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH, 8);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return year+"-9-"+lastDay;
		}if(season.toString().equals("4")){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR,year);
			cal.set(Calendar.MONTH, 11);
			int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			return year+"-12-"+lastDay;
		}
		return null;
	}

	public static int getDayofMonth(Date d){
		Calendar c=Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.DATE);
	}
	/**月份获取开始时间*/
	public static String getBeginTimeByMonth(Integer year,Integer month){
		return year+"-"+month+"-1";
	}
	/**月份获取截止时间*/
	public static String getEndTimeByMonth(Integer year,Integer month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month-1);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		return year+"-"+month+"-"+lastDay;
	}

	/**
	 * 保留digitNum位小数
	 * @param digitNum 保留小数点位数
	 * @param num 数字
	 * @return
	 */
	public static BigDecimal getRound(int digitNum,BigDecimal num){
		return num.setScale(digitNum,BigDecimal.ROUND_HALF_UP);  
	}

	/**
	 * 保留2位小数
	 */
	public static BigDecimal getRound(BigDecimal num){
		return getRound(2,num);
	}

	/**
	 * 获取折扣价,保留2位小数
	 */
	public static BigDecimal getdisPrice(BigDecimal price,double yhbl){
		BigDecimal disprice = new BigDecimal(price.doubleValue()*yhbl/100);
		return getRound(disprice);
	}



	// 测试
	public static void main(String[] args) {
		//		String endtime="2014-06-08";
		//		System.err.println(Util.formatDateTime(Util.getEndTime(endtime)));
		//		try {
		//			System.err.println(Util.getEndTime("2013-8-13 23:59:59"));
		//		} catch (Throwable e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//String end=Util.getCurrentDateStr()+" 09:00:00";

		//Long fz=compareFZ(Util.dateTimeStrToDateTime(end),Util.getCurrentDateTime());
		//		System.err.println(getFirdayOfThisWeek());
		//		System.err.println(Util.getCurrYear());
		//		System.err.println(Util.getCurrMonth());
		//System.err.println(fz);


		//		Calendar cal = Calendar.getInstance();
		//		System.out.println("今天的日期: " + cal.getTime());
		//
		//		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;
		//		cal.add(Calendar.DATE, -day_of_week);
		//		System.out.println("本周第一天: " + cal.getTime());
		//
		//		cal.add(Calendar.DATE, 6);
		//		System.out.println("本周末: " + cal.getTime());
		//		
		//		System.out.println(getDayofMonth(new Date()));
		//生成32位MD5
		//		System.out.println(Util.md5("Kqqqqqj_662294").length());
		//		System.err.println(getRandRegistRC(6));
		//		System.err.println(md5("751231yxhl8888").toLowerCase());

		System.out.println(Util.dateToDateStr("MMdd", new Date())+"-"+leftPad("123", 4, '0'));
	}


	//	public static void check() {
	//		checkArgument;
	//	}
	
	

}
