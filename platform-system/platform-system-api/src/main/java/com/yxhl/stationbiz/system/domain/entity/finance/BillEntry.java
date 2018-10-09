package com.yxhl.stationbiz.system.domain.entity.finance;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELTreeItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_bill_entry
 *  注释:票据录入表
 *  创建人: ypf
 *  创建日期:2018-9-12 11:16:57
 */
@Data
@TableName(value="bs_bill_entry")
public class BillEntry extends ELTreeItem<BillEntry>{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "票据类型(数据字典) 1电脑票 2保险票 3税务收据 4电脑三联单 5手工三联单 长度(2)")
	private java.lang.String billType;
	
	@ApiModelProperty(value = "开始号 长度(20)")
	private java.lang.String startNum;
	
	@ApiModelProperty(value = "结束号 长度(20)")
	private java.lang.String endNum;
	
	@ApiModelProperty(value = "每卷票数 ")
	private java.lang.Integer rollCount;
	
	@ApiModelProperty(value = "每张票成本 ")
	private java.lang.Float cost;
	
	@ApiModelProperty(value = "该段票数 ")
	private java.lang.Integer sectionCount;
	
	@ApiModelProperty(value = "剩余票数 ")
	private java.lang.Integer remainCount;
	
	@ApiModelProperty(value = "票源单位(数据字典) 电脑票:1税务局 2交通印刷厂;保险票:1平安保险 2太保寿险 3吉祥人寿 长度(2)")
	private java.lang.String ticketSource;
	
	@ApiModelProperty(value = "领用人 长度(32)")
	private java.lang.String receiveUser;
	
	@ApiModelProperty(value = "状态(数据字典)  0作废 1正常 2用完 长度(2)")
	private java.lang.String status;
	
	@TableField(exist=false)
	private String receiveName;
	
	@TableField(exist=false)
	private String employId;
	
}
