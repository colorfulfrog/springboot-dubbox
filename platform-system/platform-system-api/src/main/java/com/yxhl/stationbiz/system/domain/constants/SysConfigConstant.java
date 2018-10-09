package com.yxhl.stationbiz.system.domain.constants;

/**
 * 系统参数编码常量
 * @author lw
 *
 */
public interface SysConfigConstant {
	//数字代表每张车票收取5元站务费
	String STATION_FEE_PER_TICKET = "1008";
	
	//数字代表每张票收取站务费的百分比
	String STATION_FEE_PERCENT = "1009";
	
	//1按固定金额收取，2按票面比例收取
	String STATION_FEE_TYPE = "1010";
	
	//售票-锁票时间
	String SEAT_LOCK_EXPIRE = "1013";
}
