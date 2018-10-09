package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.BusEntranceDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.BusEntrance;
import com.yxhl.stationbiz.system.domain.service.basicinfo.BusEntranceService;
/**
 * @ClassName: BusEntranceServiceImpl
 * @Description: 乘车库表 serviceImpl
 * @author lw
 * @date 2018-7-10 9:48:43
 */
@Transactional(readOnly = true)
@Service("busEntranceService")
public class BusEntranceServiceImpl extends CrudService<BusEntranceDao, BusEntrance> implements BusEntranceService {
	@Autowired
	private BusEntranceDao busEntranceDao;

	@Override
	public Page<BusEntrance> selPageList(Page<BusEntrance> page, BusEntrance busEntrance) {
		List<BusEntrance> list = busEntranceDao.selPageList(page, busEntrance);
		page.setRecords(list);
		return page;
	}
}
