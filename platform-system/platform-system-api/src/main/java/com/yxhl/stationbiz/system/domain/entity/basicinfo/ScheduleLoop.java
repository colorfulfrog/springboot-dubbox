package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_loop
 *  注释:班次调度循环配置
 *  创建人: lw
 *  创建日期:2018-7-11 9:27:23
 */
@Data
@TableName(value="bs_schedule_loop")
public class ScheduleLoop extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "关联线路 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "开始日期 ")
	private java.util.Date startDate;
	
	@ApiModelProperty(value = "结束日期 ")
	private java.util.Date endDate;
	
	@ApiModelProperty(value = "循环类型(0每日、1农历单、2农历双、3每周、4隔周、5隔日、6月班) ")
	private java.lang.Integer loopType;
	
	@ApiModelProperty(value = "0正常、1关闭 ")
	private java.lang.Integer status;
	
	//扩展字段
	@TableField(exist=false)
	@ApiModelProperty(value = "班次模板列表")
	private List<ScheduleBusTpl> scheduleBusTpls;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "班次模板ID")
	private String tplId;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "班次号")
	private String busCode;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "发车时间")
	private Date startTime;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "末班车发车时间")
	private Date lastDepartureTime;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "线路名称")
	private String lineName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "座位数")
	private Integer persons;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "是否流水班次 0否 1是")
	private java.lang.Integer runFlowFlag;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "所属单位")
	private String companyName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "开始日期 起始")
	private Date startDateBegin;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "开始日期 至")
	private Date startDateEnd;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "结束日期 起始")
	private Date endDateBegin;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "结束日期 至")
	private Date endDateEnd;
}
