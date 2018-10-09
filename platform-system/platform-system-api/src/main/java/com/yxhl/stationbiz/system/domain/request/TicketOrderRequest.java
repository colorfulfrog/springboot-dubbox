package com.yxhl.stationbiz.system.domain.request;

import java.io.Serializable;
import java.util.List;

import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 1.窗口售票 2.补票 3.改签
 * 
 * @author lc
 *
 */

@Data
public class TicketOrderRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "始发站ID（发车站）")
	private String startStationId;

	@ApiModelProperty(value = "发车时间")
	private String runDateStr;

	@ApiModelProperty(value = "报班车站 长度(32)")
	private java.lang.String reportStaId;

	@ApiModelProperty(value = "所属班次")
	private java.lang.String scheduleBusId;

	@ApiModelProperty(value = "发票号码 长度(32)")
	private java.lang.String invoiceNo;

	@ApiModelProperty(value = "票种集合")
	private List<TicketCateValue> ticketCateValues;

	@ApiModelProperty(value = "是否购买保险票 1买 2不买")
	private java.lang.Integer isPurchase;

	@ApiModelProperty(value = "购买保险票数量")
	private java.lang.Integer purchaseNum;

	@ApiModelProperty(value = "补票（原订单号）")
	private String oldOrderId;

	@ApiModelProperty(value = "补票（原改签票号-售票ID）")
	private String oldTicketId;

	@ApiModelProperty(value = "订单信息")
	Order order;

	@ApiModelProperty(value = "售票表（乘车人信息）")
	List<Ticket> tickets;

}
