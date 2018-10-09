package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.User;

@Mapper
@Repository
public interface UserDao extends CrudDao<User> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<User> selPageList(Pagination page,User user);
	
	//用户登录
	User userLogin(String userName);
	
	List<User> userTree(String stationId);
	
	List<User> financeUser(String companyId);
	
	/**
	 * 根据角色查用户
	 * @param role
	 * @return
	 */
	List<User>	getUserByRole(User user);
}
