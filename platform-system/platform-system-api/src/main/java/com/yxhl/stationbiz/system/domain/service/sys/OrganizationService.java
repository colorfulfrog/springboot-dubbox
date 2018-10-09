package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.Organization;

/**
 *  OrganizationService
 *  注释:机构表Service
 *  创建人: xjh
 *  创建日期:2018-7-12 15:58:15
 */
public interface OrganizationService extends IELService<Organization>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param organization 条件参数
	 * @return 当前页数据
	 */
	Page<Organization> selPageList(Page<Organization> page,Organization organization);
	
	/**
	 * 查询导出数据
	 * @param organization
	 * @return
	 */
	List<Organization> exportData(Organization organization);
}
