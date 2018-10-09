package com.yxhl.stationbiz.system.provider.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.ELProject;

/**
 * 项目表DAO接口
 */

@Mapper
@Repository
public interface ELProjectDao extends CrudDao<ELProject> {
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<ELProject> selPageList(Pagination page,ELProject project);
}