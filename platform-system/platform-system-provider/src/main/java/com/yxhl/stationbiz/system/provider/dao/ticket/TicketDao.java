package com.yxhl.stationbiz.system.provider.dao.ticket;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.response.StatisticTCResponse;

/**
 *	
 *  bs_ticketDao
 *  注释:售票表
 *  创建人: lw
 *  创建日期:2018-9-14 10:43:42
 */
@Mapper
@Repository
public interface TicketDao extends CrudDao<Ticket>{
	/**
	 * 财务模块/售票分页查询
	 * @param page 分页参数
	 * @param ticket 条件参数
	 * @return 当前页数据
	 */
	List<Ticket> selPageList(Pagination page,Ticket ticket);
	
	List<Ticket> selTicketPageList(Pagination page,Ticket ticket);
	
	//退票列表
	List<Ticket> selRefundPageList(Pagination page,Ticket ticket);
	/**
	 * 售票模块/废票分页查询
	 * @param page 分页参数
	 * @param ticket 条件参数
	 * @return 当前页数据
	 */
	List<Ticket> fpPageList(Pagination page,Ticket ticket);
	
	/**
	 * 统计班次各票种售票数
	 * @param scheduleBusId 班次ID
	 * @return
	 */
	List<StatisticTCResponse> statisticTCCount(String scheduleBusId);
	
	/**
	 * 车票打印信息
	 * @param orderId
	 * @return
	 */
	List<Ticket> getcpdyList(Map<String,Object> map);
	
}