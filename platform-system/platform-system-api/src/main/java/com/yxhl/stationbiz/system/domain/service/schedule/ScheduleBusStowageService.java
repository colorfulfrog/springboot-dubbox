package com.yxhl.stationbiz.system.domain.service.schedule;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBusStowage;

/**
 *  ScheduleBusStowageService
 *  注释:班次配载表Service
 *  创建人: lw
 *  创建日期:2018-9-15 9:49:26
 */
public interface ScheduleBusStowageService extends IELService<ScheduleBusStowage>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusStowage 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBusStowage> selPageList(Page<ScheduleBusStowage> page,ScheduleBusStowage scheduleBusStowage);
}
