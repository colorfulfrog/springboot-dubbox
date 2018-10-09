package com.yxhl.stationbiz.system.domain.service.basicinfo;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.BusEntrance;

/**
 *  BusEntranceService
 *  注释:乘车库表Service
 *  创建人: lw
 *  创建日期:2018-7-10 9:48:43
 */
public interface BusEntranceService extends IELService<BusEntrance>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param busEntrance 条件参数
	 * @return 当前页数据
	 */
	Page<BusEntrance> selPageList(Page<BusEntrance> page,BusEntrance busEntrance);
}
