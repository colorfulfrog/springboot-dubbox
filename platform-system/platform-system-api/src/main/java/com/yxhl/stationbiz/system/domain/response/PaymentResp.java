package com.yxhl.stationbiz.system.domain.response;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 售票模块/缴款查询
 * @author xjh
 *
 */
@Data
public class PaymentResp implements Serializable{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 ")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 ")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "缴款类型(数据字典)： 1电脑票 2保险款 长度(2)")
	private java.lang.String paymentType;
	
	@ApiModelProperty(value = "售票员 长度(32)")
	private java.lang.String sellerUserId;
	
	@ApiModelProperty(value = "售票员姓名")
	private java.lang.String userName;
	
	@ApiModelProperty(value = "票款日期 ")
	private java.util.Date billingDate;
	
	@ApiModelProperty(value = "缴款起始票号")
	private java.lang.String startInsurance;
	
	@ApiModelProperty(value = "截止票号")
	private java.lang.String endInsurance;
	
	@ApiModelProperty(value = "本期应缴款")
	private java.lang.Float yjk;
	
	@ApiModelProperty(value = "售票数")
	private java.lang.Integer ticketNum;
	
	@ApiModelProperty(value = "售票金额 ")
	private java.lang.Float ticketFee;
	
	@ApiModelProperty(value = "退票数 ")
	private java.lang.Integer refundNum;
	
	@ApiModelProperty(value = "退款金额 ")
	private java.lang.Float refundFee;
	
	@ApiModelProperty(value = "退款手续费 ")
	private java.lang.Float refundCharge;
	
	@ApiModelProperty(value = "废票数 ")
	private java.lang.Integer scrapNum;
	
	@ApiModelProperty(value = "废票金额 ")
	private java.lang.Float scrapFee;
	
	@ApiModelProperty(value = "实缴款金额 ")
	private java.lang.Float paymentFee;
	
	@ApiModelProperty(value = "欠缴款金额 ")
	private java.lang.Float arrearsFee;
	
	@ApiModelProperty(value = "缴款时间 ")
	private java.util.Date paymentTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票员姓名")
	private java.lang.String sellerName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "开始时间")
	private java.lang.String startTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "结束时间")
	private java.lang.String endTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "是否已缴款完成  0否，1完成 ")
	private java.lang.Integer wcFlag;
	
}
