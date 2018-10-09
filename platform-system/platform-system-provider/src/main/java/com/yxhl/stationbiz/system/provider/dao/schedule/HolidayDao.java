package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;

/**
 *	
 *  bs_holidayDao
 *  注释:节日设置表
 *  创建人: ypf
 *  创建日期:2018-8-13 17:41:22
 */
@Mapper
@Repository
public interface HolidayDao extends CrudDao<Holiday>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param holiday 条件参数
	 * @return 当前页数据
	 */
	List<Holiday> selPageList(Pagination page,Holiday holiday);
}