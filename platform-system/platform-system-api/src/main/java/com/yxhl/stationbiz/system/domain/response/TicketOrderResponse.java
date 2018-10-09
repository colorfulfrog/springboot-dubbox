package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;
import java.util.List;

import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 1.窗口售票 2.补票
 * 
 * @author lc
 *
 */

@Data
public class TicketOrderResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "订单信息")
	Order Order;

	@ApiModelProperty(value = "售票表（乘车人信息）")
	List<Ticket> tickets;
}
