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
public class TicketCheckVehicleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "报班车车牌号")
	private String reportCarNo;
	
	@ApiModelProperty(value = "核定座位数 ")
	private java.lang.Integer approvedSeats;
	
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "报班时间 ")
	private java.util.Date reportTime;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@ApiModelProperty(value = "座位类型 0普通座，1商务座，2卧铺")
	private java.lang.String seatCategoryName;
	
	@ApiModelProperty(value = "厂牌型号")
	private java.lang.String brandModel;
}
