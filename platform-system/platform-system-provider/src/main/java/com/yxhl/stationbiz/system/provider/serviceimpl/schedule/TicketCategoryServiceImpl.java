package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.schedule.TicketCategoryDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCategoryService;
/**
 * @ClassName: TicketCategoryServiceImpl
 * @Description: 票种设置表 serviceImpl
 * @author xjh
 * @date 2018-8-16 10:25:40
 */
@Transactional(readOnly = true)
@Service("ticketCategoryService")
public class TicketCategoryServiceImpl extends CrudService<TicketCategoryDao, TicketCategory> implements TicketCategoryService {
	@Autowired
	private TicketCategoryDao ticketCategoryDao;

	@Override
	public Page<TicketCategory> selPageList(Page<TicketCategory> page, TicketCategory ticketCategory) {
		List<TicketCategory> list = ticketCategoryDao.selPageList(page, ticketCategory);
		page.setRecords(list);
		return page;
	}
}
