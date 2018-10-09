package com.yxhl.stationbiz.system.provider.serviceimpl.check;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.check.VehicleOutboundCheck;
import com.yxhl.stationbiz.system.domain.service.check.VehicleOutboundCheckService;
import com.yxhl.stationbiz.system.provider.dao.check.VehicleOutboundCheckDao;
/**
 * @ClassName: VehicleOutboundCheckServiceImpl
 * @Description: 车辆出站稽查表 serviceImpl
 * @author xjh
 * @date 2018-9-12 10:22:24
 */
@Transactional(readOnly = true)
@Service("vehicleOutboundCheckService")
public class VehicleOutboundCheckServiceImpl extends CrudService<VehicleOutboundCheckDao, VehicleOutboundCheck> implements VehicleOutboundCheckService {
	@Autowired
	private VehicleOutboundCheckDao vehicleOutboundCheckDao;

	@Override
	public Page<VehicleOutboundCheck> selPageList(Page<VehicleOutboundCheck> page, VehicleOutboundCheck vc) {
		List<VehicleOutboundCheck> list = vehicleOutboundCheckDao.selPageList(page, vc);
		List<VehicleOutboundCheck> busList = vehicleOutboundCheckDao.getBus(page, vc);
		if(busList.size()>0 && busList!=null && list.size()>0 && list!=null) {
			for (VehicleOutboundCheck vock : busList) {
				if(list.size()>0 && list!=null) {
					for (VehicleOutboundCheck vo : list) {
						//班次信息和出站信息合并
						if(vock.getScheduleBusId().equals(vo.getScheduleBusId())) {
							vock.setId(vo.getId());
							vock.setUpdater(vo.getUpdater());
							vock.setCreator(vo.getCreator());
							vock.setOutboundTime(vo.getOutboundTime());
							vock.setDriverLicFlag(vo.getDriverLicFlag());
							vock.setDrivingLicFlag(vo.getDrivingLicFlag());
							vock.setAllCertCompleteFlag(vo.getAllCertCompleteFlag());
							vock.setLineMarkFlag(vo.getLineMarkFlag());
							vock.setOperationCertFlag(vo.getOperationCertFlag());
							vock.setOverloadFlag(vo.getOverloadFlag());
							vock.setPaymentFee(vo.getPaymentFee());
							vock.setCheckType(vo.getCheckType());
							vock.setPaymentPeople(vo.getPaymentPeople());
							vock.setActualPassengerNum(vo.getActualPassengerNum());
							vock.setSeatBeltFlag(vo.getSeatBeltFlag());
							vock.setQualificationCertFlag(vo.getQualificationCertFlag());
							vock.setSecurityCheckFlag(vo.getSecurityCheckFlag());
							list.remove(vo);
							break;
						}
					}
				}
			}
		}
		page.setRecords(busList);
		return page;
	}

	@Override
	public List<VehicleOutboundCheck> selByScheduleIds(List<String> scheduleIds) {
		return vehicleOutboundCheckDao.selByScheduleIds(scheduleIds);
	}
	
	/**
	 * 班次信息
	 * @param page
	 * @param vc
	 * @return
	 */
//	@Override
//	public Page<BusVehicleOutResp> getBusPage(Page<BusVehicleOutResp> page,VehicleOutboundCheck vc) {
//		List<BusVehicleOutResp> list = vehicleOutboundCheckDao.getBus(page, vc);
//		page.setRecords(list);
//		return page;
//	}

	/**
	 * 根据id查车辆出站稽查表
	 */
	@Override
	public VehicleOutboundCheck getOne(String vochekId) {
		VehicleOutboundCheck one = vehicleOutboundCheckDao.getOne(vochekId);
		return one;
	}

	/**
	 * 添加
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(VehicleOutboundCheck vc) {
		VehicleOutboundCheck vock = new VehicleOutboundCheck();
		vock.setScheduleBusId(vc.getScheduleBusId());
		vock.setOrgId(vc.getOrgId());
		vock.setCompId(vc.getCompId());
		vock.setCheckType(vc.getCheckType());
		VehicleOutboundCheck selectOne = vehicleOutboundCheckDao.selectOne(vock);
		if(selectOne!=null) {
			if(vc.getCheckType()==1) {
				throw new YxBizException("该班次已有出站信息!");
			}else {
				throw new YxBizException("该班次已有稽查信息!");
			}
			
		}
		vc.setId(IdWorker.get32UUID());
		Integer result = vehicleOutboundCheckDao.insert(vc);
		return result>0 ? true:false;
	}

	/**
	 * 根据班次id查出站信息
	 */
	@Override
	public VehicleOutboundCheck getByBusId(String busId) {
		VehicleOutboundCheck check = vehicleOutboundCheckDao.getByBusId(busId);
		return check;
	}
}
