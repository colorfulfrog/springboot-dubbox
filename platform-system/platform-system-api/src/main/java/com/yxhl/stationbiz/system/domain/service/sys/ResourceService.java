package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELTreeService;
import com.yxhl.stationbiz.system.domain.entity.sys.Resource;

/**
 * @author yipengfi
 * @title: 菜单管理接口
 */
public interface ResourceService extends IELTreeService<Resource>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Resource> selPageList(Page<Resource> page,Resource resource);
	
	//用户角色对应的菜单
	List<Resource> selectRidResource(String parent_id,Integer type,String uid);
	
	
	boolean deleteIds(List<String> ids);
	
	List<Resource> ridResource(String rid);
	
	boolean add(Resource resource);
}
