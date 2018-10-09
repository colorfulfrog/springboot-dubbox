package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;

@Mapper
@Repository
public interface VehicleDao extends CrudDao<Vehicle> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Vehicle> selPageList(Pagination page,Vehicle vehicle);
	
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
