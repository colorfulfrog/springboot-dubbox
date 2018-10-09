package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;




import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_driver_vehicle
 *  注释:驾驶员车辆绑定表
 *  创建人: xjh
 *  创建日期:2018-7-9 17:39:14
 */
@Data
@TableName(value="bs_driver_vehicle")
public class DriverVehicle extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "驾驶员ID ")
	private String driverId;
	
	@ApiModelProperty(value = "车辆ID ")
	private String vehicleId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "创建人 名称")
	private String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改人 名称")
	private String xgrName;
	
	@TableField(exist = false)
	@ApiModelProperty("驾驶员姓名")
	private String driverName;
	
	@TableField(exist = false)
	@ApiModelProperty("驾驶员身份证")
	private String identityCard;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "车牌号 ")
	private java.lang.String carNo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "多个车辆ID ")
	private List<String> ListId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属单位id")
	private String compId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属单位 名称")
	private String fullName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "线路id")
	private String lineId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "线路 名称")
	private String lineName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "初次绑定车辆时间")
	private java.util.Date bindTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改绑定车辆时间")
	private java.util.Date updateBindTime;
	
}
