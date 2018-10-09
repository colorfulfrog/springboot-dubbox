package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBusStowage;

/**
 *	
 *  bs_schedule_bus_stowageDao
 *  注释:班次配载表
 *  创建人: lw
 *  创建日期:2018-9-15 9:49:26
 */
@Mapper
@Repository
public interface ScheduleBusStowageDao extends CrudDao<ScheduleBusStowage>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusStowage 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBusStowage> selPageList(Pagination page,ScheduleBusStowage scheduleBusStowage);
}