package com.yxhl.stationbiz.system.provider.dao.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleInstation;

/**
 *	
 *  bs_vehicle_instationDao
 *  注释:车辆进站表
 *  创建人: ypf
 *  创建日期:2018-8-13 16:52:37
 */
@Mapper
@Repository
public interface VehicleInstationDao extends CrudDao<VehicleInstation>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleInstation 条件参数
	 * @return 当前页数据
	 */
	List<VehicleInstation> selPageList(Pagination page,VehicleInstation vehicleInstation);
}