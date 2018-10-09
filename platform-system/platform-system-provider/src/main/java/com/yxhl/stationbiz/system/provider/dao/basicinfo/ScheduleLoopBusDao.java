package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoopBus;

/**
 *	
 *  bs_schedule_loop_busDao
 *  注释:班次循环配置中间表
 *  创建人: lw
 *  创建日期:2018-7-11 9:31:17
 */
@Mapper
@Repository
public interface ScheduleLoopBusDao extends CrudDao<ScheduleLoopBus>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleLoopBus 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleLoopBus> selPageList(Pagination page,ScheduleLoopBus scheduleLoopBus);
}