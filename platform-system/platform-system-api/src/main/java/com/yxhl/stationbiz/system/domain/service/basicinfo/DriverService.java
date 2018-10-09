package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;

/**
 * 司机管理
 * @author xjh
 *
 */
public interface DriverService extends IELService<Driver> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Driver> selPageList(Page<Driver> page,Driver driver);
	
	/**
	 * 查询导出数据
	 * @param driver
	 * @return
	 */
	List<Driver> exportData(Driver driver);
	
	public boolean add(Driver driver);
	
	public boolean  updated(Driver driver);
}
