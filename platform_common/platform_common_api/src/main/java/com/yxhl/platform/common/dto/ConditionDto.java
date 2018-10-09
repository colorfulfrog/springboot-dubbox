package com.yxhl.platform.common.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConditionDto implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("字段属性名")
	private String fieldName;

	@ApiModelProperty("字段名")
	private String fieldCode;
	
	@ApiModelProperty("字段描述")
	private String fieldDesc;
	
	@ApiModelProperty("查询类型")
	private String queryType;
	
	@ApiModelProperty("字段值1")
	private String filedValue1;
	
	@ApiModelProperty("字段值2")
	private String filedValue2;
	
	@ApiModelProperty("是否多值")
	private Boolean isMulti;
	
	@ApiModelProperty("关联的对象名")
	private String linkObject;
	
	@ApiModelProperty("字典类型")
	private String dicType;
	
	@ApiModelProperty("字段类型")
	private String fieldType;
}
