package com.yxhl.stationbiz.system.domain.service.schedule;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;

import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;

/**
 *  ExecPriceService
 *  注释:执行票价表Service
 *  创建人: lw
 *  创建日期:2018-8-21 10:23:17
 */
public interface ExecPriceService extends IELService<ExecPrice>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param execPrice 条件参数
	 * @return 当前页数据
	 */
	Page<ExecPrice> selPageList(Page<ExecPrice> page,ExecPrice execPrice);

	/**
	 * 将基本票价复制到       执行票价
	 * @param basicPriceList
	 * @param ticketCateValues
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	void addExecPriceByBasic(List<BasicPrice> basicPriceList,List<TicketCateValue> ticketCateValues) throws IllegalAccessException, InvocationTargetException;

	/**
	 * 将节假日票价复制至    执行票价
	 * @param holidayPriceList
	 * @param ticketCateValues
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	void addExecPriceByHoliday(List<HolidayPrice> holidayPriceList, List<TicketCateValue> ticketCateValues)
			throws IllegalAccessException, InvocationTargetException;
	
	
	/**
	 * 根据班次编号  查询执行票价
	 * @param List
	 * @param scheduleBusId
	 * @return
	 */
	List<ExecPrice> selExecPrice(String scheduleBusId);
	
	/**
	 * 复制班次节假日票价
	 * @param bus 班次
	 * @param holidayID 节日ID
	 */
	void copyHolidayPrice(ScheduleBus bus,String holidayID) throws Exception;
	
	/**
	 * 复制班次节基础票价
	 * @param bus 班次
	 */
	void copyBasicPrice(ScheduleBus bus) throws Exception;

	/**
	 * 查询班次 班次执行票价
	 * @param scheduleBusId
	 * @param seatCate
	 * @param seats
	 * @return
	 */
	List<TicketCateValue> selExecPriceBy(String scheduleBusId, String seatCate, Integer seats);
}
