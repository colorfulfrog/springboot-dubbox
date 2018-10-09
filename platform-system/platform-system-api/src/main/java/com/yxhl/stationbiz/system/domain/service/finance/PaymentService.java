package com.yxhl.stationbiz.system.domain.service.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.finance.Payment;
import com.yxhl.stationbiz.system.domain.response.PaymentResp;

/**
 *  PaymentService
 *  注释:缴款表Service
 *  创建人: xjh
 *  创建日期:2018-9-14 11:26:25
 */
public interface PaymentService extends IELService<Payment>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param payment 条件参数
	 * @return 当前页数据
	 */
	Page<Payment> selPageList(Page<Payment> page,Payment payment);
	
	/**
	 * 添加缴款
	 * @param payment
	 * @return
	 */
	boolean add(Payment payment);
	
	/**
	 * 售票模块/缴款分页查询
	 * @param page 分页参数
	 * @param ticket 条件参数
	 * @return 当前页数据
	 */
	Page<PaymentResp> jkPageList(Page<PaymentResp> page,Payment payment);
}
