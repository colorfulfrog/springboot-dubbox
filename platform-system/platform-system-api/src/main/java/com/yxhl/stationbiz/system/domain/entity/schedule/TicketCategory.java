package com.yxhl.stationbiz.system.domain.entity.schedule;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_ticket_category
 *  注释:票种设置表
 *  创建人: xjh
 *  创建日期:2018-8-16 10:25:40
 */
@Data
@TableName(value="bs_ticket_category")
public class TicketCategory extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "票种名称 长度(32)")
	private java.lang.String ticketCateName;
	
	@ApiModelProperty(value = "票种代码 长度(32)")
	private java.lang.String ticketCateCode;
	
	@ApiModelProperty(value = "字段名称 长度(32)")
	private java.lang.String fieldName;
	
	@ApiModelProperty(value = "显示图标 长度(200)")
	private java.lang.String icon;
	
	@ApiModelProperty(value = "打印名称 长度(32)")
	private java.lang.String printName;
	
	@ApiModelProperty(value = "是否默认票种 1是；0否 长度(2)")
	private java.lang.String isDefault;
	
	@ApiModelProperty(value = "是否参与积分 1参与；0不参与 长度(2)")
	private java.lang.String pointFlag;
	
}
