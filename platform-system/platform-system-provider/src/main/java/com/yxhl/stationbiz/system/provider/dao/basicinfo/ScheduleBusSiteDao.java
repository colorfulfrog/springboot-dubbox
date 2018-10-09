package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusSite;

/**
 *	
 *  bs_schedule_bus_siteDao
 *  注释:班次停靠点
 *  创建人: lw
 *  创建日期:2018-7-12 19:15:10
 */
@Mapper
@Repository
public interface ScheduleBusSiteDao extends CrudDao<ScheduleBusSite>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusSite 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBusSite> selPageList(Pagination page,ScheduleBusSite scheduleBusSite);
	
	/**
	 * 查询用于升序的对象
	 * @param sort
	 * @return
	 */
	ScheduleBusSite selSortMaxSite(@Param("sort") Integer sort,@Param("scheduleBusTplId") String scheduleBusTplId);

	
	/**
	 * 查询用于降序的对象
	 * @param sort
	 * @return
	 */
	ScheduleBusSite selSortMinSite(@Param("sort") Integer sort,@Param("scheduleBusTplId") String scheduleBusTplId);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ScheduleBusSite selOne(@Param("id") String id);
	
	/**
	 * 根据班次模板id，站点名称查停靠点
	 * @param st
	 * @return
	 */
	List<ScheduleBusSite> getStation(ScheduleBusSite st);
}