package com.yxhl.stationbiz.system.domain.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:sys_dictionary
 *  注释:
 *  创建人: xjh
 *  创建日期:2018-7-10 15:37:54
 */
@Data
@TableName(value="sys_dictionary")
public class Dictionary extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "key 长度(156) 必填")
	private java.lang.String configKey;
	
	@ApiModelProperty(value = "内容名称 长度(200)")
	private java.lang.String keyName;
	
	@ApiModelProperty(value = "内容值 长度(1024)")
	private java.lang.String value;
	
	@ApiModelProperty(value = "配置类型 长度(256) 必填")
	private java.lang.String type;
	
	@ApiModelProperty(value = "乐观锁版本 ")
	private java.lang.Integer version;
	
	@ApiModelProperty(value = "描述 长度(500)")
	private java.lang.String description;
	
	@ApiModelProperty(value = "1可见 2不可见 ")
	private java.lang.Integer visible;
	
	@ApiModelProperty(value = "是否多项 1单项 2多项 ")
	private java.lang.Integer multiFlag;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "创建人 名称")
	private String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改人 名称")
	private String xgrName;
	
}
