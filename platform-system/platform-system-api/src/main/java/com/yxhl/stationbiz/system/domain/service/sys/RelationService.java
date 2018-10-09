package com.yxhl.stationbiz.system.domain.service.sys;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;

/**
 *  RelationService
 *  注释:用户角色、角色菜单关联表Service
 *  创建人: xjh
 *  创建日期:2018-7-18 9:56:13
 */
public interface RelationService extends IELService<Relation>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param relation 条件参数
	 * @return 当前页数据
	 */
	Page<Relation> selPageList(Page<Relation> page,Relation relation);
	
	boolean relationAdd(Relation relation);
}
