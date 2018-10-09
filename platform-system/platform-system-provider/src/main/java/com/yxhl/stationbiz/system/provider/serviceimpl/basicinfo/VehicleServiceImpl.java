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
import com.yxhl.stationbiz.system.provider.dao.basicinfo.VehicleDao;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;

/**
 * 车辆管理
 * @author xjh
 *
 */
@Service(value = "vehicleService")
public class VehicleServiceImpl extends CrudService<VehicleDao, Vehicle> implements VehicleService {

	@Autowired
	private VehicleDao VehicleDao;

	@Override
	public Page<Vehicle> selPageList(Page<Vehicle> page, Vehicle vehicle) {
		List<Vehicle> list= VehicleDao.selPageList(page,vehicle);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(Vehicle vehicle) {
		Wrapper<Vehicle> wrapper = new EntityWrapper<Vehicle>();
		Integer result = 1;
		if(vehicle.getCarNo()!=null) {
			wrapper.eq("car_no", vehicle.getCarNo());
			List<Vehicle> selectList = VehicleDao.selectList(wrapper);
			if(selectList!=null && selectList.size()>0) {
				throw new YxBizException("已存在该车牌号码!");
			}else {
				result = VehicleDao.insert(vehicle);
			}
		}
		return result > 0 ? true : false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatev(Vehicle vehicle) {
		Wrapper<Vehicle> wrapper = new EntityWrapper<Vehicle>();
		Integer result = 1;
		if(vehicle.getCarNo()!=null) {
			wrapper.eq("car_no", vehicle.getCarNo());
			List<Vehicle> selectList = VehicleDao.selectList(wrapper);
			if(selectList!=null && selectList.size()>0) {
				for(Vehicle v : selectList) {
					if(!(v.getId().equals(vehicle.getId()))) {
						throw new YxBizException("已存在该车牌号码!");
					}
				}
			}
			result= VehicleDao.updateAllColumnById(vehicle);
		}
		return result > 0 ? true : false;
	}

	@Override
	public List<Vehicle> exportData(Vehicle vehicle) {
		List<Vehicle> exportData = VehicleDao.exportData(vehicle);
		return exportData;
	}

	/**
	 * 根据车牌号查车辆信息
	 */
	@Override
	public List<Vehicle> getVehicle(Vehicle vehicle) {
		List<Vehicle> vehicle2 = VehicleDao.getVehicle(vehicle);
		return vehicle2;
	}
	

}