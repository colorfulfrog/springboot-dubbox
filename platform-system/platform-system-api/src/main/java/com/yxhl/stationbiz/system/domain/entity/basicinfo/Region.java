package com.yxhl.stationbiz.system.domain.entity.basicinfo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELTreeItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value="bs_region")
public class Region extends ELTreeItem<Region>{
	
	@ApiModelProperty("编码")
	private String regionCode;
	
	@ApiModelProperty("区域名称")
	private String regionName;
	
	@ApiModelProperty("简拼")
	private String spell;
	
	@ApiModelProperty("全称")
	private String fullName;
	
    @TableField(exist=false)
	private String parentName;

}
