package com.yxhl.stationbiz.system.domain.entity.check;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_vehicle_outbound_check
 *  注释:车辆出站稽查表
 *  创建人: xjh
 *  创建日期:2018-9-12 10:22:24
 */
@Data
@TableName(value="bs_vehicle_outbound_check")
public class VehicleOutboundCheck extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "班次ID，关联班次表(bs_schedule_bus) 长度(32)")
	private java.lang.String scheduleBusId;
	
	@ApiModelProperty(value = "出站时间 ")
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date outboundTime;
	
	@ApiModelProperty(value = "驾驶证检查 1合格 0不合格 ")
	private java.lang.Integer driverLicFlag;
	
	@ApiModelProperty(value = "从业资格证检查 1合格 0不合格 ")
	private java.lang.Integer qualificationCertFlag;
	
	@ApiModelProperty(value = "行驶证检查 ")
	private java.lang.Integer drivingLicFlag;
	
	@ApiModelProperty(value = "营运证检查 1合格 0不合格 ")
	private java.lang.Integer operationCertFlag;
	
	@ApiModelProperty(value = "线路标志牌检查 1合格 0不合格 ")
	private java.lang.Integer lineMarkFlag;
	
	@ApiModelProperty(value = "安检合格单检查 1合格 0不合格 ")
	private java.lang.Integer securityCheckFlag;
	
	@ApiModelProperty(value = "实载人数 ")
	private java.lang.Integer actualPassengerNum;
	
	@ApiModelProperty(value = "是否超员 1是 0否 ")
	private java.lang.Integer overloadFlag;
	
	@ApiModelProperty(value = "证件是否齐全检查 1是 0否 ")
	private java.lang.Integer allCertCompleteFlag;
	
	@ApiModelProperty(value = "是否系好安全带检查 1是 0否 ")
	private java.lang.Integer seatBeltFlag;
	
	@ApiModelProperty(value = "稽查补缴款 ")
	private java.lang.Float paymentFee;
	
	@ApiModelProperty(value = "补缴人数 ")
	private java.lang.Integer paymentPeople;
	
	@ApiModelProperty(value = "检查类型 1车辆出站 2稽查 ")
	private java.lang.Integer checkType;
	
	//扩展属性
	@TableField(exist=false)
	@ApiModelProperty(value = "发车日期 ")
	private java.lang.String runDate;
	
	@TableField(exist=false)
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "线路名称 长度(100) 必填")
	private java.lang.String lineName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "报班车车牌号 长度(20)")
	private java.lang.String reportCarNo;
	
	@TableField(exist=false)
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "报班时间 ")
	private java.util.Date reportTime;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "班次状态1正常 2停班 3被并/停班 4并班 5配载 6被配载 ")
	private java.lang.Integer busStatus;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "报班车站 长度(32)")
	private java.lang.String reportStaId;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "始发站（发车站）")
	private String stationName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "班次号")
	private java.lang.String busCode;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "儿童数 ")
	private java.lang.Integer children;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "打单人数")
	private java.lang.Integer orderNum;
}
