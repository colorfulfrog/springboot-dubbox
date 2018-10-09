package com.yxhl.stationbiz.system.domain.service.security;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleSecurityCheck;

/**
 *  VehicleSecurityCheckService
 *  注释:车辆安检表Service
 *  创建人: xjh
 *  创建日期:2018-8-13 17:04:26
 */
public interface VehicleSecurityCheckService extends IELService<VehicleSecurityCheck>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleSecurityCheck 条件参数
	 * @return 当前页数据
	 */
	Page<VehicleSecurityCheck> selPageList(Page<VehicleSecurityCheck> page,VehicleSecurityCheck vehicleSecurityCheck);
	
	public boolean add(VehicleSecurityCheck vehicleSecurityCheck);
	
	/**
	 * 查询导出数据
	 * @param vehicleSecurityCheck
	 * @return
	 */
	public List<VehicleSecurityCheck> exportData(VehicleSecurityCheck vehicleSecurityCheck);
}
