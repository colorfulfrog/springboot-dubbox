package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Organization;

/**
 *	
 *  sys_organizationDao
 *  注释:机构表
 *  创建人: xjh
 *  创建日期:2018-7-12 15:58:15
 */
@Mapper
@Repository
public interface OrganizationDao extends CrudDao<Organization>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param organization 条件参数
	 * @return 当前页数据
	 */
	List<Organization> selPageList(Pagination page,Organization organization);
	
	/**
	 * 查询导出数据
	 * @param organization
	 * @return
	 */
	List<Organization> exportData(Organization organization);
}