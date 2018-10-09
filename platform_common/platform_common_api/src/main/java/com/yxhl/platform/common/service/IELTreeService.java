package com.yxhl.platform.common.service;

import java.io.Serializable;
import java.util.List;

/**
 * 操作树结构的TreeService
 * @author lisk
 * @version v1.0
 * @date 2017/8/10
 */
public interface IELTreeService<T> extends IELService<T> {

	/**
	 * <p>
	 * 插入一条记录（选择字段，策略插入）
	 * </p>
	 *
	 * @param treeItem 实体对象
	 * @return boolean
	 */
	boolean saveTreeItem(T treeItem);

	/**
	 * <p>
	 * 插入（批量），该方法不适合 Oracle
	 * </p>
	 *
	 * @param treeItemList 实体对象列表
	 * @return boolean
	 */
	boolean saveTree(List<T> treeItemList);
	
	/**
	 * <p>
	 * 根据根节点 ID 获得整棵树所有节点，循环递归查询
	 * 平铺列表
	 * </p>
	 *
	 * @param id 主键ID
	 * @return boolean
	 */
	List<T> getTreeById(Serializable id);
	/**
	 * <p>
	 * 根据根节点 ID 获得整棵树所有节点，循环递归查询
	 * 平铺列表
	 * </p>
	 *
	 * @param id 主键ID
	 * @param isForward	true[正向树],false[反向树]
	 * @return
	 */
	List<T> getTreeById(Serializable id,boolean isForward);

	/**
	* @param
	* @return childern list
	* @description 根据根节点 ID 获得整棵树所有节点，循环递归查询
	* @version v1.0
	*/
	List<T> getTreeByRootId(Serializable id);

	/**
	 * <p>
	 * 根据 ID 获得下一级子节点
	 * </p>
	 *
	 * @param id 主键ID
	 * @return boolean
	 */
	List<T> getChildrensById(Serializable id);
	
	/**
	 * <p>
	 * 获得上一级父节点
	 * </p>
	 *
	 * @param id 当前树节点id 主键ID
	 * @return boolean
	 */
	T getParentById(Serializable id);

}
