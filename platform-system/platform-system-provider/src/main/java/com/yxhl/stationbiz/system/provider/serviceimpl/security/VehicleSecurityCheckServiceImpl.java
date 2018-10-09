package com.yxhl.stationbiz.system.provider.serviceimpl.security;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleSecurityCheck;
import com.yxhl.stationbiz.system.domain.service.security.VehicleSecurityCheckService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.DriverDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.VehicleDao;
import com.yxhl.stationbiz.system.provider.dao.security.VehicleSecurityCheckDao;

import io.swagger.annotations.ApiModelProperty;
/**
 * @ClassName: VehicleSecurityCheckServiceImpl
 * @Description: 车辆安检表 serviceImpl
 * @author xjh
 * @date 2018-8-13 17:04:26
 */
@Transactional(readOnly = true)
@Service("vehicleSecurityCheckService")
public class VehicleSecurityCheckServiceImpl extends CrudService<VehicleSecurityCheckDao, VehicleSecurityCheck> implements VehicleSecurityCheckService {
	@Autowired
	private VehicleSecurityCheckDao vehicleSecurityCheckDao;
	
	@Autowired
	private DriverDao driverDao;
	
	@Autowired
	private VehicleDao VehicleDao;

	@Override
	public Page<VehicleSecurityCheck> selPageList(Page<VehicleSecurityCheck> page, VehicleSecurityCheck vehicleSecurityCheck) {
		List<VehicleSecurityCheck> list = vehicleSecurityCheckDao.selPageList(page, vehicleSecurityCheck);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(VehicleSecurityCheck vsc) {
		/*Date date = new Date();
		Driver dr = new Driver();
		dr.setDriverName(vsc.getDriver());
		Driver driver = driverDao.selectOne(dr);
		if(driver !=null) {
			//驾驶证有效截止日期 
			Date licenseEndDate = driver.getLicenseEndDate();
			if(licenseEndDate !=null) {
				if(date.before(licenseEndDate)) {
					new YxBizException("驾驶证已过期!");
				}
			}
			//从业资格证截止日期
			Date qualificationCertEndDate = driver.getQualificationCertEndDate();
			if(qualificationCertEndDate !=null) {
				if(date.before(qualificationCertEndDate)) {
					new YxBizException("从业资格证已过期!");
				}
			}
		}else {
			new YxBizException("该驾驶员不存在!");
		}
		Vehicle vc = new Vehicle();
		vc.setCarNo(vsc.getCarNo());
		Vehicle vehicle = VehicleDao.selectOne(vc);
		if(vehicle !=null) {
			//行驶证有效截止日期 
			Date driveLicEndDate = vehicle.getDriveLicEndDate();
			if(driveLicEndDate !=null) {
				if(date.before(driveLicEndDate)) {
					new YxBizException("行驶证已过期!");
				}
			}
		}*/
		
		
		Integer result = vehicleSecurityCheckDao.insert(vsc);
		return result > 0 ? true : false;
	}

	@Override
	public List<VehicleSecurityCheck> exportData(VehicleSecurityCheck vehicleSecurityCheck) {
		List<VehicleSecurityCheck> exportData = vehicleSecurityCheckDao.exportData(vehicleSecurityCheck);
		return exportData;
	}
}
