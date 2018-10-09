package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;

/**
 * 站点管理
 * @author xjh
 *
 */
public interface StationService extends IELService<Station> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	Page<Station> selPageList(Page<Station> page,Station Station);
	
	public boolean add(Station station);
	
	public boolean updateSta(Station station);
	
	/**
	 * 查询导出数据
	 * @param station
	 * @return
	 */
	List<Station> exportData(Station station);
}
