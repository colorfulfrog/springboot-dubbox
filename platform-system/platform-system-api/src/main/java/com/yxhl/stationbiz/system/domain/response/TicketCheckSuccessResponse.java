package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author lw
 */
@Data
public class TicketCheckSuccessResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "班次号")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "发车日期 ")
	private Date runDate;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@ApiModelProperty(value = "出发地名称")
	private java.lang.String start;
	
	@ApiModelProperty(value = "目的地名称")
	private java.lang.String destination;
	
	@ApiModelProperty(value = "乘车人姓名")
	private java.lang.String passengerName;
	
	@ApiModelProperty(value = "女，男")
	private java.lang.String sex;
	
	@ApiModelProperty(value = "身份证号码")
	private java.lang.String idCardNo;
}
