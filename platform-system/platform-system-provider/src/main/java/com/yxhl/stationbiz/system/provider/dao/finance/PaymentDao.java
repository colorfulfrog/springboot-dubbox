package com.yxhl.stationbiz.system.provider.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.finance.Payment;
import com.yxhl.stationbiz.system.domain.response.PaymentResp;

/**
 *	
 *  bs_paymentDao
 *  注释:缴款表
 *  创建人: xjh
 *  创建日期:2018-9-14 11:26:25
 */
@Mapper
@Repository
public interface PaymentDao extends CrudDao<Payment>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param payment 条件参数
	 * @return 当前页数据
	 */
	List<Payment> selPageList(Pagination page,Payment payment);
	
	/**
	 * 售票模块/缴款分页查询 缴款类型电脑票
	 * @param page 分页参数
	 * @param payment 条件参数
	 * @return 当前页数据
	 */
	List<PaymentResp> jkPageList(Pagination page,Payment payment);
	
	/**
	 * 售票模块/缴款分页查询 缴款类型保险票
	 * @param page 分页参数
	 * @param payment 条件参数
	 * @return 当前页数据
	 */
	List<PaymentResp> jkPageList2(Pagination page,Payment payment);
}