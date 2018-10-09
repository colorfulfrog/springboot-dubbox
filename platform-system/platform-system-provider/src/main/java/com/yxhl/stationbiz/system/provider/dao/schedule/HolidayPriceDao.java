package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;

/**
 *	
 *  bs_holiday_priceDao
 *  注释:节假日票价表
 *  创建人: ypf
 *  创建日期:2018-8-15 14:20:34
 */
@Mapper
@Repository
public interface HolidayPriceDao extends CrudDao<HolidayPrice>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param holidayPrice 条件参数
	 * @return 当前页数据
	 */
	List<HolidayPrice> selPageList(Pagination page,HolidayPrice holidayPrice);
	
	List<HolidayPrice> selholidayPriceList(HolidayPrice holidayPrice);
}