package com.yxhl.stationbiz.system.domain.request;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结算单管理请求对象
 * @author lw
 *
 */
@Data
public class SettlementStatisticRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位（即营运单位） 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "站点ID")
	private String stationId;
	
	@ApiModelProperty(value = "查询类型：1 按日期 2按月份 3按时段")
	private String queryType;
	
	@ApiModelProperty(value = "按日期查询时传入查询日期 ")
	private String date;
	@ApiModelProperty(value = "按月份查询时传入查询月份 ")
	private String month;
	
	@ApiModelProperty(value = "按时段查询时传入开始日期 ")
	private String startDate;
	@ApiModelProperty(value = "按时段查询时传入结束日期 ")
	private String endDate;
	
	@ApiModelProperty(value = "车牌号 ")
	private String reportCarNo;
}
