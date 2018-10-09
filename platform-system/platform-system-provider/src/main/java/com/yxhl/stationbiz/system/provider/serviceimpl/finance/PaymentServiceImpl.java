package com.yxhl.stationbiz.system.provider.serviceimpl.finance;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.finance.Payment;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.response.PaymentResp;
import com.yxhl.stationbiz.system.domain.service.finance.PaymentService;
import com.yxhl.stationbiz.system.provider.dao.finance.PaymentDao;
import com.yxhl.stationbiz.system.provider.dao.finance.TicketSellerBillDao;
/**
 * @ClassName: PaymentServiceImpl
 * @Description: 缴款表 serviceImpl
 * @author xjh
 * @date 2018-9-14 11:26:25
 */
@Transactional(readOnly = true)
@Service("paymentService")
public class PaymentServiceImpl extends CrudService<PaymentDao, Payment> implements PaymentService {
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private TicketSellerBillDao ticketSellerBillDao;

	@Override
	public Page<Payment> selPageList(Page<Payment> page, Payment payment) {
		List<Payment> list = paymentDao.selPageList(page, payment);
		page.setRecords(list);
		return page;
	}

	/**
	 * 添加缴款
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(Payment payment) {
		//缴款时间
		payment.setPaymentTime(new Date());
		TicketSellerBill bill = new TicketSellerBill();
		bill.setOrgId(payment.getOrgId());
		bill.setCompId(payment.getCompId());
		bill.setReceiveUser(payment.getPayer());	//售票员
		bill.setBillType(payment.getPaymentType());	//票据类型
		//查当前缴款人票据信息
		TicketSellerBill selectOne = ticketSellerBillDao.selectOne(bill);
		if(selectOne == null) {
			throw new YxBizException("前缴款人没有票据信息!");
		}else {
			int cnum = Integer.parseInt(selectOne.getCurrentNum());	//当前售票员的当前票号
			int pnum = cnum-1;	//当前售票员的当前票号-1
			Payment pt = new Payment();
			pt.setOrgId(payment.getOrgId());
			pt.setCompId(payment.getCompId());
			pt.setPayer(payment.getPayer());
			pt.setPaymentNum(String.valueOf(pnum));
			pt.setPaymentType(payment.getPaymentType());
			//根据缴款人，缴款单号,缴款类型查当前缴款人是否存在这个缴款单号
			Payment selectOne2 = paymentDao.selectOne(pt);
			if(selectOne2!=null) {
				//存在则在当前缴款单号基础上+1
				int oldNum = Integer.parseInt(selectOne2.getPaymentNum());
				payment.setPaymentNum(String.valueOf(oldNum+1));
				
			}else {		
				//当前缴款人不存在这个缴款单号，则取当前售票员的当前票号-1
				payment.setPaymentNum(String.valueOf(pnum));
			}
		}
		paymentDao.insert(payment);
		return true;
	}

	/**
	 * 售票模块/缴款分页查询
	 */
	@Override
	public Page<PaymentResp> jkPageList(Page<PaymentResp> page, Payment payment) {
		//电脑票缴款记录
		List<PaymentResp> list = paymentDao.jkPageList(page, payment);
		respList(list);
		//保险票缴款记录
		List<PaymentResp> list2 = paymentDao.jkPageList2(page, payment);
		respList(list2);
		list.addAll(list2);
		page.setRecords(list);
		return page;
	}
	
	public List<PaymentResp> respList(List<PaymentResp> list){
		for (PaymentResp resp : list) {
			Float ticketFee = resp.getTicketFee();	//售票金额
			Float refundFee = resp.getRefundFee();	//退票金额
			Float scrapFee = resp.getScrapFee();	//废票金额
			Float refundCharge = resp.getRefundCharge();	//退款手续费
			//应缴款 = 售票金额 - 退票金额 - 废票金额 + 退款手续费
			Float fee = (ticketFee - refundFee - scrapFee) + refundCharge;
			resp.setYjk(fee);
			Float paymentFee = resp.getPaymentFee();  //实缴款金额
			//欠款 = 应缴款 - 实缴款金额
			Float arrearsFee = fee - paymentFee;
			resp.setArrearsFee(arrearsFee);
			if(arrearsFee == 0.0) {
				resp.setWcFlag(1);  //缴款完成
			}else {
				resp.setWcFlag(0);  //缴款没完成
			}
			if(resp.getPaymentFee() == 0.0) {
				list.remove(resp);
				break;
			}
		}
		return list;
	}
}
