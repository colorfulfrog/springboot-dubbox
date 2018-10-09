package com.yxhl.stationbiz.system.provider.serviceimpl.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.security.VehicleInstationDao;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleInstation;
import com.yxhl.stationbiz.system.domain.service.security.VehicleInstationService;
/**
 * @ClassName: VehicleInstationServiceImpl
 * @Description: 车辆进站表 serviceImpl
 * @author ypf
 * @date 2018-8-13 16:52:37
 */
@Transactional(readOnly = true)
@Service("vehicleInstationService")
public class VehicleInstationServiceImpl extends CrudService<VehicleInstationDao, VehicleInstation> implements VehicleInstationService {
	@Autowired
	private VehicleInstationDao vehicleInstationDao;

	@Override
	public Page<VehicleInstation> selPageList(Page<VehicleInstation> page, VehicleInstation vehicleInstation) {
		List<VehicleInstation> list = vehicleInstationDao.selPageList(page, vehicleInstation);
		page.setRecords(list);
		return page;
	}
}
