package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.request.SettlementStatisticRequest;
import com.yxhl.stationbiz.system.domain.request.TicketCheckRequest;
import com.yxhl.stationbiz.system.domain.response.SettlementStatisticResponse;

/**
 *	
 *  bs_schedule_busDao
 *  注释:班次
 *  创建人: lw
 *  创建日期:2018-7-10 16:42:40
 */
@Mapper
@Repository
public interface ScheduleBusDao extends CrudDao<ScheduleBus>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param scheduleBus 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBus> selPageList(Pagination page,ScheduleBus scheduleBus);
	
	/**
	 * 排班班次分页查询
	 * @param page 分页参数
	 * @param scheduleBus 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBus> selSchedulePageList(Pagination page,ScheduleBus scheduleBus);
	
	/**
	 * 班次检票班次列表-分页查询
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBus> checkBusPageList(Pagination page,TicketCheckRequest scheduleBus);
	
	/**
	 * 结算单-分页查询
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	List<SettlementStatisticResponse> selSettlementStatisticPageList(Pagination page,SettlementStatisticRequest scheduleBus);
	
	/**
	 * 结算单导出查询
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	List<SettlementStatisticResponse> expSettlementStatisticList(SettlementStatisticRequest req);
	
	
	/**
	 * 班次检票班次列表-分页查询
	 * @param page 分页参数
	 * @param TicketCheckRequest 条件参数
	 * @return 当前页数据
	 */
	List<ScheduleBus> queryScheduleBusList(Pagination page,ScheduleBus scheduleBus);
	
	
	/**
	 * 查询配载的   【从属班次】   可售座位和已售座位数
	 * @param scheduleBusId
	 * @return seatStatus 2 表示可售车票数   5 表示已售车票数
	 *         counts  具体数值
	 */
	List<Map<String,Object>> queryBelongStowageSeats(@Param("scheduleBusId") String scheduleBusId);
	
	
	/**
	 * 查询配载的  【待选班次】   可售座位和已售座位数 和从属的班次编号
	 * @param scheduleBusId
	 * @return seatStatus 2 表示可售车票数   5 表示已售车票数
	 *  belongBusId  从属班次编号  
	 *  counts  具体数值
	 */
	List<Map<String,Object>> queryCandidateStowageSeats(@Param("scheduleBusId") String scheduleBusId);
	
	/**
	 * 查询一条班次   可售座位数  和已售座位数
	 * @param scheduleBusId
	 * @return seatStatus 2 表示可售车票数   5 表示已售车票数
	 *  counts  具体数值
	 */
	List<Map<String,Object>> queryStowageByScheduleId(@Param("scheduleBusId") String scheduleBusId);
	
	
	
}