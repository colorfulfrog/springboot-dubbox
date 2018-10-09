package com.yxhl.stationbiz.system.provider.dao.check;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.check.VehicleOutboundCheck;

/**
 *	
 *  bs_vehicle_outbound_checkDao
 *  注释:车辆出站稽查表
 *  创建人: xjh
 *  创建日期:2018-9-12 10:22:24
 */
@Mapper
@Repository
public interface VehicleOutboundCheckDao extends CrudDao<VehicleOutboundCheck>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleOutboundCheck 条件参数
	 * @return 当前页数据
	 */
	List<VehicleOutboundCheck> selPageList(Pagination page,VehicleOutboundCheck vehicleOutboundCheck);
	
	/**
	 * 根据班次ID查询稽查信息
	 * @param scheduleIds 班次ID列表
	 * @return
	 */
	List<VehicleOutboundCheck> selByScheduleIds(List<String> scheduleIds);
	
	/**
	 * 查班次信息
	 * @param page 分页参数
	 * @param vehicleOutboundCheck 条件参数
	 * @return 当前页数据
	 */
	List<VehicleOutboundCheck> getBus(Pagination page,VehicleOutboundCheck vehicleOutboundCheck);
	
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
}