package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Resource;

@Mapper
@Repository
public interface ResourceDao extends CrudDao<Resource> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Resource> selPageList(Pagination page,Resource resource);
	
	
	List<Resource> selectRidResource(Map<String, Object> map);
	
	List<Resource> ridResource(String rid);
	
}
