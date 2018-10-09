package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;

@Mapper
@Repository
public interface RoleDao extends CrudDao<Role> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Role> selPageList(Pagination page,Role role);
	
	List<Role> userList(Role role);
}
