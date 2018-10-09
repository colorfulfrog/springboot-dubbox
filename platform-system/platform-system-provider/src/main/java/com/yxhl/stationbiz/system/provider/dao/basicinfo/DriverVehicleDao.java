package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;

/**
 *	
 *  bs_driver_vehicleDao
 *  注释:驾驶员车辆绑定表
 *  创建人: xjh
 *  创建日期:2018-7-9 17:39:14
 */
@Mapper
@Repository
public interface DriverVehicleDao extends CrudDao<DriverVehicle>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param driverVehicle 条件参数
	 * @return 当前页数据
	 */
	List<DriverVehicle> selPageList(Pagination page,DriverVehicle driverVehicle);
	
	public DriverVehicle getOne(String id);
	
	public boolean deleteBydrId(String sjId);
	
	/**
	 * 查询导出数据
	 * @param driverVehicle
	 * @return
	 */
	List<DriverVehicle> exportData(DriverVehicle driverVehicle);
}