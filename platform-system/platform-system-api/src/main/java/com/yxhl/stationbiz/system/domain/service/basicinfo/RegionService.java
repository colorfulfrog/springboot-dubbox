package com.yxhl.stationbiz.system.domain.service.basicinfo;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELTreeService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Region;

public interface RegionService extends IELTreeService<Region> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Region> selPageList(Page<Region> page,Region region);
}
