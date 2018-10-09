package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoopBus;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleLoopService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleBusTplDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleLoopBusDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleLoopDao;
/**
 * @ClassName: ScheduleLoopServiceImpl
 * @Description: 班次调度循环配置 serviceImpl
 * @author lw
 * @date 2018-7-11 9:27:23
 */
@Transactional(readOnly = true)
@Service("scheduleLoopService")
public class ScheduleLoopServiceImpl extends CrudService<ScheduleLoopDao, ScheduleLoop> implements ScheduleLoopService {
	@Autowired
	private ScheduleLoopDao scheduleLoopDao;
	
	@Autowired
	private ScheduleLoopBusDao scheduleLoopBusDao;
	@Autowired
	private ScheduleBusTplDao scheduleBusTplDao;

	@Override
	public Page<ScheduleLoop> selPageList(Page<ScheduleLoop> page, ScheduleLoop scheduleLoop) {
		List<ScheduleLoop> list = scheduleLoopDao.selPageList(page, scheduleLoop);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean addScheduleLoop(ScheduleLoop scheduleLoop) {
		String loopId = IdWorker.get32UUID();
		scheduleLoop.setId(loopId);
		scheduleLoopDao.insert(scheduleLoop);
		
		List<ScheduleBusTpl> scheduleBusTpls = scheduleLoop.getScheduleBusTpls();
		if(scheduleBusTpls !=null) {
			for (ScheduleBusTpl scheduleBusTpl : scheduleBusTpls) {
				ScheduleLoopBus loopBus = new ScheduleLoopBus();
				loopBus.setId(IdWorker.get32UUID());
				loopBus.setOrgId(scheduleLoop.getOrgId());
				loopBus.setCompId(scheduleLoop.getCompId());
				loopBus.setScheduleLoopId(loopId);
				loopBus.setScheduleBusId(scheduleBusTpl.getId());
				loopBus.setCreateBy(scheduleLoop.getCreateBy());
				loopBus.setUpdateBy(scheduleLoop.getUpdateBy());
				scheduleLoopBusDao.insert(loopBus);
			}
		}
		
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean updateScheduleLoop(ScheduleLoop scheduleLoop) {
		scheduleLoopDao.updateById(scheduleLoop); //修改循环配置
		
		//查询该循环配置绑定的班次信息
		Wrapper<ScheduleLoopBus> wrapper = new EntityWrapper<ScheduleLoopBus>();
		wrapper.where("schedule_loop_id={0}", scheduleLoop.getId());
		scheduleLoopBusDao.delete(wrapper);
		
		List<ScheduleBusTpl> scheduleBusTpls = scheduleLoop.getScheduleBusTpls();
		if(scheduleBusTpls !=null) {
			for (ScheduleBusTpl scheduleBusTpl : scheduleBusTpls) {
				ScheduleLoopBus loopBus = new ScheduleLoopBus();
				loopBus.setId(IdWorker.get32UUID());
				loopBus.setOrgId(scheduleLoop.getOrgId());
				loopBus.setCompId(scheduleLoop.getCompId());
				loopBus.setScheduleLoopId(scheduleLoop.getId());
				loopBus.setScheduleBusId(scheduleBusTpl.getId());
				loopBus.setCreateBy(scheduleLoop.getCreateBy());
				loopBus.setUpdateBy(scheduleLoop.getUpdateBy());
				scheduleLoopBusDao.insert(loopBus);
			}
		}
		
		return true;
	}

	@Override
	public ScheduleLoop getInfoById(String scheduleLoopId) {
		ScheduleLoop scheduleLoop = scheduleLoopDao.getScheduleLoopById(scheduleLoopId);
		List<ScheduleBusTpl> tplList = scheduleBusTplDao.selTplByLoopId(scheduleLoopId);
		scheduleLoop.setScheduleBusTpls(tplList);
		return scheduleLoop;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	@Override
	public boolean delScheduleLoops(List<String> ids) {
		if(!ids.isEmpty()) {
			deleteBatchIds(ids);
			Wrapper<ScheduleLoopBus> wrapper = new EntityWrapper<ScheduleLoopBus>();
			wrapper.in("schedule_loop_id", ids);
			scheduleLoopBusDao.delete(wrapper);
		}
		return true;
	}

	@Override
	public List<ScheduleLoop> exportData(ScheduleLoop scheduleLoop) {
		return scheduleLoopDao.exportData(scheduleLoop);
	}
}
