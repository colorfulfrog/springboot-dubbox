package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_line
 *  注释:线路表
 *  创建人: xjh
 *  创建日期:2018-7-10 14:54:42
 */
@Data
@TableName(value="bs_line")
public class Line extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "区域类别(0县内、1县际、2市际、3省际) ")
	private java.lang.Integer rangeType;
	
	@ApiModelProperty(value = "始发站编号 长度(32)")
	private java.lang.String startStateId;
	
	@ApiModelProperty(value = "终点站编号 长度(32)")
	private java.lang.String endStateId;
	
	@ApiModelProperty(value = "线路等级 一类、二类、三类、四类客运班线 ")
	private java.lang.Integer lineLevel;
	
	@ApiModelProperty(value = "线路方向（东、南、西、北、东北、东南、西南、西北） 长度(100)")
	private java.lang.String lineDirection;
	
	@ApiModelProperty(value = "线路名称 长度(100)")
	private java.lang.String lineName;
	
	@ApiModelProperty(value = "简拼 长度(50)")
	private java.lang.String spell;
	
	@ApiModelProperty(value = "线路里程 ")
	private java.lang.String lineMileage;
	
	@ApiModelProperty(value = "回程票有效天数 ")
	private java.lang.Integer ticketEffectiveDays;
	
	@ApiModelProperty(value = "是否支持小件快运 0是、1否 ")
	private java.lang.Integer smallExpressFlag;
	
	@ApiModelProperty(value = "线路别称 长度(100)")
	private java.lang.String lineAlias;
	
	@ApiModelProperty(value = "报班时间间隔 ")
	private java.lang.Integer intervalMinutes;
	
	@ApiModelProperty(value = "是否实名制购票0是、1否 ")
	private java.lang.Integer realNameTicketFlag;
	
	@ApiModelProperty(value = " 是否审核通过 0是、1否 ")
	private java.lang.Integer approveFlag;
	
	@ApiModelProperty(value = "0正常、1关闭 ")
	private java.lang.Integer status;
	
	
	/**
	 * 扩展属性
	 */
	@ApiModelProperty(value = "区域类别(县内、县际、市际、省际) ")
	@TableField(exist=false)
	private java.lang.String rangeTypeName;
	
	@ApiModelProperty(value = "所属单位名称")
	@TableField(exist=false)
	private java.lang.String fullName;
	
	@ApiModelProperty(value = "线路等级 一类、二类、三类、四类客运班线名称 ")
	@TableField(exist=false)
	private java.lang.String lineLevelName;
	
	@ApiModelProperty(value = "始发站名称")
	@TableField(exist=false)
	private java.lang.String startState;
	
	@ApiModelProperty(value = "终点站名称")
	@TableField(exist=false)
	private java.lang.String endState;
	
	
	
	
	
	
	
	
}
