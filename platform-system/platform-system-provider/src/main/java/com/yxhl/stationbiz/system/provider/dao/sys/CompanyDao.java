package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Company;

@Mapper
@Repository
public interface CompanyDao extends CrudDao<Company> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Company> selPageList(Pagination page,Company company);
	
	List<Company> exportData(Company company);
}
