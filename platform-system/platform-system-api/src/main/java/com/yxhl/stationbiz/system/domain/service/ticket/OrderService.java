package com.yxhl.stationbiz.system.domain.service.ticket;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.request.TicketOrderRequest;
import com.yxhl.stationbiz.system.domain.response.TicketOrderResponse;

/**
 * OrderService 注释:订单表Service 创建人: lw 创建日期:2018-9-15 10:39:27
 */
public interface OrderService extends IELService<Order> {

	/**
	 * 分页查询
	 * 
	 * @param page
	 *            分页参数
	 * @param order
	 *            条件参数
	 * @return 当前页数据
	 */
	Page<Order> selPageList(Page<Order> page, Order order);

	/**
	 * 预估订单费用
	 * 
	 * @param estimatedOrderCostRequest
	 *            界面入参
	 * @param loginUser
	 *            当前操作用户
	 * @return
	 */
	JSONObject getOrderCost(TicketOrderRequest ticketOrderRequest, ELUser loginUser);

	/**
	 *窗口售票/补票- 下单
	 * 
	 * @param estimatedOrderCostRequest
	 * @param loginUser
	 * @return
	 */
	String addPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser,ScheduleBus getScheduleBus) throws Exception;

	/**
	 * 校验下单/补票 基础数据
	 * @param ticketOrderRequest
	 * @param loginUser
	 * @param string
	 * @return
	 */
	ScheduleBus checkPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser, String type);

	/**
	 * 根据票号找订单
	 * @param invoiceNo
	 * @return
	 */
	TicketOrderResponse getOrderAndTicket(String invoiceNo);

	/**
	 * 改签F9-下单
	 * @param ticketOrderRequest
	 * @param loginUser
	 * @param getScheduleBus
	 * @return
	 */
	String resetAddPlaceOrder(TicketOrderRequest ticketOrderRequest, ELUser loginUser, ScheduleBus getScheduleBus);
}
