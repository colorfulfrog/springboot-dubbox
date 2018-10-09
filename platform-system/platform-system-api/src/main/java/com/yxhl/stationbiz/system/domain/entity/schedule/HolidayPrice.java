package com.yxhl.stationbiz.system.domain.entity.schedule;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_holiday_price
 *  注释:节假日票价表
 *  创建人: ypf
 *  创建日期:2018-8-15 14:20:34
 */
@Data
@TableName(value="bs_holiday_price")
public class HolidayPrice extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "节假日ID 长度(32)")
	private java.lang.String holidayId;
	
	@ApiModelProperty(value = "班次模板ID 长度(32)")
	private java.lang.String scheduleTplId;
	
	@ApiModelProperty(value = "上车站 长度(32)")
	private java.lang.String onStaId;
	
	@ApiModelProperty(value = "下车站 长度(32)")
	private java.lang.String offStaId;
	
	@ApiModelProperty(value = "座位类型 0普通 1商务 2卧铺 长度(2)")
	private java.lang.String seatCate;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@ApiModelProperty(value = "座位号 0全部 其他数字表示具体座位号 ")
	private java.lang.Integer seatNo;
	
	@TableField(exist=false)
	private String startName;
	
	@TableField(exist=false)
	private String endName;
	
	@TableField(exist=false)
	private Date beginDate;
	
	@TableField(exist=false)
	private Date endDate;
	
	@TableField(exist=false)
	private String holidayName;
	
	@TableField(exist=false)
	private List<HolidayPriceResponse> newRuleForm;
	
}
