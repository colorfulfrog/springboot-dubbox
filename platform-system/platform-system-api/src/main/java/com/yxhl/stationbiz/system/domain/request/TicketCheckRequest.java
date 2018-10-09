package com.yxhl.stationbiz.system.domain.request;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TicketCheckRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "发车日期 ")
	private Date runDate;
	
	@ApiModelProperty(value = "检票口ID")
	private java.lang.String ticketGateId;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@ApiModelProperty(value = "始发站ID（发车站）")
	private String startStationId;
	
	@ApiModelProperty(value = "终点站")
	private String endStationId;
	
	@ApiModelProperty(value = "班次号 长度(20)")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "报班车车牌号 长度(20)")
	private java.lang.String reportCarNo;
	
	@ApiModelProperty(value = "已报班 选中传1")
	private java.lang.Integer reportFlag;
	
	@ApiModelProperty(value = "未发班 选中传1")
	private java.lang.Integer notRunFlag;
	
	@ApiModelProperty(value = "已售票 选中传1")
	private java.lang.Integer saleTicketFlag;
	
	@ApiModelProperty(value = "已发班 选中传1")
	private java.lang.Integer runFlag;
	

	
	
}
