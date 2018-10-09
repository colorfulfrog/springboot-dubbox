package com.yxhl.stationbiz.system.domain.entity.ticket;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_bus_seats
 *  注释:班次座位表
 *  创建人: lw
 *  创建日期:2018-9-13 9:57:59
 */
@Data
@TableName(value="bs_schedule_bus_seats")
public class ScheduleBusSeats extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "班次ID，关联班次表(bs_schedule_bus) 长度(32)")
	private java.lang.String scheduleBusId;
	
	@ApiModelProperty(value = "座位号 ")
	private java.lang.Integer seatNum;
	
	@ApiModelProperty(value = "状态(数据字典)：2可选 3预留 4不售 5已售   长度(2)")
	private java.lang.String seatStatus;
}
