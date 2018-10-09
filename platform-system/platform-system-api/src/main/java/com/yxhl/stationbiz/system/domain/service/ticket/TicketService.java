package com.yxhl.stationbiz.system.domain.service.ticket;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.response.StatisticTCResponse;

/**
 *  TicketService
 *  注释:售票表Service
 *  创建人: lw
 *  创建日期:2018-9-14 10:43:42
 */
public interface TicketService extends IELService<Ticket>{
	
	/**
	 * 财务模块/售票分页查询
	 * @param page 分页参数
	 * @param ticket 条件参数
	 * @return 当前页数据
	 */
	Page<Ticket> selPageList(Page<Ticket> page,Ticket ticket);
	
	Page<Ticket> selTicketPageList(Page<Ticket> page,Ticket ticket);
	
	//退票列表
	Page<Ticket> selRefundPageList(Page<Ticket> page,Ticket ticket) throws NumberFormatException, Exception;
	/**
	 * 售票模块/废票分页查询
	 * @param page 分页参数
	 * @param ticket 条件参数
	 * @return 当前页数据
	 */
	Page<Ticket> fpPageList(Page<Ticket> page,Ticket ticket);
	
	/**
	 * 废票
	 * @param ticketId
	 * @return
	 */
	boolean scrapTicket(Ticket ticket);

	/**
	 * 退检
	 * @param ticket
	 * @param checkUserId 检票员
	 * @return
	 */
	boolean refundCheck(Ticket ticket,String checkUserId);

	/**
	 * 检票
	 * @param ticket
	 * @param checkUserId 检票员
	 * @return
	 */
	boolean checkSingle(Ticket ticket,String checkUserId);

	/**
	 * 混检
	 * @param scheduleBusId 检票班次
	 * @param ticket 车票
	 * @param checkUserId 检票员
	 * @return
	 */
	boolean mixCheck(String scheduleBusId, Ticket ticket,String checkUserId);
	
	/***
	 * 退票/退保险详情
	 * @param ticket
	 * @return
	 */
	List<Ticket> retreatTicketInfo(Ticket ticket) throws NumberFormatException, Exception;
	/**
	 * 统计班次各票种售票数
	 * @param scheduleBusId 班次ID
	 * @return
	 */
	List<StatisticTCResponse> statisticTCCount(String scheduleBusId);
	
	/***
	 * 退票
	 * @param ticket
	 * @return
	 */
	boolean retreatTicket(Ticket ticket) throws NumberFormatException, Exception;
	
	/**
	 * 车票打印信息
	 * @param orderId
	 * @return
	 */
	List<Ticket> getcpdyList(String orderId);
	
}
