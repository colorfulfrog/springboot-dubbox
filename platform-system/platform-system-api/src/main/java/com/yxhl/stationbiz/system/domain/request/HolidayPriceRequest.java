package com.yxhl.stationbiz.system.domain.request;


import java.io.Serializable;
import java.util.List;

import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;

import lombok.Data;

@Data
public class HolidayPriceRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	List<HolidayPrice>holidayprice;
	
	private	String lineNameCopyFlag;
	
	private String holidayId;
	
	private String lineId;
	
}
