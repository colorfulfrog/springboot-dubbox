package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusSite;

/**
 *  ScheduleBusSiteService
 *  注释:班次停靠点Service
 *  创建人: lw
 *  创建日期:2018-7-12 19:15:10
 */
public interface ScheduleBusSiteService extends IELService<ScheduleBusSite>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusSite 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBusSite> selPageList(Page<ScheduleBusSite> page,ScheduleBusSite scheduleBusSite);

	/**
	 * 排序
	 * @param busSiteId
	 * @param sortType
	 * @return
	 */
	boolean sort(String busSiteId, String sortType);
	
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
