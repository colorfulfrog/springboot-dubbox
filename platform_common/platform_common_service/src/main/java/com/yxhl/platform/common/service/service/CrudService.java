package com.yxhl.platform.common.service.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.enums.SqlMethod;
import com.baomidou.mybatisplus.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.baomidou.mybatisplus.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.baomidou.mybatisplus.toolkit.TableInfoHelper;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.entity.BaseObject;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.platform.common.utils.InstanceUtil;
import com.yxhl.platform.common.utils.SysConstants;

/**
 * Service基类
 * @param <T> the type parameter
 */
@Transactional(readOnly = true)
public abstract class CrudService<D extends CrudDao<T>,T extends BaseObject> {
	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected CrudDao<T> crudDao;

	protected static boolean retBool(Integer result) {
		return SqlHelper.retBool(result);
	}

	protected Class<T> currentModelClass() {
		return ReflectionKit.getSuperClassGenricType(this.getClass(), 1);
	}

	/**
	 * @param
	 * @return
	 * @description 批量操作 SqlSession
	 */
	protected SqlSession sqlSessionBatch() {
		return SqlHelper.sqlSessionBatch(this.currentModelClass());
	}

	/**
	 * @param
	 * @return
	 * @description 获取SqlStatement
	 */
	protected String sqlStatement(SqlMethod sqlMethod) {
		return SqlHelper.table(this.currentModelClass()).getSqlStatement(sqlMethod.getMethod());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insert(T entity) {
		return retBool(crudDao.insert(entity));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public T insertOne(T entity) {
		try {
			crudDao.insert(entity);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer InInsert(T entity) {
		try {
			return crudDao.insert(entity);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertAllColumn(T entity) {
		return retBool(crudDao.insertAllColumn(entity));
	}

	/**
	 * @param entityList 列表
	 * @return bool
	 * @description 批量插入
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertBatch(List<T> entityList) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			int batchSize = entityList.size();
			String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
			for (int i = 0; i < size; i++) {
				if (StringUtils.isEmpty(entityList.get(i).getId())) {
					entityList.get(i).setId(IdWorker.get32UUID());
				}
				batchSqlSession.insert(sqlStatement, entityList.get(i));
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Throwable e) {
			throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
		}
		return true;
	}

	/**
	 * <p>
	 * TableId 注解存在更新记录，否插入一条记录
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return boolean
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertOrUpdate(T entity) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal)) {
					return retBool(InInsert(entity));
				} else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
					return updateById(entity) || retBool(InInsert(entity));
				}
			} else {
				throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}

	@Transactional
	public boolean insertOrUpdateAllColumn(T entity) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal)) {
					return insertAllColumn(entity);
				} else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
					 */
					return updateAllColumnById(entity) || insertAllColumn(entity);
				}
			} else {
				throw new MybatisPlusException("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}


	@Transactional
	public boolean insertOrUpdateBatch(List<T> entityList) {
		return insertOrUpdateBatch(entityList, true);
	}

	@Transactional
	public boolean insertOrUpdateAllColumnBatch(List<T> entityList) {
		return insertOrUpdateBatch(entityList, false);
	}

	@Transactional
	public boolean insertOrUpdateAllColumnBatch(List<T> entityList, int batchSize) {
		return insertOrUpdateBatch(entityList, false);
	}

	/**
	 * 批量插入修改
	 *
	 * @param entityList 实体对象列表
	 * @param selective  是否滤掉空字段
	 * @return boolean
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertOrUpdateBatch(List<T> entityList, boolean selective) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			int batchSize = entityList.size();
			for (int i = 0; i < size; i++) {
				if (selective) {
					insertOrUpdate(entityList.get(i));
				} else {
					insertOrUpdateAllColumn(entityList.get(i));
				}
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Exception e) {
			throw new MybatisPlusException("Error: Cannot execute insertOrUpdateBatch Method. Cause", e);
		}
		return true;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delete(Wrapper<T> wrapper) {
		return retBool(crudDao.delete(wrapper));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer delete(String id) {
		try {
			return crudDao.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteById(Serializable id) {
		return retBool(crudDao.deleteById(id));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBatchIds(List<? extends Serializable> idList) {
		return retBool(crudDao.deleteBatchIds(idList));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteByMap(Map<String, Object> columnMap) {
		return retBool(crudDao.deleteByMap(columnMap));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer deleteByEntity(T t) {
		try {
			Wrapper<T> wrapper = new EntityWrapper<T>(t);
			return crudDao.delete(wrapper);
		} catch (Exception e) {
			logger.error(SysConstants.Exception_Head, e);
			return Integer.MIN_VALUE;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public T update(T entity) {
		try {
			if (entity.getId() == null) {
				crudDao.insert(entity);
			} else {
				T org = crudDao.selectById(entity.getId());
				T update = InstanceUtil.getDiff(org, entity);
				update.setId(entity.getId());
				crudDao.updateById(update);
				entity = crudDao.selectById(entity.getId());
			}
		} catch (Exception e) {
			logger.error(SysConstants.Exception_Head, e);
		}
		return entity;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateById(T entity) {
		return retBool(crudDao.updateById(entity));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAllColumnById(T entity) {
		return retBool(crudDao.updateAllColumnById(entity));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean update(T entity, Wrapper<T> wrapper) {
		return retBool(crudDao.update(entity, wrapper));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateBatchById(List<T> entityList) {
		return updateBatchById(entityList, true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAllColumnBatchById(List<T> entityList) {
		return updateBatchById(entityList, false);
	}

	/**
	 * 根据主键ID进行批量修改
	 *
	 * @param entityList 实体对象列表
	 * @param selective  是否滤掉空字段
	 * @return boolean
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateBatchById(List<T> entityList, boolean selective) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			int batchSize = entityList.size();
			SqlMethod sqlMethod = selective ? SqlMethod.UPDATE_BY_ID : SqlMethod.UPDATE_ALL_COLUMN_BY_ID;
			String sqlStatement = sqlStatement(sqlMethod);
			for (int i = 0; i < size; i++) {
				MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
				param.put("et", entityList.get(i));
				batchSqlSession.update(sqlStatement, param);
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Throwable e) {
			throw new MybatisPlusException("Error: Cannot execute updateBatchById Method. Cause", e);
		}
		return true;
	}

	public T queryById(String id) {
		logger.debug(getClass().getSimpleName() + ":" + id + " retry queryById.");
		return crudDao.selectById(id);
	}

	public T selectById(Serializable id) {
		return crudDao.selectById(id);
	}

	public T get(String id) {
		return crudDao.selectById(id);
	}

	public T selectOne(T entity) {
		return crudDao.selectOne(entity);
	}

	public T selectOne(Wrapper<T> wrapper) {
		return SqlHelper.getObject(crudDao.selectList(wrapper));
	}

	public int selectCount(Wrapper<T> wrapper) {
		return SqlHelper.retCount(crudDao.selectCount(wrapper));
	}

	public List<Object> selectObjs(Wrapper<T> wrapper) {
		return crudDao.selectObjs(wrapper);
	}

	public Page<T> selectPage(Page<T> page) {
		return this.selectPage(page, Condition.EMPTY);
	}

	public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
		SqlHelper.fillWrapper(page, wrapper);
		page.setRecords(crudDao.selectMapsPage(page, wrapper));
		return page;
	}

	public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
		SqlHelper.fillWrapper(page, wrapper);
		page.setRecords(crudDao.selectPage(page, wrapper));
		return page;
	}

	/**
	 * 根据Id查询(默认类型T)
	 */
	public List<T> getList(final List<String> ids) {
		List<T> list = Lists.newArrayList();
		if (ids != null) {
			for (int i = 0; i < ids.size(); i++) {
				list.add(null);
			}
			list = crudDao.selectBatchIds(ids);
		}
		return list;
	}

	public List<T> queryList(Map<String, Object> columnMap) {
		return this.crudDao.selectByMap(columnMap);
	}

	public List<T> selectList(Wrapper<T> entity) {
		return crudDao.selectList(entity);
	}
	
    public List<T> selectBatchIds(List<? extends Serializable> idList){
    	return crudDao.selectBatchIds(idList);
    }
}
