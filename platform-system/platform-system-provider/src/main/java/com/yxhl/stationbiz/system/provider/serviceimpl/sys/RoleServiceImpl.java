package com.yxhl.stationbiz.system.provider.serviceimpl.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.service.sys.RoleService;
import com.yxhl.stationbiz.system.provider.dao.sys.RoleDao;


@Service(value = "roleService")
public class RoleServiceImpl extends CrudService<RoleDao, Role> implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public Page<Role> selPageList(Page<Role> page, Role role) {
		List<Role> list= roleDao.selPageList(page, role);
		page.setRecords(list);
		return page;
	}

	public List<Role> userList(Role role) {
		return roleDao.userList(role);
	}


}