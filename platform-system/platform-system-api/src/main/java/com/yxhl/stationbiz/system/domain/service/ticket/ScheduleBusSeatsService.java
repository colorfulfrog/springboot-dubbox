package com.yxhl.stationbiz.system.domain.service.ticket;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;

/**
 *  ScheduleBusSeatsService
 *  注释:班次座位表Service
 *  创建人: lw
 *  创建日期:2018-9-13 9:57:59
 */
public interface ScheduleBusSeatsService extends IELService<ScheduleBusSeats>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusSeats 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBusSeats> selPageList(Page<ScheduleBusSeats> page,ScheduleBusSeats scheduleBusSeats);

	/**
	 * 修改座位状态为已售
	 * @param ids
	 * @return
	 */
	boolean updateScheduleBusSeatStatus(List<String> ids);
}
