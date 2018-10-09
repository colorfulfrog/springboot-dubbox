package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.schedule.HolidayDao;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayService;
/**
 * @ClassName: HolidayServiceImpl
 * @Description: 节日设置表 serviceImpl
 * @author ypf
 * @date 2018-8-13 17:41:22
 */
@Transactional(readOnly = true)
@Service("holidayService")
public class HolidayServiceImpl extends CrudService<HolidayDao, Holiday> implements HolidayService {
	@Autowired
	private HolidayDao holidayDao;

	@Override
	public Page<Holiday> selPageList(Page<Holiday> page, Holiday holiday) {
		List<Holiday> list = holidayDao.selPageList(page, holiday);
		page.setRecords(list);
		return page;
	}
}
