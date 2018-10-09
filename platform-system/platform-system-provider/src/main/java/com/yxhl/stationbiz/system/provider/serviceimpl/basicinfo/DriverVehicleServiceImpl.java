package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverVehicleService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.DriverDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.DriverVehicleDao;
/**
 * @ClassName: DriverVehicleServiceImpl
 * @Description: 驾驶员车辆绑定表 serviceImpl
 * @author xjh
 * @date 2018-7-9 17:39:14
 */
@Transactional(readOnly = true)
@Service("driverVehicleService")
public class DriverVehicleServiceImpl extends CrudService<DriverVehicleDao, DriverVehicle> implements DriverVehicleService {
	@Autowired
	private DriverVehicleDao driverVehicleDao;
	
	@Autowired
	private DriverDao driverDao;

	@Override
	public Page<DriverVehicle> selPageList(Page<DriverVehicle> page, DriverVehicle driverVehicle) {
		List<DriverVehicle> list = driverVehicleDao.selPageList(page, driverVehicle);
		page.setRecords(list);
		return page;
	}

	@Override
	public DriverVehicle getOne(String id) {
		DriverVehicle one = driverVehicleDao.getOne(id);
		return one;
	}

	/**
	 * 添加
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(DriverVehicle dv) {
		Integer result = 1;
		Wrapper<DriverVehicle> wrapper = new EntityWrapper<DriverVehicle>();
		wrapper.eq("driver_id", dv.getDriverId());
		List<DriverVehicle> dbList = driverVehicleDao.selectList(wrapper);
		List<String> vcList = new ArrayList<>();
		for(DriverVehicle dvId : dbList) {
			vcList.add(dvId.getVehicleId());
		}
		List<String> listId = dv.getListId();  //车辆id
		for(String did : listId) {
			if(vcList.contains(did)) {
				throw new YxBizException("司机已绑定该车辆!");
			}else {
				dv.setVehicleId(did);
				dv.setId(IdWorker.get32UUID());
				Driver driver = new Driver();
				driver.setId(dv.getDriverId());
				driver.setBindTime(new Date());	//初次绑定车辆时间
				driver.setUpdateBindTime(new Date());	//修改绑定车辆时间
				driverDao.updateById(driver);
				result = driverVehicleDao.insert(dv);
			}
		}
		
		return result > 0 ? true : false;
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateDv(DriverVehicle dv) {
		boolean add = true;
		List<String> sjId = new ArrayList<>();
		sjId.add(dv.getDriverId());
		boolean deleteBydrId = deleteBydrId(sjId);
		if(deleteBydrId) {
			Wrapper<DriverVehicle> wrapper = new EntityWrapper<DriverVehicle>();
			wrapper.eq("driver_id", dv.getDriverId());
			List<DriverVehicle> dbList = driverVehicleDao.selectList(wrapper);
			List<String> vcList = new ArrayList<>();
			for(DriverVehicle dvId : dbList) {
				vcList.add(dvId.getVehicleId());
			}
			List<String> listId = dv.getListId();  //车辆id
			for(String did : listId) {
				if(vcList.contains(did)) {
					throw new YxBizException("司机已绑定该车辆!");
				}else {
					dv.setVehicleId(did);
					dv.setId(IdWorker.get32UUID());
					dv.setCreateBy(dv.getUpdateBy());
					Driver driver = new Driver();
					driver.setId(dv.getDriverId());
					driver.setUpdateBindTime(new Date());	//修改绑定车辆时间
					driverDao.updateById(driver);
					driverVehicleDao.insert(dv);
				}
			}
		}else {
			add = false;
		}
		return add;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteBydrId(List<String> driverId) {
		boolean deleteBydrId = true;
		for(String sjId : driverId) {
			deleteBydrId = driverVehicleDao.deleteBydrId(sjId);
		}
		return deleteBydrId;
	}

	@Override
	public List<DriverVehicle> exportData(DriverVehicle driverVehicle) {
		List<DriverVehicle> exportData = driverVehicleDao.exportData(driverVehicle);
		return exportData;
	}
}
