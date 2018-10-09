package com.yxhl.platform.common.service.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.entity.ELTreeItem;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.platform.common.utils.Reflections;

/**
 * TreeService
 * @param <T> the type parameter
 */
@Transactional(readOnly = true)
public abstract class TreeService<D extends CrudDao<T>,T extends ELTreeItem<T>> extends CrudService<D, T> {

	/**
	 * <p>
	 * 插入一条记录（选择字段，策略插入）
	 * </p>
	 *
	 * @param treeItem 实体对象
	 * @return boolean
	 */
	@Transactional(readOnly = false)
	public boolean saveTreeItem(T treeItem) {
		return super.insertOrUpdate(treeItem);
	}

	/**
	 * <p>
	 * 插入（批量），该方法不适合 Oracle
	 * </p>
	 *
	 * @param treeItemList 实体对象列表
	 * @return boolean
	 */
	@Transactional(readOnly = false)
	public boolean saveTree(List<T> treeItemList) {
		return super.insertOrUpdateBatch(treeItemList);
	}

	/**
	 * 根据根节点 ID 获得整棵树所有节点，循环递归查询
	 * @param id 主键ID
	 * @return boolean
	 */
	public List<T> getTreeById(Serializable id) {
		List<T> tree = Lists.newArrayList();
		T root = super.selectById(id);
		if (root != null) {
			tree.add(root);
			getTreeListByParentId(tree, id);
		}
		return tree;
	}
	
	/**根据根节点 ID 获得整棵树所有节点，循环递归查询
	 * @param id		主键ID
	 * @param isForward	true[正向树],false[反向树]
	 * @return
	 */
	public List<T> getTreeById(Serializable id,boolean isForward ) {
		List<T> tree = Lists.newArrayList();
		T root = super.selectById(id);
		if (root != null) {
			tree.add(root);
			getTreeListByParentId(tree, id,isForward);
		}
		return tree;
	}

	/**
	 * @param
	 * @return
	 * @description 平铺列表
	 */
	private void getTreeListByParentId(List<T> tree,Serializable id) {
		EntityWrapper<T> wrapper = new EntityWrapper<>();
		wrapper.setEntity(newInstance());
		wrapper.where("parent_id={0}", id);
		List<T> list = super.selectList(wrapper);
		if (list.size() > 0) {
			for (T object : list) {
				tree.add(object);
				getTreeListByParentId(tree, object.getId());
			}
		}
	}
	
	private void getTreeListByParentId(List<T> tree,Serializable id,boolean isForward) {
		if(isForward){
			getTreeListByParentId(tree,id);
		}else{
			getReverseTreeListByParentId(tree,id);
		}
		
	}
	
	private void getReverseTreeListByParentId(List<T> tree,Serializable id) {
		T object = super.selectById(id);
		if(object!=null){
			tree.add(object);
			getReverseTreeListByParentId(tree,object.getParentId());
		}
	}

	/**
	* @param
	* @return
	* @description 根据根节点 ID 获得整棵树所有节点，循环递归查询
	 * childern list
	*/
	public List<T> getTreeByRootId(Serializable id) {
		List<T> tree = Lists.newArrayList();
		T root = super.selectById(id);
		if (root != null) {
		int treeDeepLevel = 0;
			root.setChildren(getTreeByParentId(id, treeDeepLevel));
			tree.add(root);
		}
		return tree;
	}

	/**
	* @param
	* @return
	* @description 父子childern
	*/
	private List<T> getTreeByParentId(Serializable id,int treeDeepLevel) {
		treeDeepLevel += 1;
//		if(treeDeepLevel == DomainConstants.TREE_DEEP_LEVEL){
//			return
//		}
		EntityWrapper<T> wrapper = new EntityWrapper<>();
		wrapper.setEntity(newInstance());
		wrapper.where("parent_id={0}", id);
		List<T> list = super.selectList(wrapper);
		if (list.size() > 0) {
			for (T object : list) {
				List<T> listChild = getTreeByParentId(object.getId(), treeDeepLevel);
				if (listChild.size() > 0) {
					object.setChildren(listChild);
				}
			}
		}
		return list;
	}

	/**
	 * <p>
	 * 获得所有上级节点，拼接parentIdPath
	 * </p>
	 *
	 * @param t 当前树节点
	 * @return T
	 */
	public void buildParentPath(T t) {
		if (t == null) {
			return;
		} else {
			t.setParentIdPath("");
		}
		T p = this.getParentById(t.getId());
		String parentId = t.getParentId();
		while (p != null) {
			parentId = p.getParentId();
			t.setParentIdPath("/" + p.getId() + t.getParentIdPath());
			p = this.getParentById(p.getId());
			if (p == null) {
				break;
			}
			if (parentId.equals(p.getId())) {
				//如果当前节点的id和parentId一致，跳出循环
				break;
			}
		}
		if (p == null) {
			t.setParentIdPath("/" + parentId + t.getParentIdPath());
		}
	}

	/**
	 * <p>
	 * 根据 ID 获得下一级子节点
	 * </p>
	 *
	 * @param id 主键ID
	 * @return boolean
	 */
	public List<T> getChildrensById(Serializable id) {
		EntityWrapper<T> wrapper = new EntityWrapper<>();
		wrapper.setEntity(newInstance());
		wrapper.where("parent_id = {0}", id);
		return super.selectList(wrapper);
	}

	/**
	 * <p>
	 * 获得上一级父节点
	 * </p>
	 *
	 * @param id 当前树节点id 主键ID
	 * @return T
	 */
	public T getParentById(Serializable id) {
		EntityWrapper<T> wrapper = new EntityWrapper<>();
		wrapper.setEntity(newInstance());
		wrapper.where("id={0}", id);

		T thisOne = selectOne(wrapper);
		if (thisOne != null) {
			wrapper = new EntityWrapper<>();
			wrapper.setEntity(newInstance());
			wrapper.where("id={0}", thisOne.getParentId());
		}
		return selectOne(wrapper);
	}

	/**
	 * @param
	 * @return
	 * @description 实例化对象
	 */
	private T newInstance() {
		@SuppressWarnings("unchecked")
		Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);
		T object;
		try {
			object = entityClass.newInstance();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return object;
	}
}
