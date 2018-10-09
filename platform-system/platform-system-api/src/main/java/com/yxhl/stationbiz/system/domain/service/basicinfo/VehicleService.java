package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;

/**
 * 车辆管理
 * @author xjh
 *
 */
public interface VehicleService extends IELService<Vehicle> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Vehicle> selPageList(Page<Vehicle> page,Vehicle vehicle);
	
	public boolean add (Vehicle vehicle);
	
	public boolean updatev(Vehicle vehicle);
	
	/**
	 * 查询导出数据
	 * @param vehicle
	 * @return
	 */
	List<Vehicle> exportData(Vehicle vehicle);
	
	/**
	 * 根据车牌号查车辆信息
	 * @param vehicle
	 * @return
	 */
	List<Vehicle> getVehicle(Vehicle vehicle);
}
