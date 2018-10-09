package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_bus_entrance
 *  注释:乘车库表
 *  创建人: lw
 *  创建日期:2018-7-10 9:48:43
 */
@Data
@TableName(value="bs_bus_entrance")
public class BusEntrance extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "所属站点 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "车库位置 长度(20)")
	private java.lang.String entranceName;
	
	@ApiModelProperty(value = "打印名称 长度(20)")
	private java.lang.String printEntranceName;
	
	@ApiModelProperty(value = "描述 长度(100)")
	private java.lang.String remark;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "所属站点名称")
	private java.lang.String stationName;
}
