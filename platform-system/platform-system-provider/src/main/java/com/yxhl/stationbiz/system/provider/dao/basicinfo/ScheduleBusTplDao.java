package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;

/**
 *	
 *  bs_schedule_bus_tplDao
 *  注释:班次模板
 *  创建人: lw
 *  创建日期:2018-7-11 9:58:54
 */
@Mapper
@Repository
public interface ScheduleBusTplDao extends CrudDao<ScheduleBusTpl>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusTpl 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBusTpl> selPageList(Pagination page,ScheduleBusTpl scheduleBusTpl);
	
	/**
	 * 查询班次循环已设置的班次模板
	 * @param scheduleLoopId 班次循环表ID
	 * @return 班次模板
	 */
	List<ScheduleBusTpl> selTplByLoopId(String scheduleLoopId);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */

	ScheduleBusTpl selOne(@Param("id") String id);

	
	/**
	 * 根据线路ID查询已经配置循环的班次模板信息
	 * @param lineId
	 * @return
	 */
	List<ScheduleBusTpl> selScheduleTplByLineId(String lineId);
	
	
	/**
	 * 票价管理 班次模板分页查询
	 * @param page 分页参数
	 * @param scheduleBusTpl 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBusTpl> selBusPageList(Pagination page,ScheduleBusTpl scheduleBusTpl);

}