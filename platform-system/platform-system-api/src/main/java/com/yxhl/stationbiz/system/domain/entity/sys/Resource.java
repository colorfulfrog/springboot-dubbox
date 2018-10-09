package com.yxhl.stationbiz.system.domain.entity.sys;


import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELTreeItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 菜单管理
 * @author ypf
 *
 */
@Data
@TableName(value="sys_resource")
public class Resource extends ELTreeItem<Resource>{
	
	@ApiModelProperty("菜单名称")
	private String resourceName;
	
	@ApiModelProperty("菜单地址")
	private String resourceUrl;
	
	@ApiModelProperty("功能类型 1、菜单 2、按钮")
	private Integer type;
	
	@ApiModelProperty("排序")
	private Integer sortNum;
	
	@ApiModelProperty("资源等级，表示几级菜单")
	private Integer resourceLevel;
	
	@ApiModelProperty("权限编码")
	private String permision;
	
	@ApiModelProperty("备注")
	private String remake;
	
	@ApiModelProperty("版本号")
	private Long version;
	
	@ApiModelProperty("是否显示 1.显示 0.不显示")
	private Integer isShow;
	
	@ApiModelProperty("功能类型 1 系统  2 通用")
	private Integer functionType;
	
	@TableField(exist=false)
	private List<Resource> chridend;
	
	@TableField(exist=false)
	private List<Resource> chridendList;
}
