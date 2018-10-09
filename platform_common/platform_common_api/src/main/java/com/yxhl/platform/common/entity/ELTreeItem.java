package com.yxhl.platform.common.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

import lombok.Data;

/**
 * 树结构业务对象基础类：ELTreeItem
 */
@Data
public class ELTreeItem<T> extends ELItem {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 父级对象
	 */
	@TableField(exist = false)
	protected T parent;
	
	
	/**
	 * 父级Id
	 */
	@TableField(value = "parent_id")
	private String parentId;
	
	
	/**
	 * 所有父级IdPath
	 */
	@TableField(exist = false)
	private String parentIdPath;
	
	/**
	 * 子节点列表
	 */
	@TableField(exist = false)
	private List<T> children;

	public ELTreeItem() {
		super();
	}
}
