package com.yxhl.stationbiz.system.domain.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 班次检票-发班请求
 * @author lw
 *
 */
@Data
public class ScheduleDepartRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "班次ID")
	private String scheduleBusId;
	
	@ApiModelProperty(value = "三联单号")
	private String triplicateBillNum;
}
