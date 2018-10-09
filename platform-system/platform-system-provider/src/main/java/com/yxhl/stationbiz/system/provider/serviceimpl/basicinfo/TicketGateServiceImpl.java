package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.TicketGateDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.TicketGate;
import com.yxhl.stationbiz.system.domain.service.basicinfo.TicketGateService;
/**
 * @ClassName: TicketGateServiceImpl
 * @Description: 检票口表 serviceImpl
 * @author lw
 * @date 2018-7-10 9:28:58
 */
@Transactional(readOnly = true)
@Service("ticketGateService")
public class TicketGateServiceImpl extends CrudService<TicketGateDao, TicketGate> implements TicketGateService {
	@Autowired
	private TicketGateDao ticketGateDao;

	@Override
	public Page<TicketGate> selPageList(Page<TicketGate> page, TicketGate ticketGate) {
		List<TicketGate> list = ticketGateDao.selPageList(page, ticketGate);
		page.setRecords(list);
		return page;
	}
}
