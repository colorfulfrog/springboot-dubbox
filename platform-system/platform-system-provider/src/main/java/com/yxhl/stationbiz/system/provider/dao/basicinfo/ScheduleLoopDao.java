package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;

/**
 *	
 *  bs_schedule_loopDao
 *  注释:班次调度循环配置
 *  创建人: lw
 *  创建日期:2018-7-11 9:27:23
 */
@Mapper
@Repository
public interface ScheduleLoopDao extends CrudDao<ScheduleLoop>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleLoop 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleLoop> selPageList(Pagination page,ScheduleLoop scheduleLoop);
	
	/**
	 * 查询所有循环配置(只查固定班次)
	 * @return
	 */
	List<ScheduleLoop> selAllScheduleLoop();
	
	/**
	 * 根据班次模板ID查询该班次的循环配置信息
	 * @param loop 参数
	 * @return
	 */
	List<ScheduleLoop> selScheduleLoop(ScheduleLoop loop);
	
	/**
	 * 查详情
	 * @param id
	 * @return
	 */
	ScheduleLoop getScheduleLoopById(String id);
	
	/**
	 * 查询导出数据
	 * @param scheduleLoop 参数
	 * @return
	 */
	List<ScheduleLoop> exportData(ScheduleLoop scheduleLoop);
}