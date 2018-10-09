package com.yxhl.stationbiz.system.domain.service.basicinfo;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoopBus;

/**
 *  ScheduleLoopBusService
 *  注释:班次循环配置中间表Service
 *  创建人: lw
 *  创建日期:2018-7-11 9:31:17
 */
public interface ScheduleLoopBusService extends IELService<ScheduleLoopBus>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleLoopBus 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleLoopBus> selPageList(Page<ScheduleLoopBus> page,ScheduleLoopBus scheduleLoopBus);
}
