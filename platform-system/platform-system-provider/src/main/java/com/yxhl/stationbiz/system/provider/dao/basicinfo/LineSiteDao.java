package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;

/**
 *	
 *  bs_line_siteDao
 *  注释:线路停靠点
 *  创建人: lw
 *  创建日期:2018-7-11 9:56:16
 */
@Mapper
@Repository
public interface LineSiteDao extends CrudDao<LineSite>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param lineSite 条件参数
	 * @return 当前页数据
	 */
	List<LineSite> selPageList(Pagination page,LineSite lineSite);

	/**
	 * 查询
	 * @param 
	 * @param lineSite 条件参数
	 * @return
	 */
	List<LineSite> selList(LineSite lineSite);
	
	/**
	 * 查询用于升序的对象
	 * @param sort
	 * @return
	 */
	LineSite selSortMaxSite(@Param("sort") Integer sort,@Param("lineId") String lineId);

	/**
	 * 查询用于降序的对象
	 * @param sort
	 * @return
	 */
	LineSite selSortMinSite(@Param("sort") Integer sort,@Param("lineId") String lineId);
	
		
	/**
	 * 查询 
	 * @param 
	 * @return
	 */
	LineSite selOne(@Param("id") String id);
	
}