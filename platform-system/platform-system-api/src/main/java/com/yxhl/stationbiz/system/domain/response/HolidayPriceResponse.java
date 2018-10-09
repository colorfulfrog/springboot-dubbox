package com.yxhl.stationbiz.system.domain.response;


import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HolidayPriceResponse implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "票种id")
	private String id;
	
	@ApiModelProperty(value = "数据行id")
	private String rowId;
	
	@ApiModelProperty(value = "字段名称")
	private String fieldName;
	
	@ApiModelProperty(value = "票种名称")
	private String ticketCateName;
	
	@ApiModelProperty(value = "票价")
	private Float ticketValue;
	
	@ApiModelProperty(value = "班次模板id")
	private String scheduleTplId;
}
