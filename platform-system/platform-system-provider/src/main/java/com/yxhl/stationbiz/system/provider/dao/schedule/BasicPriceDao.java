package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.response.BusPriceResp;
import com.yxhl.stationbiz.system.domain.response.SeatCategoryResp;

/**
 *	
 *  bs_basic_priceDao
 *  注释:基础票价表
 *  创建人: xjh
 *  创建日期:2018-8-20 10:53:51
 */
@Mapper
@Repository
public interface BasicPriceDao extends CrudDao<BasicPrice>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param basicPrice 条件参数
	 * @return 当前页数据
	 */
	List<BasicPrice> selPageList(Pagination page,BasicPrice basicPrice);
	
	/**
	 * 班次模板信息
	 * @param busPrice
	 * @return
	 */
	List<BasicPrice> getBusPrice(BasicPrice busPrice);
	
	/**
	 * 查班次模板座位类型
	 * @param busPrice
	 * @return
	 */
	List<SeatCategoryResp> getSeatCategory(BasicPrice busPrice);
	
	List<BasicPrice>  getSeatCategoryList(BasicPrice basicPrice);
	/**
	 * 根据班次模板id查票价
	 * @param basicPrice
	 * @return
	 */
	List<BasicPrice> getPriceList(BasicPrice basicPrice);
}