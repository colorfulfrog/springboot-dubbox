package com.yxhl.stationbiz.system.provider.serviceimpl.finance;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.finance.TicketSellerBillDao;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.service.finance.TicketSellerBillService;
/**
 * @ClassName: TicketSellerBillServiceImpl
 * @Description: 售票员票据表 serviceImpl
 * @author ypf
 * @date 2018-9-12 11:45:23
 */
@Transactional(readOnly = true)
@Service("ticketSellerBillService")
public class TicketSellerBillServiceImpl extends CrudService<TicketSellerBillDao, TicketSellerBill> implements TicketSellerBillService {
	@Autowired
	private TicketSellerBillDao ticketSellerBillDao;

	@Override
	public Page<TicketSellerBill> selPageList(Page<TicketSellerBill> page, TicketSellerBill ticketSellerBill) {
		List<TicketSellerBill> list = ticketSellerBillDao.selPageList(page, ticketSellerBill);
		page.setRecords(list);
		return page;
	}

	@Override
	public Page<TicketSellerBill> companybillInfo(Page<TicketSellerBill> page, String billEntryId) {
		List<TicketSellerBill> list = ticketSellerBillDao.companybillInfo(page, billEntryId);
		page.setRecords(list);
		return page;
	}
}
