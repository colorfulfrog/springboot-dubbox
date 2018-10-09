package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_station	
 *  注释:站点表
 *  创建人: xjh
 *  创建日期:2018-7-12 11:45:24
 */
@Data
@TableName(value="bs_station")
public class Station extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "站点名称 长度(100)")
	private java.lang.String stationName;
	
	@ApiModelProperty(value = "所属区域 长度(50)")
	private java.lang.String regionCode;
	
	@ApiModelProperty(value = "车站级别 长度(50)")
	private java.lang.String stationLevel;
	
	@ApiModelProperty(value = "操作码 长度(50)")
	private java.lang.String oprCode;
	
	@ApiModelProperty(value = "简拼 长度(50)")
	private java.lang.String spell;
	
	@ApiModelProperty(value = "公里数 ")
	private java.lang.Long mileage;
	
	@ApiModelProperty(value = "是否上车点 0是、1否 ")
	private java.lang.Integer boardPointFlag;
	
	@ApiModelProperty(value = "是否售往返车票0是、1否 ")
	private java.lang.Integer returnTicketFlag;
	
	@ApiModelProperty(value = "联系人 长度(50)")
	private java.lang.String contacts;
	
	@ApiModelProperty(value = "联系电话 长度(50)")
	private java.lang.String telephone;
	
	@ApiModelProperty(value = "是否是快递服务站0否、1县级中转站、2家村服务站 ")
	private java.lang.Integer serviceStationFlag;
	
	@ApiModelProperty(value = "别称 长度(50)")
	private java.lang.String alias;
	
	@ApiModelProperty(value = "0正常、1关闭 ")
	private java.lang.Integer status;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属区域 名称")
	private String regionName;
	
	@ApiModelProperty(value = "站点编码 ")
	private java.lang.String stationCode;
	
	@TableField(exist = false)
	private String fullName;
	
}
