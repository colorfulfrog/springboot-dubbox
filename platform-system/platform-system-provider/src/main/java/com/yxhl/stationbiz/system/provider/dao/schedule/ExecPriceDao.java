package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;

/**
 *	
 *  bs_exec_priceDao
 *  注释:执行票价表
 *  创建人: lw
 *  创建日期:2018-8-21 10:23:17
 */
@Mapper
@Repository
public interface ExecPriceDao extends CrudDao<ExecPrice>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param execPrice 条件参数
	 * @return 当前页数据
	 */
	List<ExecPrice> selPageList(Pagination page,ExecPrice execPrice);
	
	
	/**
	 * 
	 * @param page
	 * @param execPrice
	 * @return
	 */
	List<ExecPrice> selExecPrice(@Param("scheduleBusId") String scheduleBusId);
	
	
	/**
	 * 根据 班次 和车型  座位数查询
	 * @param page
	 * @param execPrice
	 * @return
	 */
	List<TicketCateValue> selExecPriceBy(@Param("scheduleBusId") String scheduleBusId,@Param("seatCate") String seatCate,@Param("seats") Integer seats);
	
	
}