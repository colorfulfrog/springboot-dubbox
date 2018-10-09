package com.yxhl.stationbiz.system.domain.service.schedule;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;

/**
 *  TicketCateValueService
 *  注释:票种取值表Service
 *  创建人: ypf
 *  创建日期:2018-8-15 14:12:46
 */
public interface TicketCateValueService extends IELService<TicketCateValue>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketCateValue 条件参数
	 * @return 当前页数据
	 */
	Page<TicketCateValue> selPageList(Page<TicketCateValue> page,TicketCateValue ticketCateValue);
	
	List<TicketCateValue> selPriceValue(TicketCateValue ticketCateValue);
}
