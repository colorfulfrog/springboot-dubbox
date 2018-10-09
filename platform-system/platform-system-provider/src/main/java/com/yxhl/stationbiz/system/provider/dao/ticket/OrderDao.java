package com.yxhl.stationbiz.system.provider.dao.ticket;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;

/**
 *	
 *  bs_orderDao
 *  注释:订单表
 *  创建人: lw
 *  创建日期:2018-9-15 10:39:27
 */
@Mapper
@Repository
public interface OrderDao extends CrudDao<Order>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param order 条件参数
	 * @return 当前页数据
	 */
	List<Order> selPageList(Pagination page,Order order);
}