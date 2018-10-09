package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;

/**
 *	
 *  bs_ticket_categoryDao
 *  注释:票种设置表
 *  创建人: xjh
 *  创建日期:2018-8-16 10:25:40
 */
@Mapper
@Repository
public interface TicketCategoryDao extends CrudDao<TicketCategory>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketCategory 条件参数
	 * @return 当前页数据
	 */
	List<TicketCategory> selPageList(Pagination page,TicketCategory ticketCategory);
}