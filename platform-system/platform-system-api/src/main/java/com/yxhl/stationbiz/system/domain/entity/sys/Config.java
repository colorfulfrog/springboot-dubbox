package com.yxhl.stationbiz.system.domain.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:sys_config
 *  注释:参数配置表
 *  创建人: xjh
 *  创建日期:2018-7-12 16:14:50
 */
@Data
@TableName(value="sys_config")
public class Config extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32) 必填")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "参数类型： 1 售票，2补票 ")
	private java.lang.Integer type;
	
	@ApiModelProperty(value = "参数编码 长度(10) 必填")
	private java.lang.String code;
	
	@ApiModelProperty(value = "参数取值 长度(10) 必填")
	private java.lang.String value;
	
	@ApiModelProperty(value = "备注 长度(200) 必填")
	private java.lang.String remark;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属机构名称")
	private String shortName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "创建人 名称")
	private String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改人 名称")
	private String xgrName;
	
}
