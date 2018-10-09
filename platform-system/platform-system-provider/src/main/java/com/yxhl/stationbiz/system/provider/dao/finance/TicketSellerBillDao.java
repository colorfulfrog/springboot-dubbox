package com.yxhl.stationbiz.system.provider.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;

/**
 *	
 *  bs_ticket_seller_billDao
 *  注释:售票员票据表
 *  创建人: ypf
 *  创建日期:2018-9-12 11:45:23
 */
@Mapper
@Repository
public interface TicketSellerBillDao extends CrudDao<TicketSellerBill>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketSellerBill 条件参数
	 * @return 当前页数据
	 */
	List<TicketSellerBill> selPageList(Pagination page,TicketSellerBill ticketSellerBill);
	
	List<TicketSellerBill> companybillInfo(Pagination page,String billEntryId);
}