package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;

/**
 *  DriverVehicleService
 *  注释:驾驶员车辆绑定表Service
 *  创建人: xjh
 *  创建日期:2018-7-9 17:39:14
 */
public interface DriverVehicleService extends IELService<DriverVehicle>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param driverVehicle 条件参数
	 * @return 当前页数据
	 */
	Page<DriverVehicle> selPageList(Page<DriverVehicle> page,DriverVehicle driverVehicle);
	
	public DriverVehicle getOne(String id);
	
	public boolean add(DriverVehicle driverVehicle);
	
	public boolean updateDv(DriverVehicle driverVehicle);
	
	public boolean deleteBydrId(List<String> driverId);
	
	/**
	 * 查询导出数据
	 * @param driverVehicle
	 * @return
	 */
	List<DriverVehicle> exportData(DriverVehicle driverVehicle);
	
}
