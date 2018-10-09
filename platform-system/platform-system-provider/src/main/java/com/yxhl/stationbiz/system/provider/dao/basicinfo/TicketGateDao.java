package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.TicketGate;

/**
 *	
 *  bs_ticket_gateDao
 *  注释:检票口表
 *  创建人: lw
 *  创建日期:2018-7-10 9:28:58
 */
@Mapper
@Repository
public interface TicketGateDao extends CrudDao<TicketGate>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketGate 条件参数
	 * @return 当前页数据
	 */
	List<TicketGate> selPageList(Pagination page,TicketGate ticketGate);
}