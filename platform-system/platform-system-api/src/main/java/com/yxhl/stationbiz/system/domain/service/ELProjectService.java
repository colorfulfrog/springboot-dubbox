package com.yxhl.stationbiz.system.domain.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELTreeService;
import com.yxhl.stationbiz.system.domain.entity.ELProject;

/**
 * @author liwei
 * @title: 项目服务接口
 */
public interface ELProjectService extends IELTreeService<ELProject>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<ELProject> selPageList(Page<ELProject> page,ELProject project);
}
