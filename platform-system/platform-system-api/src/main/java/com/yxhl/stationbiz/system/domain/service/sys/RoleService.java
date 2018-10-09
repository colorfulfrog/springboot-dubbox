package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;

public interface RoleService extends IELService<Role> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Role> selPageList(Page<Role> page,Role role);
	
	List<Role> userList(Role role);
}
