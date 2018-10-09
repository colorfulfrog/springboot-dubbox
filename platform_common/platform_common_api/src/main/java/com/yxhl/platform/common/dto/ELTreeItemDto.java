package com.yxhl.platform.common.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据ELTreeItemDto类
 */
@Data
@ApiModel("树对象Dto")
public class ELTreeItemDto<T> extends ELObjectDto {
    private static final long serialVersionUID = 1L;
    
    /**
	 * 节点类型
	 */
    @ApiModelProperty("类型")
	private String type;
    
    /**
	 * 父级对象
	 */
    @ApiModelProperty("父节点")
	protected T parent;
	
	/**
	 * 父级Id
	 */
    @ApiModelProperty("父节点Id")
	private String parentId;
	
	/**
	 * 所有父级IdPath
	 */
    @ApiModelProperty("父节点IdPath")
	private String parentIdPath;
    
	/**
	 * 子节点列表
	 */
    @ApiModelProperty("子节点列表")
	private List<T> children;
	
}
