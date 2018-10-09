package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleLoopBusDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoopBus;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleLoopBusService;
/**
 * @ClassName: ScheduleLoopBusServiceImpl
 * @Description: 班次循环配置中间表 serviceImpl
 * @author lw
 * @date 2018-7-11 9:31:17
 */
@Transactional(readOnly = true)
@Service("scheduleLoopBusService")
public class ScheduleLoopBusServiceImpl extends CrudService<ScheduleLoopBusDao, ScheduleLoopBus> implements ScheduleLoopBusService {
	@Autowired
	private ScheduleLoopBusDao scheduleLoopBusDao;

	@Override
	public Page<ScheduleLoopBus> selPageList(Page<ScheduleLoopBus> page, ScheduleLoopBus scheduleLoopBus) {
		List<ScheduleLoopBus> list = scheduleLoopBusDao.selPageList(page, scheduleLoopBus);
		page.setRecords(list);
		return page;
	}
}
