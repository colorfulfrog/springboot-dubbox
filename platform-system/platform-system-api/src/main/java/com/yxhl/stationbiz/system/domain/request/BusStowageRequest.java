package com.yxhl.stationbiz.system.domain.request;


import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  班次配载请求实体
 *  创建人: lw
 */
@Data
public class BusStowageRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "待选班次ID列表")
	private List<String> candidateBusIds;
	
	@ApiModelProperty(value = "从属班次ID")
	private String belongBusId;
}
