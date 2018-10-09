package com.yxhl.stationbiz.system.domain.entity.finance;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_ticket_seller_bill
 *  注释:售票员票据表
 *  创建人: ypf
 *  创建日期:2018-9-12 11:45:23
 */
@Data
@TableName(value="bs_ticket_seller_bill")
public class TicketSellerBill extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "领用人 长度(32)")
	private java.lang.String receiveUser;
	
	@ApiModelProperty(value = "票据类型(数据字典) 1电脑票 2保险票 3税务收据 4电脑三联单 5手工三联单 长度(2)")
	private java.lang.String billType;
	
	@ApiModelProperty(value = "开始号 长度(20)")
	private java.lang.String startNum;
	
	@ApiModelProperty(value = "结束号 长度(20)")
	private java.lang.String endNum;
	
	@ApiModelProperty(value = "当前票号 长度(20)")
	private java.lang.String currentNum;
	
	@ApiModelProperty(value = "发票人 长度(32)")
	private java.lang.String grantUser;
	
	@ApiModelProperty(value = "每卷票数 ")
	private java.lang.Integer rollCount;
	
	@ApiModelProperty(value = "该段票数 ")
	private java.lang.Integer sectionCount;
	
	@ApiModelProperty(value = "剩余票数 ")
	private java.lang.Integer remainCount;
	
	@ApiModelProperty(value = "票源单位(数据字典) 电脑票:1税务局 2交通印刷厂;保险票:1平安保险 2太保寿险 3吉祥人寿 长度(2)")
	private java.lang.String ticketSource;
	
	@ApiModelProperty(value = "状态(数据字典)  0未启用 1启用 2用完 长度(2)")
	private java.lang.String status;
	
	@ApiModelProperty(value = "公司票据父ID 长度(32)")
	private java.lang.String billEntryId;
	
	@TableField(exist=false)
	private String receiveName;
	
	@TableField(exist=false)
	private String grantName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "重复当前票号 ")
	private String reCurrentNum;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "保险当前票号 ")
	private String inCurrentNum;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "重复当前保险票号 ")
	private String reInCurrentNum;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "保险id ")
	private String inId;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "保险剩余票数 ")
	private java.lang.Integer inRemainCount;
	
}
