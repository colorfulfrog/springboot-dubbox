package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结算单查询返回对象
 * @author lw
 */
@Data
public class SettlementStatisticResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "班次ID")
	private String id;
	
	@ApiModelProperty(value = "站点名称")
	private String stationName;
	
	@ApiModelProperty(value = "三联单号")
	private String triplicateBillNum;
	
	@ApiModelProperty(value = "三联单开单人")
	private String triplicateBiller;
	
	@ApiModelProperty(value = "班次号")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "发车日期 ")
	private Date runDate;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@ApiModelProperty(value = "报班车车牌号")
	private java.lang.String reportCarNo;
	
	@ApiModelProperty(value = "稽查员")
	private java.lang.String inspectionUser;
	
	@ApiModelProperty(value = "合计人数")
	private java.lang.Integer totalPassengerCount;
	
	@ApiModelProperty(value = "稽查补缴款 ")
	private java.lang.Float paymentFee;
	
	@ApiModelProperty(value = "补缴人数 ")
	private java.lang.Integer paymentPeople;
	
	@ApiModelProperty(value = "售票金额 ")
	private java.lang.Float ticketAmount;
	
	@ApiModelProperty(value = "总金额 ")
	private java.lang.Float totalAmount;
	
	@ApiModelProperty(value = "各票种售票数")
	private List<TicketCategoryCountResponse> tcCountList;
	
	//补全字段
	@ApiModelProperty(value = "出站检查员")
	private java.lang.String outboundChecker;
	
	@ApiModelProperty(value = "站务费")
	private java.lang.Float stationFee;
	
	@ApiModelProperty(value = "客运结算金额 ")
	private java.lang.Float settlementAmount;
}
