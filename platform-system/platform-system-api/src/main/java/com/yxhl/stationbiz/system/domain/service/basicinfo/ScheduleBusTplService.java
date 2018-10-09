package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;

/**
 *  ScheduleBusTplService
 *  注释:班次模板Service
 *  创建人: lw
 *  创建日期:2018-7-11 9:58:54
 */
public interface ScheduleBusTplService extends IELService<ScheduleBusTpl>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBusTpl 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBusTpl> selPageList(Page<ScheduleBusTpl> page,ScheduleBusTpl scheduleBusTpl);

	/**
	 * 添加班次模板
	 * @param scheduleBusTpl
	 * @return
	 */
	boolean addScheduleBusTpl(ScheduleBusTpl scheduleBusTpl);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	ScheduleBusTpl selOne(String id);

	/**
	 * 修改班次模板
	 * @param scheduleBusTpl
	 * @return
	 */
	boolean updateScheduleBusTpl(ScheduleBusTpl scheduleBusTpl);

	/**
	 * 删除班次模板
	 * @param scheduleBusTplId
	 * @return
	 */
	boolean delScheduleBusTpl(List<String> scheduleBusTplId); 
	/**
	 * 根据线路ID查询已经配置循环的班次模板信息
	 * @param lineId
	 * @return
	 */
	List<ScheduleBusTpl> selScheduleTplByLineId(String lineId); 

}
