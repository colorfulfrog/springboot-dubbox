package com.yxhl.stationbiz.system.domain.service.check;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.check.VehicleOutboundCheck;

/**
 *  VehicleOutboundCheckService
 *  注释:车辆出站稽查表Service
 *  创建人: xjh
 *  创建日期:2018-9-12 10:22:24
 */
public interface VehicleOutboundCheckService extends IELService<VehicleOutboundCheck>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleOutboundCheck 条件参数
	 * @return 当前页数据
	 */
	Page<VehicleOutboundCheck> selPageList(Page<VehicleOutboundCheck> page,VehicleOutboundCheck vehicleOutboundCheck);
	
	/**
	 * 根据班次ID查询出站信息
	 * @param scheduleIds 班次ID列表
	 * @return
	 */
	List<VehicleOutboundCheck> selByScheduleIds(List<String> scheduleIds);
	
	/**
	 * 班次信息
	 * @param page
	 * @param vehicleOutboundCheck
	 * @return
	 */
//	Page<BusVehicleOutResp> getBusPage(Page<BusVehicleOutResp> page,VehicleOutboundCheck vehicleOutboundCheck);
	
	/**
	 * 根据id查车辆出站稽查表
	 * @param vochekId
	 * @return
	 */
	VehicleOutboundCheck getOne(String vochekId);
	
	/**
	 * 根据班次id查出站信息
	 * @param busId
	 * @return
	 */
	VehicleOutboundCheck getByBusId(String busId);
	
	/**
	 * 添加
	 * @param vehicleOutboundCheck
	 * @return
	 */
	boolean add(VehicleOutboundCheck vehicleOutboundCheck);
}
