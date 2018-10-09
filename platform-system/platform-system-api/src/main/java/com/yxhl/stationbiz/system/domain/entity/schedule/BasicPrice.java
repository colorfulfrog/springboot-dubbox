package com.yxhl.stationbiz.system.domain.entity.schedule;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_basic_price
 *  注释:基础票价表
 *  创建人: xjh
 *  创建日期:2018-8-20 10:53:51
 */
@Data
@TableName(value="bs_basic_price")
public class BasicPrice extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "班次模板ID 长度(32)")
	private java.lang.String scheduleTplId;
	
	@ApiModelProperty(value = "票价id")
	@TableField(exist=false)
	private java.lang.String priceId;
	
	@ApiModelProperty(value = "上车站 长度(32)")
	private java.lang.String onStaId;
	
	@ApiModelProperty(value = "下车站 长度(32)")
	private java.lang.String offStaId;
	
	@ApiModelProperty(value = "上车站名称")
	@TableField(exist=false)
	private java.lang.String startName;
	
	@ApiModelProperty(value = "到达站名称")
	@TableField(exist=false)
	private java.lang.String endName;
	
	@ApiModelProperty(value = "座位类型 0普通 1商务 2卧铺 长度(2)")
	private java.lang.String seatCate;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@ApiModelProperty(value = "座位号 0全部 其他数字表示具体座位号 ")
	private java.lang.Integer seatNo;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "始发时间(时:分) ")
	@TableField(exist=false)
	private Date startTime;
	
	@ApiModelProperty(value = "标识 ")
	@TableField(exist=false)
	private java.lang.String rowId;
	
	@ApiModelProperty(value = "线路id")
	@TableField(exist=false)
	private String lineId;
	
	@ApiModelProperty(value = "加班班次0否  1是 ")
	@TableField(exist=false)
	private java.lang.Integer overtimeBusFlag;
	
	@ApiModelProperty(value = "正班班次0否  1是 ")
	@TableField(exist=false)
	private java.lang.Integer overtimeBusFlag2;
}
