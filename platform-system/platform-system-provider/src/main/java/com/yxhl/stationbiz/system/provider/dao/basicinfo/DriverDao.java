package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;

@Mapper
@Repository
public interface DriverDao extends CrudDao<Driver> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Driver> selPageList(Pagination page,Driver driver);
	
	/**
	 * 查询导出数据
	 * @param driver
	 * @return
	 */
	List<Driver> exportData(Driver driver);
}
