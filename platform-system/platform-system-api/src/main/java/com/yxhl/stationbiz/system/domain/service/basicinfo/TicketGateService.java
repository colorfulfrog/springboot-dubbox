package com.yxhl.stationbiz.system.domain.service.basicinfo;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.TicketGate;

/**
 *  TicketGateService
 *  注释:检票口表Service
 *  创建人: lw
 *  创建日期:2018-7-10 9:28:58
 */
public interface TicketGateService extends IELService<TicketGate>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketGate 条件参数
	 * @return 当前页数据
	 */
	Page<TicketGate> selPageList(Page<TicketGate> page,TicketGate ticketGate);
}
