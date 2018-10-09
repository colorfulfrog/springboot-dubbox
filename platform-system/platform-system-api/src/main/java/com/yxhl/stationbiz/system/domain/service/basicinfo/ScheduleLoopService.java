package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;

/**
 *  ScheduleLoopService
 *  注释:班次调度循环配置Service
 *  创建人: lw
 *  创建日期:2018-7-11 9:27:23
 */
public interface ScheduleLoopService extends IELService<ScheduleLoop>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleLoop 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleLoop> selPageList(Page<ScheduleLoop> page,ScheduleLoop scheduleLoop);
	
	/**
	 * 添加班次循环配置
	 * @param scheduleLoop
	 * @return
	 */
	boolean addScheduleLoop(ScheduleLoop scheduleLoop);
	
	/**
	 * 修改班次循环配置
	 * @param scheduleLoop
	 * @return
	 */
	boolean updateScheduleLoop(ScheduleLoop scheduleLoop);
	
	/**
	 * 批量删除班次循环配置
	 * @param ids
	 * @return
	 */
	boolean delScheduleLoops(List<String> ids);

	/**
	 * 查看详情
	 * @param scheduleLoopId
	 * @return
	 */
	ScheduleLoop getInfoById(String scheduleLoopId);
	
	/**
	 * 查询导出数据
	 * @param scheduleLoop 参数
	 * @return
	 */
	List<ScheduleLoop> exportData(ScheduleLoop scheduleLoop);
}
