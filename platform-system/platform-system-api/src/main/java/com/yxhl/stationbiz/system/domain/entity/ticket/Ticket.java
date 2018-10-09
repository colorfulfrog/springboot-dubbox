package com.yxhl.stationbiz.system.domain.entity.ticket;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_ticket
 *  注释:售票表
 *  创建人: lw
 *  创建日期:2018-9-14 10:43:42
 */
@Data
@TableName(value="bs_ticket")
public class Ticket extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 ")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 ")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "所属订单 长度(32)")
	private java.lang.String orderId;
	
	@ApiModelProperty(value = "乘车人姓名 长度(32)")
	private java.lang.String name;
	
	@ApiModelProperty(value = "女，男 长度(2)")
	private java.lang.String sex;
	
	@ApiModelProperty(value = "身份证号码")
	private java.lang.String idCardNo;
	
	@ApiModelProperty(value = "电话号码 长度(15)")
	private java.lang.String mobile;
	
	@ApiModelProperty(value = "票价 ")
	private java.lang.Float price;
	
	@ApiModelProperty(value = "所属班次 长度(32)")
	private java.lang.String checkScheduleBusId;
	
	@ApiModelProperty(value = "所属班次 长度(32)")
	private java.lang.String scheduleBusId;
	
	@ApiModelProperty(value = "所属座位号id 长度(32)")
	private java.lang.String seatId;
	
	@ApiModelProperty(value = "保险公司 长度(32)")
	private java.lang.String insuranceCompany;
	
	@ApiModelProperty(value = "保险票号 长度(32)")
	private java.lang.String insuranceCode;
	
	@ApiModelProperty(value = "保险费用 ")
	private java.lang.Float insuranceCost;
	
	@ApiModelProperty(value = "保险状态 1正常 2作废 3退保 长度(2)")
	private java.lang.String insuranceStatus;
	
	@ApiModelProperty(value = "发票号码 长度(32)")
	private java.lang.String invoiceNo;
	
	@ApiModelProperty(value = "所属票种 长度(32)")
	private java.lang.String ticketCategoryId;
	
	@ApiModelProperty(value = "支付状态（1未支付  2已支付 ） ")
	private java.lang.Integer payStatus;
	
	@ApiModelProperty(value = "检票口（名称） 长度(32)")
	private java.lang.String ticketCheck;
	
	@ApiModelProperty(value = "乘车库(名称) 长度(32)")
	private java.lang.String busGarage;
	
	@ApiModelProperty(value = "售票点 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "0 否，1是 ")
	private java.lang.Integer netTicketFlag;
	
	@ApiModelProperty(value = "互联网名称 长度(32)")
	private java.lang.String netName;
	
	@ApiModelProperty(value = "0未取票 1已取票 ")
	private java.lang.Integer takeTicketFlag;
	
	@ApiModelProperty(value = "取票时间 ")
	private java.util.Date takeTicketDate;
	
	@ApiModelProperty(value = "检票时间 ")
	private java.util.Date checkDate;
	
	@ApiModelProperty(value = "检票状态 0未检  1已检 ")
	private java.lang.Integer checkStatus;
	
	@ApiModelProperty(value = "检票员 长度(32)")
	private java.lang.String checkUserId;
	
	@ApiModelProperty(value = "退款金额 ")
	private java.lang.Float refundFee;
	
	@ApiModelProperty(value = "退款手续费 ")
	private java.lang.Float refundCharge;
	
	@ApiModelProperty(value = "票状态（0锁票 1正常 2、废票 3、退票） 长度(10)")
	private java.lang.String status;
	
	@ApiModelProperty(value = "退款时间 /废票时间")
	private java.util.Date refundDate;
	
	@ApiModelProperty(value = "0窗口、1网络、2自助 ")
	private java.lang.Integer sellerType;
	
	@ApiModelProperty(value = "售票员 长度(32)")
	private java.lang.String sellerUserId;
	
	@ApiModelProperty(value = "备注 长度(500)")
	private java.lang.String remark;
	
	@ApiModelProperty(value = "条码 长度(32)")
	private java.lang.String barCode;
	
	@ApiModelProperty(value = "燃油费 ")
	private java.lang.Float fuelFee;
	
	@ApiModelProperty(value = "退票员/废票员")
	private java.lang.String refundUserId;
	
	
	//班次扩展属性
	@TableField(exist = false)
	@ApiModelProperty(value = "售票员姓名")
	private java.lang.String sellerName;
	
	//班次扩展属性
	@TableField(exist = false)
	@ApiModelProperty(value = "退票员/废票员姓名")
	private java.lang.String refundName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "线路名称")
	private java.lang.String lineName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发车日期")
	private java.util.Date runDate;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "车辆类型")
	private java.lang.String vehicleType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "班次号")
	private java.lang.String busCode;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "车牌号码")
	private java.lang.String reportCarNo;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "始发地")
	private java.lang.String start;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "目的地")
	private java.lang.String destination;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "总金额")
	private java.lang.String totalFee;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票点名称")
	private java.lang.String stationName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "票种名称")
	private java.lang.String ticketCateName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "工号")
	private java.lang.String employId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "退票员/废票员工号")
	private java.lang.String tpEmployId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票开始时间")
	private java.lang.String spStartTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票结束时间")
	private java.lang.String spEndTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发车开始时间")
	private java.lang.String fcStartTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发车结束时间")
	private java.lang.String fcEndTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "退款时间 /废票时间开始时间")
	private java.lang.String refundStartTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "退款时间 /废票时间结束时间")
	private java.lang.String refundEndTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票类型 0固定班售票 1固定班补票 2流水班补票 3退票")
	private java.lang.String sellType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票类型 固定单售票 ")
	private java.lang.String gdbspFlag;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票类型  固定单补票 ")
	private java.lang.String gdbbpFlag;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票类型 流水班补票 ")
	private java.lang.String lsbbpFlag;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "售票类型  退票")
	private java.lang.String tpFlag;
	
	@ApiModelProperty(value = "废票原因 ")
	private java.lang.String abolishReason;
	
	@ApiModelProperty(value = "退票原因  0乘客原因 1车站")
	private java.lang.String refundReason;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "座位号")
	private java.lang.String seatNum;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "身份证类型")
	private java.lang.String cardtype;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "携童数")
	private java.lang.String children;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发车站")
	private java.lang.String startStation;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发班状态 1已发班 0未发班 2班次完成")
	private java.lang.String runStatus;
	
	@TableField(exist = false)
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "费率")
	private java.lang.String fl;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "意外伤害")
	private java.lang.String ywsh;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "意外医疗")
	private java.lang.String ywyl;
	@TableField(exist = false)
	@ApiModelProperty(value = "CASH(现金)、WECHAT(微信)、ALIPAY(支付宝)")
	private java.lang.String outPayType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "班次类型 1固定班 2流水班 ")
	private java.lang.Integer busType;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "发班时间（司机发车时间）")
	private java.util.Date realRunTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "退票选择 1按票号  2按日期  3退保险")
	private java.lang.String type;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "退款总金额 ")
	private java.lang.Float refundFeeSum;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "全票数量 ")
	private java.lang.Integer qpNum;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "折扣票数量 ")
	private java.lang.Integer zkNum;
}
