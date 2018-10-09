package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 票种售票数
 * @author lw
 */
@Data
public class StatisticTCResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "票种ID")
	private String ticketCategoryId;
	
	@ApiModelProperty(value = "售票数量")
	private int saleCount;
}
