package com.yxhl.stationbiz.system.domain.service.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELTreeService;
import com.yxhl.stationbiz.system.domain.entity.finance.BillEntry;

/**
 *  BillEntryService
 *  注释:票据录入表Service
 *  创建人: ypf
 *  创建日期:2018-9-12 11:16:57
 */
public interface BillEntryService extends IELTreeService<BillEntry>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param billEntry 条件参数
	 * @return 当前页数据
	 */
	Page<BillEntry> selPageList(Page<BillEntry> page,BillEntry billEntry);
}
