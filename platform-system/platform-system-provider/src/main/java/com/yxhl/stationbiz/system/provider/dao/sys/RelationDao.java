package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;

/**
 *	
 *  sys_relationDao
 *  注释:用户角色、角色菜单关联表
 *  创建人: xjh
 *  创建日期:2018-7-18 9:56:13
 */
@Mapper
@Repository
public interface RelationDao extends CrudDao<Relation>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param relation 条件参数
	 * @return 当前页数据
	 */
	List<Relation> selPageList(Pagination page,Relation relation);
}