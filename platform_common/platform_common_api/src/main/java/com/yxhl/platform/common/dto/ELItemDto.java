package com.yxhl.platform.common.dto;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据ELItemDto类
 */
@Data
public class ELItemDto extends ELObjectDto {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
	private String name;

	@ApiModelProperty("描述")
	private String description;

	@ApiModelProperty("扩展属性")
	private Map<String, Object> flexAttrs;
	
}
