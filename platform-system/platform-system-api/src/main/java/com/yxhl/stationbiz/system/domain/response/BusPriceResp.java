package com.yxhl.stationbiz.system.domain.response;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加票价列表返回对象
 * @author Administrator
 *
 */
@Data
public class BusPriceResp implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "班次模板id")
	private java.lang.String id;
    
    @ApiModelProperty(value = "票价id")
	private java.lang.String priceId;
	
	@ApiModelProperty(value = "核定座位数 ")
	private java.lang.Integer approvedSeats;
	
	@ApiModelProperty(value = "座位类型 0普通座，1商务座，2卧铺")
	private java.lang.Integer seatCategory;
	
	@ApiModelProperty(value = "上车站")
	@TableField(exist=false)
	private java.lang.String startName;
	
	@ApiModelProperty(value = "始发站编号 长度(32)")
	private java.lang.String startStateId;
	
	@ApiModelProperty(value = "终点站编号 长度(32)")
	private java.lang.String endStateId;
	
	@ApiModelProperty(value = "到达站")
	@TableField(exist=false)
	private java.lang.String endName;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "始发时间(时:分) ")
	private Date startTime;
	
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
}
