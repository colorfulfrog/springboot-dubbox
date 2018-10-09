package com.yxhl.platform.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.Wrapper;

/**
 * 顶级 Service
 */
public interface IELService<T> {
	/**
	 * <p>
	 * 插入一条记录（选择字段，策略插入）
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean insert(T entity);

	/**
	 * <p>
	 * 插入一条记录（全部字段）
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean insertAllColumn(T entity);

	/**
	 * <p>
	 * 插入（批量），该方法不适合 Oracle
	 * </p>
	 *
	 * @param entityList 实体对象列表
	 * @return boolean
	 */
	boolean insertBatch(List<T> entityList);



	/**
	 * <p>
	 * TableId 注解存在更新记录，否插入一条记录
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean insertOrUpdate(T entity);

	/**
	 * <p>
	 * 根据 ID 删除
	 * </p>
	 *
	 * @param id 主键ID
	 * @return boolean
	 */
	boolean deleteById(Serializable id);


	/**
	 * <p>
	 * 根据 entity 条件，删除记录
	 * </p>
	 *
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return boolean
	 */
	boolean delete(Wrapper<T> wrapper);

	/**
	 * <p>
	 * 删除（根据ID 批量删除）
	 * </p>
	 *
	 * @param idList 主键ID列表
	 * @return boolean
	 */
	boolean deleteBatchIds(List<? extends Serializable> idList);

	/**
	 * <p>
	 * 删除，自定义条件
	 * </p>
	 *
	 * @param columnMap 自定义属性map
	 * @return boolean
	 */
	boolean deleteByMap(Map<String, Object> columnMap);
	/**
	 * <p>
	 * 根据 ID 选择修改
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean updateById(T entity);

	/**
	 * <p>
	 * 根据 whereEntity 条件，更新记录
	 * </p>
	 *
	 * @param entity  实体对象
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return boolean
	 */
	boolean update(T entity, Wrapper<T> wrapper);

	/**
	 * <p>
	 * 根据 ID 修改全部字段
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	boolean updateAllColumnById(T entity);

	/**
	 * <p>
	 * 根据ID 批量更新字段
	 * </p>
	 *
	 * @param entityList 实体对象列表
	 * @return boolean
	 */
	boolean updateBatchById(List<T> entityList);

	/**
	 * <p>
	 * 根据ID 批量更新全部字段
	 * </p>
	 *
	 * @param entityList 实体对象列表
	 * @return boolean
	 */
	boolean updateAllColumnBatchById(List<T> entityList);

	/**
	 * <p>
	 * 根据 ID 查询
	 * </p>
	 *
	 * @param id 主键ID
	 * @return T
	 */
	T selectById(Serializable id);
	
	/**
	 * <p>
	 * 查询单个实体
	 * </p>
	 *
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return
	 */
	T selectOne(Wrapper<T> wrapper);
	
	/**
     * <p>
     * 查询（根据ID 批量查询）
     * </p>
     *
     * @param idList 主键ID列表
     * @return List<T>
     */
    List<T> selectBatchIds(List<? extends Serializable> idList);

	/**
	 * <p>
	 * 查询列表
	 * </p>
	 *
	 * @param wrapper 实体包装类 {@link Wrapper}
	 * @return
	 */
	List<T> selectList(Wrapper<T> wrapper);
	
	
	/**
     * <p>
     * 根据 Wrapper 条件，查询总记录数
     * </p>
     *
     * @param wrapper 实体对象
     * @return int
     */
    int selectCount(Wrapper<T> wrapper);
}
