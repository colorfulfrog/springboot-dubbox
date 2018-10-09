package com.yxhl.stationbiz.system.domain.request;



import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改进站口信息
 * @author Administrator
 *
 */
@Data
public class ScheduleBusRequest  implements Serializable{
	private static final long serialVersionUID = 1L;

	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	private String ticketGateId;//检票口
	
	private String busEntranceId;//乘车库
	
	private Integer carryChildrenNum;
	
	List<String> ids;//选中的班次信息
	
	public static void main(String[] args) throws Exception {
		Format f = new SimpleDateFormat("yyyy-MM-dd");
		 
        Date today = new Date();
        System.out.println("今天是:" + f.format(today));
 
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, 300);// 今天+1天
 
        Date tomorrow = c.getTime();
        System.out.println("明天是:" + f.format(tomorrow));
        
        Integer a=differentDays(today,tomorrow);
        
        System.out.println(a);

	}
	
	   public static int differentDays(Date date1,Date date2)
	    {
	        Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //同一年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //不同年
	        {
	            System.out.println("判断day2 - day1 : " + (day2-day1));
	            return day2-day1;
	        }
	    }
}
