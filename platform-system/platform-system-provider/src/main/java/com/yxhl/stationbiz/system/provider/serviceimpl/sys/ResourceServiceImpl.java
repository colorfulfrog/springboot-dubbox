package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.service.service.TreeService;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;
import com.yxhl.stationbiz.system.domain.entity.sys.Resource;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;
import com.yxhl.stationbiz.system.domain.service.sys.ResourceService;
import com.yxhl.stationbiz.system.provider.dao.sys.ResourceDao;

@Service(value = "resourceService")
public class ResourceServiceImpl extends TreeService<ResourceDao, Resource> implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private ResourceService resourceService;
	
    @Autowired
    private RelationService relationService;


	@Override
	public Page<Resource> selPageList(Page<Resource> page, Resource resource) {
		List<Resource> list = resourceDao.selPageList(page, resource);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<Resource> selectRidResource(String parent_id,Integer type,String uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parent_id", parent_id);
		map.put("type", type);
		map.put("uid", uid);
		List<Resource> resources = resourceDao.selectRidResource(map);// 用户角色所对应的所有菜单
		return resources;
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteIds(List<String> ids) {
		for (String string : ids) {
			Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
			wrapper.where("type={0} and parent_id={1}", 2, string);
			List<Resource> list1 = resourceService.selectList(wrapper);// 查询所删除菜单下面的按钮
			if(list1.size()>0) {
				for (Resource resource : list1) {
					ids.add(resource.getId());//要删除的二级菜单
				}
			}
		}
		boolean isdel=resourceService.deleteBatchIds(ids);//删除一级菜单和一级菜单下面多对应的按钮
		List<String> relationIds=new ArrayList<String>();//取所有要删除的角色所对应的菜单和按钮
		for (String string : ids) {
			Wrapper<Relation> wrappers = new EntityWrapper<Relation>();
			wrappers.where("type={0} and bid={1}", 2,string);
			List<Relation> relist=relationService.selectList(wrappers);
			for (Relation relation : relist) {
				relationIds.add(relation.getId());
			}
		}
		relationService.deleteBatchIds(relationIds);//删除角色所对应的菜单和按钮
		return isdel;
	}

	@Override
	public List<Resource> ridResource(String rid) {
		return resourceDao.ridResource(rid);
	}

	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(Resource resource) {
		boolean isAdd=resourceService.insert(resource);
		List<Resource> list=new ArrayList<Resource>();
		if (StringUtils.isNotBlank(resource.getParentId())) {//如果是二级菜单
			for (int i = 0; i <=4; i++) {
				Resource res= new Resource();
				res.setId(IdWorker.get32UUID());
				res.setParentId(resource.getId());
				res.setCreateBy(resource.getCreateBy());
				res.setUpdateBy(resource.getUpdateBy());
				res.setFunctionType(2);
				res.setType(2);
				res.setResourceName(i==0?"新增":i==1?"删除":i==2?"修改":i==3?"导出":i==4?"关闭":i+"");
				res.setPermision(i==0?"add":i==1?"del":i==2?"update":i==3?"export":i==4?"close":i+"");
				res.setSortNum(i+1);
				res.setIsShow(1);
				list.add(res);
			}
		}
		if(list.size()>0) {
			resourceService.insertBatch(list);
		}
		return isAdd;
	}

}