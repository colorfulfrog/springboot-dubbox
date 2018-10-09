package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusStowageDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBusStowage;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusStowageService;
/**
 * @ClassName: ScheduleBusStowageServiceImpl
 * @Description: 班次配载表 serviceImpl
 * @author lw
 * @date 2018-9-15 9:49:26
 */
@Transactional(readOnly = true)
@Service("scheduleBusStowageService")
public class ScheduleBusStowageServiceImpl extends CrudService<ScheduleBusStowageDao, ScheduleBusStowage> implements ScheduleBusStowageService {
	@Autowired
	private ScheduleBusStowageDao scheduleBusStowageDao;

	@Override
	public Page<ScheduleBusStowage> selPageList(Page<ScheduleBusStowage> page, ScheduleBusStowage scheduleBusStowage) {
		List<ScheduleBusStowage> list = scheduleBusStowageDao.selPageList(page, scheduleBusStowage);
		page.setRecords(list);
		return page;
	}
}
