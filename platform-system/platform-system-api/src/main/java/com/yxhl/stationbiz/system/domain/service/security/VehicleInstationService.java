package com.yxhl.stationbiz.system.domain.service.security;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleInstation;

/**
 *  VehicleInstationService
 *  注释:车辆进站表Service
 *  创建人: ypf
 *  创建日期:2018-8-13 16:52:37
 */
public interface VehicleInstationService extends IELService<VehicleInstation>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleInstation 条件参数
	 * @return 当前页数据
	 */
	Page<VehicleInstation> selPageList(Page<VehicleInstation> page,VehicleInstation vehicleInstation);
}
