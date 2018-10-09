package com.yxhl.stationbiz.system.domain.service.schedule;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;
import com.yxhl.stationbiz.system.domain.request.HolidayPriceRequest;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;

/**
 *  HolidayPriceService
 *  注释:节假日票价表Service
 *  创建人: ypf
 *  创建日期:2018-8-15 14:20:34
 */
public interface HolidayPriceService extends IELService<HolidayPrice>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param holidayPrice 条件参数
	 * @return 当前页数据
	 */
	Page<HolidayPrice> selPageList(Page<HolidayPrice> page,HolidayPrice holidayPrice);
	
	List<HolidayPrice> selholidayPriceList(HolidayPrice holidayPrice);
	
	public List<HolidayPriceResponse> dynamic(List<String> ids,String type,String org_id,String comp_id);
	
	public List<HolidayPriceResponse> dynamicForTitle(String org_id,String comp_id);
	
	public boolean addHolidayPrice(HolidayPriceRequest req,String userid,String id)throws Exception;
	
	/**
	 * 判断班次是否设置了节假日票价,并返回节假日的ID
	 * @param bus 班次
	 * @return
	 */
	String isHolidayPriceSet(ScheduleBus bus);
}
