package com.yxhl.stationbiz.system.domain.service.schedule;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;

/**
 *  HolidayService
 *  注释:节日设置表Service
 *  创建人: ypf
 *  创建日期:2018-8-13 17:41:22
 */
public interface HolidayService extends IELService<Holiday>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param holiday 条件参数
	 * @return 当前页数据
	 */
	Page<Holiday> selPageList(Page<Holiday> page,Holiday holiday);
}
