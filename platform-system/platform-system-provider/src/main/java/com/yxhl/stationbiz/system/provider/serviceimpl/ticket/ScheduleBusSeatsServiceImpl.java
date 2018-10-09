package com.yxhl.stationbiz.system.provider.serviceimpl.ticket;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.ticket.ScheduleBusSeatsDao;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;
/**
 * @ClassName: ScheduleBusSeatsServiceImpl
 * @Description: 班次座位表 serviceImpl
 * @author lw
 * @date 2018-9-13 9:57:59
 */
@Transactional(readOnly = true)
@Service("scheduleBusSeatsService")
public class ScheduleBusSeatsServiceImpl extends CrudService<ScheduleBusSeatsDao, ScheduleBusSeats> implements ScheduleBusSeatsService {
	@Autowired
	private ScheduleBusSeatsDao scheduleBusSeatsDao;

	@Override
	public Page<ScheduleBusSeats> selPageList(Page<ScheduleBusSeats> page, ScheduleBusSeats scheduleBusSeats) {
		List<ScheduleBusSeats> list = scheduleBusSeatsDao.selPageList(page, scheduleBusSeats);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateScheduleBusSeatStatus(List<String> ids) {
		return scheduleBusSeatsDao.updateScheduleBusSeatStatus(ids) > 0;
	}
}
