package com.yxhl.stationbiz.system.domain.entity.sys;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:sys_relation
 *  注释:用户角色、角色菜单关联表
 *  创建人: xjh
 *  创建日期:2018-7-18 9:56:13
 */
@Data
@TableName(value="sys_relation")
public class Relation extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "关联对象ID 长度(32) 必填")
	private java.lang.String aid;
	
	@ApiModelProperty(value = "被关联对象ID 长度(32) 必填")
	private java.lang.String bid;
	
	@ApiModelProperty(value = "关联类型 1 用户-角色；2 角色-菜单 长度()")
	private java.lang.String type;
	
	@TableField(exist=false)
	private List<String> bids;
	
	@TableField(exist=false)
	private java.lang.String uid;
	
}
