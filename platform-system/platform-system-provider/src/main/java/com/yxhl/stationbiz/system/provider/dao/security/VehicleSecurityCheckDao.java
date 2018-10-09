package com.yxhl.stationbiz.system.provider.dao.security;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleSecurityCheck;

/**
 *	
 *  bs_vehicle_security_checkDao
 *  注释:车辆安检表
 *  创建人: xjh
 *  创建日期:2018-8-13 17:04:26
 */
@Mapper
@Repository
public interface VehicleSecurityCheckDao extends CrudDao<VehicleSecurityCheck>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param vehicleSecurityCheck 条件参数
	 * @return 当前页数据
	 */
	List<VehicleSecurityCheck> selPageList(Pagination page,VehicleSecurityCheck vehicleSecurityCheck);
	
	/**
	 * 查询导出数据
	 * @param vehicleSecurityCheck
	 * @return
	 */
	public List<VehicleSecurityCheck> exportData(VehicleSecurityCheck vehicleSecurityCheck);
}