package com.yxhl.stationbiz.system.domain.entity.security;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_vehicle_security_check
 *  注释:车辆安检表
 *  创建人: xjh
 *  创建日期:2018-8-13 17:04:26
 */
@Data
@TableName(value="bs_vehicle_security_check")
public class VehicleSecurityCheck extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "安检车站ID 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "安检车辆ID 长度(32)")
	private java.lang.String vehicleId;
	
	@ApiModelProperty(value = "外观检查结果 1合格；0不合格 长度(2)")
	private java.lang.String exteriorFlag;
	
	@ApiModelProperty(value = "外观检查不合格原因 长度(200)")
	private java.lang.String exteriorReason;
	
	@ApiModelProperty(value = "转向系统检查结果 1合格；0不合格 长度(2)")
	private java.lang.String steeringSysFlag;
	
	@ApiModelProperty(value = "转向系统检查不合格原因 长度(200)")
	private java.lang.String steeringSysReason;
	
	@ApiModelProperty(value = "制动系统检查结果 1合格；0不合格 长度(2)")
	private java.lang.String brakeSysFlag;
	
	@ApiModelProperty(value = "制动系统检查不合格原因 长度(200)")
	private java.lang.String brakeSysReason;
	
	@ApiModelProperty(value = "传动系统检查结果 1合格；0不合格 长度(2)")
	private java.lang.String transSysFlag;
	
	@ApiModelProperty(value = "传动系统检查不合格原因 长度(200)")
	private java.lang.String transSysReason;
	
	@ApiModelProperty(value = "悬架系统检查结果 1合格；0不合格 长度(2)")
	private java.lang.String suspensionSysFlag;
	
	@ApiModelProperty(value = "悬架系统检查不合格原因 长度(200)")
	private java.lang.String suspensionSysReason;
	
	@ApiModelProperty(value = "轮胎检查结果 1合格；0不合格 长度(2)")
	private java.lang.String tyreFlag;
	
	@ApiModelProperty(value = "轮胎检查不合格原因 长度(200)")
	private java.lang.String tyreReason;
	
	@ApiModelProperty(value = "灯光检查结果 1合格；0不合格 长度(2)")
	private java.lang.String lightsFlag;
	
	@ApiModelProperty(value = "灯光检查不合格原因 长度(200)")
	private java.lang.String lightsReason;
	
	@ApiModelProperty(value = "安全设施检查结果 1合格；0不合格 长度(2)")
	private java.lang.String safetyFlag;
	
	@ApiModelProperty(value = "安全设施检查不合格原因 长度(200)")
	private java.lang.String safetyReason;
	
	@ApiModelProperty(value = "其他部分检查结果 1合格；0不合格 长度(2)")
	private java.lang.String othersFlag;
	
	@ApiModelProperty(value = "其他部分检查不合格原因 长度(200)")
	private java.lang.String othersReason;
	
	@ApiModelProperty(value = "证件检查结果 1合格；0不合格 长度(2)")
	private java.lang.String certificateFlag;
	
	@ApiModelProperty(value = "证件检查不合格原因 长度(200)")
	private java.lang.String certificateReason;
	
	@ApiModelProperty(value = "GPS检查结果 1合格；0不合格 长度(2)")
	private java.lang.String gpsFlag;
	
	@ApiModelProperty(value = "GPS检查不合格原因 长度(200)")
	private java.lang.String gpsReason;
	
	@ApiModelProperty(value = "回程检查结果 1合格；0不合格 长度(2)")
	private java.lang.String returnFlag;
	
	@ApiModelProperty(value = "回程检查不合格原因 长度(200)")
	private java.lang.String returnReason;
	
	@ApiModelProperty(value = "安检日期 ")
	private java.util.Date checkDate;
	
	@ApiModelProperty(value = "安检结果 1合格，0不合格 长度(2)")
	private java.lang.String result;
	
	@ApiModelProperty(value = "安检员ID 长度(32)")
	private java.lang.String checkerId;
	
	@ApiModelProperty(value = "安检费 ")
	private java.lang.Float checkFee;
	
	@ApiModelProperty(value = "安检类型 1日检 长度(2)")
	private java.lang.String checkType;
	
	@ApiModelProperty(value = "是否安检明天车辆 1是；0否 长度(2)")
	private java.lang.String checkTomoVehicleFlag;
	
	@ApiModelProperty(value = "驾驶员Id 长度(32)")
	private java.lang.String driverId;
	
	@ApiModelProperty(value = "备注 长度(200)")
	private java.lang.String remark;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "安检开始日期")
	private java.lang.String startDate;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "安检结束日期")
	private java.lang.String endDate;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "安检员姓名")
	private java.lang.String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "燃料类型 0汽油、1柴油、2天然气，3电动、4油电混合、5油气混合")
	private java.lang.Integer fuelType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "营运类型 0本站公营车，1外站公营车，2本站承包车，3外站承包车")
	private java.lang.Integer operationType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "车牌号 ")
	private java.lang.String carNo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "营运证号 ")
	private java.lang.String opeCertNo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "线路 名称")
	private String lineName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "驾驶员姓名 长度(32)")
	private java.lang.String driverName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "从业资格证号")
	private java.lang.String qualificationCertNo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "站点名称")
	private java.lang.String stationName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "车所属单位 名称")
	private String fullName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属线路 01")
	private String allCompLines;
}
