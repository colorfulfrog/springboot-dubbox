package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.schedule.TicketCateValueDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCateValueService;
/**
 * @ClassName: TicketCateValueServiceImpl
 * @Description: 票种取值表 serviceImpl
 * @author ypf
 * @date 2018-8-15 14:12:46
 */
@Transactional(readOnly = true)
@Service("ticketCateValueService")
public class TicketCateValueServiceImpl extends CrudService<TicketCateValueDao, TicketCateValue> implements TicketCateValueService {
	@Autowired
	private TicketCateValueDao ticketCateValueDao;

	@Override
	public Page<TicketCateValue> selPageList(Page<TicketCateValue> page, TicketCateValue ticketCateValue) {
		List<TicketCateValue> list = ticketCateValueDao.selPageList(page, ticketCateValue);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<TicketCateValue> selPriceValue(TicketCateValue ticketCateValue) {
		return ticketCateValueDao.selPriceValue(ticketCateValue);
	}
}
