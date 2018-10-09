package com.yxhl.stationbiz.system.provider.dao.ticket;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;

/**
 *	
 *  bs_schedule_bus_seatsDao
 *  注释:班次座位表
 *  创建人: lw
 *  创建日期:2018-9-13 9:57:59
 */
@Mapper
@Repository
public interface ScheduleBusSeatsDao extends CrudDao<ScheduleBusSeats>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusSeats 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBusSeats> selPageList(Pagination page,ScheduleBusSeats scheduleBusSeats);
	
	/**
	 * 修改座位状态为已售
	 * @param ids
	 * @return
	 */
	int updateScheduleBusSeatStatus(List<String> ids);
}