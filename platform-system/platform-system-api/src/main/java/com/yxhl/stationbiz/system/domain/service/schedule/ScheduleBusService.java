package com.yxhl.stationbiz.system.domain.service.schedule;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.request.BusStowageRequest;
import com.yxhl.stationbiz.system.domain.request.CreateScheduleBusRequest;
import com.yxhl.stationbiz.system.domain.request.ScheduleDepartRequest;
import com.yxhl.stationbiz.system.domain.request.SettlementStatisticRequest;
import com.yxhl.stationbiz.system.domain.request.TicketCheckRequest;
import com.yxhl.stationbiz.system.domain.response.SettlementStatisticResponse;

/**
 *  ScheduleBusService
 *  注释:班次Service
 *  创建人: lw
 *  创建日期:2018-7-10 16:42:40
 */
public interface ScheduleBusService extends IELService<ScheduleBus>{
	
	/**
	 * 报班班次分页查询
	 * @param page 分页参数
	 * @param scheduleBus 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBus> selPageList(Page<ScheduleBus> page,ScheduleBus scheduleBus);
	
	/**
	 * 排班班次分页查询
	 * @param page 分页参数
	 * @param scheduleBus 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBus> selSchedulePageList(Page<ScheduleBus> page,ScheduleBus scheduleBus);
	
	/**
	 *  售票   查询班次
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBus> queryScheduleBusList(Page<ScheduleBus> page,ScheduleBus scheduleBus);
	
	/**
	 * 结算单分页查询
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	Page<SettlementStatisticResponse> selSettlementStatisticPageList(Page<SettlementStatisticResponse> page,SettlementStatisticRequest req);
	
	/**
	 * 结算单导出查询
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	List<SettlementStatisticResponse> expSettlementStatisticList(SettlementStatisticRequest req);
	
	/**
	 * 根据循环配置生成班次
	 */
	void createScheduleTask();

	/**
	 * 制作运营计划
	 * @param req 制作运营计划请求参数
	 * @return
	 */
	void createOperatePlan(CreateScheduleBusRequest req);

	/**
	 * 执行制作运营计划任务
	 * @param req
	 */
	void createOperPlanTask(CreateScheduleBusRequest req);
	
	/**
	 * 添加加班班次
	 * @param scheduleBus
	 * @return
	 */
	boolean addOverTimeBus(ScheduleBus scheduleBus);

	/**
	 * 并班
	 * @param beMergedBusId 被并班次
	 * @param mergeBusId 并入班次
	 * @param mergeReason 并班原因
	 * @return
	 */
	boolean mergeBus(String beMergedBusId, String mergeBusId, String mergeReason);

	/**
	 * 配载
	 * @param req 参数
	 * @return
	 */
	boolean stowageBus(BusStowageRequest req);

	/**
	 * 取消发班
	 * @param scheduleBus
	 * @return
	 */
	boolean cancleDepart(ScheduleBus scheduleBus);

	/**
	 * 发班
	 * @param req
	 * @param biller 开单人
	 * @return
	 */
	boolean depart(ScheduleDepartRequest req,String biller);
	
	
	/**
	 * 检票班次分页查询
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	Page<ScheduleBus> checkBusPageList(Page<ScheduleBus> page,TicketCheckRequest req);
	
	
}
