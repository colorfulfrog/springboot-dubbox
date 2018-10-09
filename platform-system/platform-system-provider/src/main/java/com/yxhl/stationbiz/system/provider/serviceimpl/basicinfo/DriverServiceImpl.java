package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.DriverDao;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverService;

/**
 * 司机管理
 * @author xjh
 *
 */
@Service(value = "driverService")
public class DriverServiceImpl extends CrudService<DriverDao, Driver> implements DriverService {

	@Autowired
	private DriverDao driverDao;

	@Override
	public Page<Driver> selPageList(Page<Driver> page, Driver driver) {
		List<Driver> list= driverDao.selPageList(page,driver);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updated(Driver driver) {
		Integer result =1;
		Wrapper<Driver> wrapper = new EntityWrapper<Driver>();
		wrapper.like("identity_card", driver.getIdentityCard());
		List<Driver> list = driverDao.selectList(wrapper);
		if(list !=null && list.size()>0) {
			for(Driver dv : list) {
				if(!(dv.getId().equals(driver.getId())) && dv.getIdentityCard().equals(driver.getIdentityCard())) {
					throw new YxBizException("该身份证已绑定!");
				}
			}
		}else {
			result = driverDao.updateById(driver);
		}
		driverDao.updateById(driver);
		return result > 0 ? true : false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(Driver driver) {
		Integer result =1;
		Wrapper<Driver> wrapper = new EntityWrapper<Driver>();
		wrapper.like("identity_card", driver.getIdentityCard());
		List<Driver> list = driverDao.selectList(wrapper);
		if(list !=null && list.size()>0) {
			throw new YxBizException("该身份证已绑定!");
		}else {
			result = driverDao.insert(driver);
		}
		driverDao.updateById(driver);
		return result > 0 ? true : false;
	}
	
	@Override
	public List<Driver> exportData(Driver driver) {
		List<Driver> exportData = driverDao.exportData(driver);
		return exportData;
	}

}