package com.yxhl.stationbiz.system.domain.entity.sys;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色管理
 * @author ypf
 *
 */
@Data
@TableName(value="sys_role")
public class Role extends ELItem{
	
	@ApiModelProperty("角色名称")
	private String roleName;
	
	@ApiModelProperty("所属机构")
	private String orgId;
	
	@ApiModelProperty("角色类型：1 快递业务角色 2 普通站务角色")
	private Integer roleType;
	
	@ApiModelProperty("备注")
	private String remark;
	
	@ApiModelProperty("角色标识，通过该字段识别角色")
	private String roleCode;
	
	@TableField(exist=false)
	private String orgName;
	
	@TableField(exist=false)
	private String createName;
	
	@ApiModelProperty("授权人")
	private String authorizer;
	
    @JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("授权时间")
	private Date authorizeTime;
    
    @TableField(exist=false)
	private String authorizerName;
	
}
