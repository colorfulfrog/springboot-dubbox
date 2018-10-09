package com.yxhl.stationbiz.system.domain.entity.security;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_vehicle_instation
 *  注释:车辆进站表
 *  创建人: ypf
 *  创建日期:2018-8-13 16:52:37
 */
@Data
@TableName(value="bs_vehicle_instation")
public class VehicleInstation extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "参运车站ID 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "参运车辆ID 长度(32)")
	private java.lang.String vehicleId;
	
	@ApiModelProperty(value = "营运线路ID 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "进站期起 ")
	private java.util.Date beginDate;
	
	@ApiModelProperty(value = "进站期止 ")
	private java.util.Date endDate;
	
	@ApiModelProperty(value = "临时参运 1是 0否 长度(2)")
	private java.lang.String isTemp;
	
	@ApiModelProperty(value = "进站牌证号 长度(32)")
	private java.lang.String licNum;
	
	@ApiModelProperty(value = "开单方案 长度(2)")
	private java.lang.String billPlan;
	
	@TableField(exist=false)
	private String stationName;
	
	@TableField(exist=false)
	private String carNo;
	
	@TableField(exist=false)
	private String fullName;
	
	@TableField(exist=false)
	private String lineName;
	
	@TableField(exist=false)
	private Integer approvedSeats;
	
	@TableField(exist=false)
	private Integer seatCategory;
}
