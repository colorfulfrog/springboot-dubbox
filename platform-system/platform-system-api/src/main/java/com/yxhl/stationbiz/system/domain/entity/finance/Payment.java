package com.yxhl.stationbiz.system.domain.entity.finance;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_payment
 *  注释:缴款表
 *  创建人: xjh
 *  创建日期:2018-9-14 11:26:25
 */
@Data
@TableName(value="bs_payment")
public class Payment extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "车站 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "售票点ID,目前也取车站ID 长度(32)")
	private java.lang.String ticketOfficeId;
	
	@ApiModelProperty(value = "缴款类型(数据字典)： 1电脑票 2保险款 长度(2)")
	private java.lang.String paymentType;
	
	@ApiModelProperty(value = "缴款时间 ")
	private java.util.Date paymentTime;
	
	@ApiModelProperty(value = "缴款金额 ")
	private java.lang.Float paymentFee;
	
	@ApiModelProperty(value = "缴款人 长度(32)")
	private java.lang.String payer;
	
	@ApiModelProperty(value = "收款人 长度(32)")
	private java.lang.String payee;
	
	@ApiModelProperty(value = "记账日期 ")
	private java.util.Date billingDate;
	
	@ApiModelProperty(value = "缴款单号 ")
	private java.lang.String paymentNum;
	
	@ApiModelProperty(value = "状态 1正常 0取消 ")
	private java.lang.Integer paymentStatus;
	
	@ApiModelProperty(value = "备注 长度(200)")
	private java.lang.String remark;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "开始时间")
	private java.lang.String startTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "结束时间")
	private java.lang.String endTime;
	
	@TableField(exist = false) 
	@ApiModelProperty(value = "缴款人")
	private java.lang.String payerName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "收款人")
	private java.lang.String payeeName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属站名称")
	private java.lang.String stationName;
	
}
