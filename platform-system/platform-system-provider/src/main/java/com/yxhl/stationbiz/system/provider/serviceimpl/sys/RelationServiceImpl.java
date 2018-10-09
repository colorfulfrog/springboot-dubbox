package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;
import com.yxhl.stationbiz.system.domain.service.sys.ResourceService;
import com.yxhl.stationbiz.system.domain.service.sys.RoleService;
import com.yxhl.stationbiz.system.provider.dao.sys.RelationDao;
/**
 * @ClassName: RelationServiceImpl
 * @Description: 用户角色、角色菜单关联表 serviceImpl
 * @author xjh
 * @date 2018-7-18 9:56:13
 */
@Transactional(readOnly = true)
@Service("relationService")
public class RelationServiceImpl extends CrudService<RelationDao, Relation> implements RelationService {
	@Autowired
	private RelationDao relationDao;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private RelationService relationService;
	
	@Autowired
	private RoleService roleService;

	@Override
	public Page<Relation> selPageList(Page<Relation> page, Relation relation) {
		List<Relation> list = relationDao.selPageList(page, relation);
		page.setRecords(list);
		return page;
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean relationAdd(Relation relation) {
		Wrapper<Relation> wrappers = new EntityWrapper<Relation>();
		wrappers.where("type={0} and aid={1}", 2,relation.getAid());
		List<Relation> relist=relationService.selectList(wrappers);
		List<String> ids= new ArrayList<String>();
		boolean isAdd=false;
		if(relist.size()>0) {
		for (Relation resource : relist) {
			ids.add(resource.getId());
		}
		isAdd= relationService.deleteBatchIds(ids);
		}
		
		List<String> list = relation.getBids();
		if (list.size() > 0) {
			List<Relation> relationList = new ArrayList<Relation>();
			for (String string : list) {
				Relation a = new Relation();
				a.setAid(relation.getAid());
				a.setBid(string);
				a.setType("2");
				relationList.add(a);
			}
			isAdd=relationService.insertBatch(relationList);
		}
		Role role= new Role();
		role.setId(relation.getAid());
		role.setAuthorizer(relation.getUid());
		role.setAuthorizeTime(new Date());
		roleService.updateById(role);
		return isAdd;
	}
}
