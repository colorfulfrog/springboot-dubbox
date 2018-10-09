package com.yxhl.stationbiz.system.domain.service.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;

/**
 *  TicketSellerBillService
 *  注释:售票员票据表Service
 *  创建人: ypf
 *  创建日期:2018-9-12 11:45:23
 */
public interface TicketSellerBillService extends IELService<TicketSellerBill>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketSellerBill 条件参数
	 * @return 当前页数据
	 */
	Page<TicketSellerBill> selPageList(Page<TicketSellerBill> page,TicketSellerBill ticketSellerBill);
	
	Page<TicketSellerBill> companybillInfo(Page<TicketSellerBill> page,String  billEntryId);
}
