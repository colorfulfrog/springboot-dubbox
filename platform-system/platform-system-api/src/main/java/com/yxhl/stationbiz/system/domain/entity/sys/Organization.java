package com.yxhl.stationbiz.system.domain.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:sys_organization
 *  注释:机构表
 *  创建人: xjh
 *  创建日期:2018-7-12 15:58:15
 */
@Data
@TableName(value="sys_organization")
public class Organization extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "编码 长度(10) 必填")
	private java.lang.String orgCode;
	
	@ApiModelProperty(value = "机构简称 长度(20) 必填")
	private java.lang.String shortName;
	
	@ApiModelProperty(value = "机构全称 长度(64) 必填")
	private java.lang.String fullName;
	
	@ApiModelProperty(value = "简拼 长度(10) 必填")
	private java.lang.String shortSpell;
	
	@ApiModelProperty(value = "所属区域 长度(32)")
	private java.lang.String regId;
	
	@ApiModelProperty(value = "联系人 长度(20)")
	private java.lang.String contact;
	
	@ApiModelProperty(value = "联系电话 长度(20)")
	private java.lang.String telephone;
	
	@ApiModelProperty(value = "附件 Url")
	private java.lang.String attachmentUrl;
	
	@ApiModelProperty(value = "附件名称")
	private java.lang.String attachmentName;
	
	@ApiModelProperty(value = "有效期开始时间 ")
	private java.util.Date startTime;
	
	@ApiModelProperty(value = "有效期结束时间 ")
	private java.util.Date endTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属区域 名称")
	private String regionName;
	
}
