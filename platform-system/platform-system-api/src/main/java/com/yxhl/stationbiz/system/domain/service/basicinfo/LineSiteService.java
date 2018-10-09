package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;

/**
 *  LineSiteService
 *  注释:线路停靠点Service
 *  创建人: lw
 *  创建日期:2018-7-11 9:56:16
 */
public interface LineSiteService extends IELService<LineSite>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param lineSite 条件参数
	 * @return 当前页数据
	 */
	Page<LineSite> selPageList(Page<LineSite> page,LineSite lineSite);

	

	/**
	 * 排序
	 * @param lineSiteId
	 * @param sortType
	 * @return
	 */
	boolean sort(String lineSiteId, String sortType); 
	
	LineSite selOne(String id);


	/**
	 * 查询list
	 * @param lineSite
	 * @return
	 */
	List<LineSite> selList(LineSite lineSite);
}
