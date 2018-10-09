package com.yxhl.stationbiz.system.domain.entity.sys;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value="sys_company")
public class Company extends ELItem{

	
	@ApiModelProperty("编码")
	private String companyCode;
	
	@ApiModelProperty("单位简称")
	private String shortName;

	@ApiModelProperty("单位全称")
	private String fullName;
	
	@ApiModelProperty("简拼")
	private String shortSpell;
	
	@ApiModelProperty("所属区域")
	private String areaId;
	
	@ApiModelProperty("所属机构")
	private String orgId;
	
	@ApiModelProperty("联系人")
	private String contact;
	
	@ApiModelProperty("联系电话")
	private String telephone;
	
	@ApiModelProperty("单位类型: 1 运输公司 2 站场公司")
	private Integer type;
	
	@TableField(exist=false)
	private String createName;
	
	@TableField(exist=false)
	private String areaName;
	
	@TableField(exist=false)
	private String updateName;
	
	@TableField(exist=false)
	private String orgName;
	
	@TableField(exist=false)
	private String TypeName;
	
}
