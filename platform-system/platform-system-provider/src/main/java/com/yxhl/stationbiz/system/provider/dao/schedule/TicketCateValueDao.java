package com.yxhl.stationbiz.system.provider.dao.schedule;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;

/**
 *	
 *  bs_ticket_cate_valueDao
 *  注释:票种取值表
 *  创建人: ypf
 *  创建日期:2018-8-15 14:12:46
 */
@Mapper
@Repository
public interface TicketCateValueDao extends CrudDao<TicketCateValue>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ticketCateValue 条件参数
	 * @return 当前页数据
	 */
	List<TicketCateValue> selPageList(Pagination page,TicketCateValue ticketCateValue);
	
	List<TicketCateValue> selPriceValue(TicketCateValue ticketCateValue);
	
	TicketCateValue selPriceValueForType(TicketCateValue ticketCateValue);
	
	/**
	 * 根据票价id删除
	 * @param priceId
	 * @return
	 */
	Integer delectByPriceId(String priceId);
}