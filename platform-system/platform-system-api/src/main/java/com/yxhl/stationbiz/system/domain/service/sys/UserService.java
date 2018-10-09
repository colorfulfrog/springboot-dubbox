package com.yxhl.stationbiz.system.domain.service.sys;


import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.User;

public interface UserService extends IELService<User> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<User> selPageList(Page<User> page,User user);
	
	//用户登录
	User userLogin(String userName,String passWord);
	
	boolean userAdd(User user);
	
	boolean userUpdate(User user);
	
	boolean userDelete(List<String> userId);
	
	List<User> userTree(String stationId);
	
	List<User> financeUser(String companyId);
	
	/**
	 * 根据角色查用户
	 * @param role
	 * @return
	 */
	List<User>	getUserByRole(User user);
}
