package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_loop_bus
 *  注释:班次循环配置中间表
 *  创建人: lw
 *  创建日期:2018-7-11 9:31:17
 */
@Data
@TableName(value="bs_schedule_loop_bus")
public class ScheduleLoopBus extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "关联线路 长度(32)")
	private java.lang.String scheduleLoopId;
	
	@ApiModelProperty(value = "关联班次模板表 长度(32)")
	private java.lang.String scheduleBusId;
	
}
