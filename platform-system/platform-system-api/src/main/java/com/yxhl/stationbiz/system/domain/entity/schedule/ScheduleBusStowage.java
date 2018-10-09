package com.yxhl.stationbiz.system.domain.entity.schedule;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_bus_stowage
 *  注释:班次配载表
 *  创建人: lw
 *  创建日期:2018-9-15 10:37:52
 */
@Data
@TableName(value="bs_schedule_bus_stowage")
public class ScheduleBusStowage extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "从属班次，其他班次被配载到该班次 长度(32)")
	private java.lang.String belongBusId;
	
	@ApiModelProperty(value = "待选班次,该班次被配载到从属班次 长度(32)")
	private java.lang.String candidateBusId;
	
}
