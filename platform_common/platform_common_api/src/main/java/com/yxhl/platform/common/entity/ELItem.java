package com.yxhl.platform.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 业务对象基础类：ELItem
 */
@Data
public class ELItem extends ELObject {
	private static final long serialVersionUID = 1L;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "创建人")
	private java.lang.String creator;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "修改人")
	private java.lang.String updater;
	
	public ELItem() {
		super();
	}
}
