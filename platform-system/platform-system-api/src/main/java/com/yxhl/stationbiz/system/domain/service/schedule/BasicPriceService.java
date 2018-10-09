package com.yxhl.stationbiz.system.domain.service.schedule;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.request.BusPriceRequest;
import com.yxhl.stationbiz.system.domain.response.BusPriceResp;
import com.yxhl.stationbiz.system.domain.response.SeatCategoryResp;

/**
 *  BasicPriceService
 *  注释:基础票价表Service
 *  创建人: xjh
 *  创建日期:2018-8-20 10:53:51
 */
public interface BasicPriceService extends IELService<BasicPrice>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param basicPrice 条件参数
	 * @return 当前页数据
	 */
	Page<BasicPrice> selPageList(Page<BasicPrice> page,BasicPrice basicPrice);
	
	/**
	 * 班次模板分页查询
	 * @param page 分页参数
	 * @param scheduleBusTpl 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBusTpl> selBusPageList(Page<ScheduleBusTpl> page,ScheduleBusTpl scheduleBusTpl,String Type);
	
	/**
	 * 查班次模板信息
	 * @param busPrice
	 * @return
	 */
	List<BasicPrice> getBusPrice(BasicPrice busPrice);
	
	boolean add(List<BusPriceRequest> req,String lineCopyFlag)throws Exception;
	
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
	
	/**
	 * 修改票价取值表
	 * @param req
	 * @return
	 */
	boolean updatePrice(List<BusPriceRequest> req,String lineCopyFlag)throws Exception;
	
	/**
	 * 设置站点票价
	 * @param req
	 * @return
	 */
	boolean addStaPrice(BusPriceRequest req);
	
	/**
	 * 复制票价
	 * @param req
	 * @return
	 */
	boolean copyPrice(BusPriceRequest req);
	
	/**
	 * 删除票价
	 * @param priceId
	 * @return
	 */
	boolean delectPrice(List<String> priceId);
}
