package com.yxhl.stationbiz.system.provider.serviceimpl.sys;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.redis.util.RedisUtil;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.entity.sys.User;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.domain.service.sys.CompanyService;
import com.yxhl.stationbiz.system.domain.service.sys.OrganizationService;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;
import com.yxhl.stationbiz.system.domain.service.sys.RoleService;
import com.yxhl.stationbiz.system.domain.service.sys.UserService;
import com.yxhl.stationbiz.system.provider.dao.sys.UserDao;

@Service(value = "userService")
public class UserServiceImpl extends CrudService<UserDao, User> implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RedisUtil redisUtil;
	
    @Autowired
    private StationService stationService;
    
    @Autowired
    private OrganizationService organizationService;
    
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private RelationService relationService;
	
	@Override
	public Page<User> selPageList(Page<User> page, User user) {
		List<User> list= userDao.selPageList(page, user);
		for (User user2 : list) {
			user2.setUserPwd("");
			user2.setIdentityCard("");
		}
		page.setRecords(list);
		return page;
	}
	@Override
	public User userLogin(String userName,String passWord) {
		User user= userDao.userLogin(userName);
		if(user==null) {
			throw new YxBizException("用户名不存在");
		}
		if(!user.getUserPwd().toString().equals(Util.md5(passWord+"YXHL"))) {
			throw new YxBizException("密码错误");
		}
		//判断redis中是否有值
		if (redisUtil.exists("SYS_USER_TOKEN:"+userName)) {
			String token = (String)redisUtil.get("SYS_USER_TOKEN:"+userName);
			redisUtil.remove("SYS_USER_TOKEN:"+userName);
			redisUtil.remove("SYS_USER_TOKEN:"+token);
		}
		/**创建token*/
		String token=UUID.randomUUID().toString();
		ELUser eluser= new ELUser();
		eluser.setNickName(user.getNickName());
		eluser.setUserName(user.getUserName());
		eluser.setCompanyId(user.getCompanyId());
		eluser.setOrgId(user.getOrgId());
		eluser.setStationId(user.getStationId());
		eluser.setAvatar(user.getAvatar());
		eluser.setId(user.getId());
		redisUtil.set("SYS_USER_TOKEN:"+userName,token);
		redisUtil.set("SYS_USER_TOKEN:"+token,eluser);
		
		//清空用户的一些重要信息
		user.setUserPwd("");
		user.setEmployId("");
		user.setIdentityCard("");
		user.setTelphone("");
		user.setToken(token);
		return user;
	}
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean userAdd(User user) {
		user.setId(IdWorker.get32UUID());
		user.setUserPwd(Util.md5(user.getUserPwd()+"YXHL"));
		boolean isadd= userService.insert(user);
		if(isadd) {
			List<String> list= user.getIds();
			for (String string : list) {
				Relation relation= new Relation();
				relation.setId(IdWorker.get32UUID());
				relation.setType("1");
				relation.setAid(user.getId());
				relation.setBid(string);
				relation.setCreateBy(user.getCreateBy());
				relation.setUpdateBy(user.getUpdateBy());
				relationService.insert(relation);
			}
			
		}
		return isadd;
	}
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean userUpdate(User user) {
		Wrapper<Relation> wrapper = new EntityWrapper<Relation>();
		wrapper.where("aid={0} and type={1}", user.getId(), 1);
		List<Relation> list = relationService.selectList(wrapper);// 原来用户所对应的觉色
		boolean isupdate = userService.updateById(user);
		if (isupdate) {
			for (Relation relation : list) {
				relationService.deleteById(relation.getId());// 删除用户原来的角色
			}
			List<String> list1= user.getIds();
			for (String string : list1) {//修改为现在传过来的角色
				Relation relation = new Relation();
				relation.setId(IdWorker.get32UUID());
				relation.setType("1");
				relation.setAid(user.getId());
				relation.setBid(string);
				relation.setCreateBy(user.getCreateBy());
				relation.setUpdateBy(user.getUpdateBy());
				relationService.insert(relation);
			}
		}
		return isupdate;
	}
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean userDelete(List<String> userId) {
		boolean flag = false;
		for (String string : userId) {
			boolean isDelete = userService.deleteById(string);// 删除用户
			Wrapper<Relation> wrapper = new EntityWrapper<Relation>();
			wrapper.where("aid={0} and type={1}", string, 1);
			List<Relation> list = relationService.selectList(wrapper);// 原来用户所对应的觉色
			if (isDelete) {
				for (Relation relation : list) {
					relationService.deleteById(relation.getId());// 删除用户所对用的角色
					flag = true;
				}
			}
		}
		return flag;
	}
	@Override
	public List<User> userTree(String stationId) {
		return userDao.userTree(stationId);
	}
	@Override
	public List<User> financeUser(String companyId) {
		return userDao.financeUser(companyId);
	}
	@Override
	public List<User> getUserByRole(User user) {
		List<User> userByRole = userDao.getUserByRole(user);
		return userByRole;
	}

}