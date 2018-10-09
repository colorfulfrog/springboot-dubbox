package com.yxhl.stationbiz.system.domain.service.schedule;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;

/**
 *  TicketCategoryService
 *  注释:票种设置表Service
 *  创建人: xjh
 *  创建日期:2018-8-16 10:25:40
 */
public interface TicketCategoryService extends IELService<TicketCategory>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketCategory 条件参数
	 * @return 当前页数据
	 */
	Page<TicketCategory> selPageList(Page<TicketCategory> page,TicketCategory ticketCategory);
}
