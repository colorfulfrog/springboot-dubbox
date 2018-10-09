package com.yxhl.stationbiz.system.domain.entity.schedule;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_ticket_cate_value
 *  注释:票种取值表
 *  创建人: ypf
 *  创建日期:2018-8-15 14:12:46
 */
@Data
@TableName(value="bs_ticket_cate_value")
public class TicketCateValue extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "票种ID，关联票种配置表主键 长度(32)")
	private java.lang.String ticketCateId;
	
	@ApiModelProperty(value = "票价表ID，根据票价表类型1基础票价表 2节日票价表 3执行票价表分别存对应表ID 长度(32)")
	private java.lang.String priceId;
	
	@ApiModelProperty(value = "票价表类型 1基础票价表 2节日票价表 3执行票价表 长度(2)")
	private java.lang.String priceTblType;
	
	@ApiModelProperty(value = "票价取值 ")
	private java.lang.Float ticketValue;
	
	@TableField(exist=false)
	private String ticketCateName;
	
	@TableField(exist=false)
	private String fieldName;
	
	@TableField(exist=false)
	private String tableName;
	
	@TableField(exist=false)
	List<String> ids;
	
	@ApiModelProperty(value = "购买的票种数量 ")
	@TableField(exist=false)
	private Integer num;
	
}
