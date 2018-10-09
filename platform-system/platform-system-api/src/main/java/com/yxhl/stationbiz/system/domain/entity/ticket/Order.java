package com.yxhl.stationbiz.system.domain.entity.ticket;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_order
 *  注释:订单表
 *  创建人: lw
 *  创建日期:2018-9-15 10:39:27
 */
@Data
@TableName(value="bs_order")
public class Order extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "订单编号 长度(32)")
	private java.lang.String orderCode;
	
	@ApiModelProperty(value = "所属机构 ")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 ")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "所属班次 长度(32)")
	private java.lang.String scheduleBusId;
	
	@ApiModelProperty(value = "用户编号 长度(32)")
	private java.lang.String userId;
	
	@ApiModelProperty(value = "手机号码 长度(11)")
	private java.lang.String mobile;
	
	@ApiModelProperty(value = "班次号 长度(20)")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "出发地名称 长度(32)")
	private java.lang.String start;
	
	@ApiModelProperty(value = "目的地名称 长度(32)")
	private java.lang.String destination;
	
	@ApiModelProperty(value = "出发站点编码 长度(32)")
	private java.lang.String startStationId;
	
	@ApiModelProperty(value = "目的地站点编码 长度(32)")
	private java.lang.String endStationId;
	
	@ApiModelProperty(value = "携童数 ")
	private java.lang.Integer children;
	
	@ApiModelProperty(value = "人数 ")
	private java.lang.Integer amount;
	
	@ApiModelProperty(value = "座位号,多张票如（23,24） 长度(32)")
	private java.lang.String seatNo;
	
	@ApiModelProperty(value = "取票码 长度(10)")
	private java.lang.String verifyCode;
	
	@ApiModelProperty(value = "支付总金额 ")
	private java.lang.Float totalFee;
	
	@ApiModelProperty(value = "拆扣费用 ")
	private java.lang.Float discountFee;
	
	@ApiModelProperty(value = "技术服务费 ")
	private java.lang.Float serviceFee;
	
	@ApiModelProperty(value = "退款金额 ")
	private java.lang.Float refundFee;
	
	@ApiModelProperty(value = "退款手续费 ")
	private java.lang.Float refundCharge;
	
	@ApiModelProperty(value = "退款状态 长度(10)")
	private java.lang.String refundStatus;
	
	@ApiModelProperty(value = "退款时间 ")
	private java.util.Date refundDate;
	
	@ApiModelProperty(value = "业务类型 1 汽车大巴 长度(10)")
	private java.lang.String bizType;
	
	@ApiModelProperty(value = "支付状态（1已下单  2已支付 3取消 ） ")
	private java.lang.Integer payStatus;
	
	@ApiModelProperty(value = "支付时间 ")
	private java.util.Date payDate;
	
	@ApiModelProperty(value = "支付(平台)订单号 长度(32)")
	private java.lang.String outPayNo;
	
	@ApiModelProperty(value = "CASH(现金)、WECHAT(微信)、ALIPAY(支付宝) 长度(32)")
	private java.lang.String outPayType;
	
	@ApiModelProperty(value = "支付平台商户id 长度(32)")
	private java.lang.String paySellerId;
	
	@ApiModelProperty(value = "乘客支付id 长度(32)")
	private java.lang.String payBuyerId;
	
	@ApiModelProperty(value = "购票失败原因 长度(100)")
	private java.lang.String failReason;
	
	@ApiModelProperty(value = "版本号 ")
	private java.lang.Integer version;
	
	@ApiModelProperty(value = "扩展属性：存放出发地目的地（经纬度）等 长度(500)")
	private java.lang.String attributes;
	
	@ApiModelProperty(value = "0窗口、1网络、2自助 ")
	private java.lang.Integer sellWay;
	
	@ApiModelProperty(value = "售票类型 0固定单售票 1固定单补票 2流水班补票 3退票 ")
	private java.lang.Integer sellType;
	
	@ApiModelProperty(value = "备注 长度(500)")
	private java.lang.String remark;
	
}
