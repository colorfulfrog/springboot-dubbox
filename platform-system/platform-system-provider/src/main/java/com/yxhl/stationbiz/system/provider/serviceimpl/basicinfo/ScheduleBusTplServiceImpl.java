package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusSite;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.enums.BusTplEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusReportStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusSeatStatusEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusSiteService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusTplService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineSiteDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleBusTplDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusDao;
/**
 * @ClassName: ScheduleBusTplServiceImpl
 * @Description: 班次模板 serviceImpl
 * @author lw
 * @date 2018-7-11 9:58:54
 */
@Transactional(readOnly = true)
@Service("scheduleBusTplService")
public class ScheduleBusTplServiceImpl extends CrudService<ScheduleBusTplDao, ScheduleBusTpl> implements ScheduleBusTplService {
	@Autowired
	private ScheduleBusTplDao scheduleBusTplDao;
	
	@Autowired
	private ScheduleBusDao scheduleBusDao;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private ScheduleBusSiteService scheduleBusSiteService;
	
	@Autowired
	private LineDao lineDao;
	
	@Autowired
	private LineSiteDao lineSiteDao;
	@Autowired
	private HolidayPriceService holidayPriceService;
	@Autowired
	private ExecPriceService execPriceService;
	@Autowired
    private ScheduleBusSeatsService scheduleBusSeatsService;
	@Autowired
	private StationService stationService;

	@Override
	public Page<ScheduleBusTpl> selPageList(Page<ScheduleBusTpl> page, ScheduleBusTpl scheduleBusTpl) {
		List<ScheduleBusTpl> list = scheduleBusTplDao.selPageList(page, scheduleBusTpl);
		pushDictionaryValue(list);
		page.setRecords(list);
		return page;
	}
	

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addScheduleBusTpl(ScheduleBusTpl scheduleBusTpl) {
		pushFlagValue(scheduleBusTpl);
		scheduleBusTpl.setStatus(0);//0、正常  1长停  
		boolean flag=false;
		Integer isAdded =scheduleBusTplDao.insert(scheduleBusTpl);
		Line line= lineDao.selById(scheduleBusTpl.getLineId());
		scheduleBusTpl.setLineName(line.getLineName()); 
		if(isAdded>0) {
			flag=true;
		}
		if(null!=scheduleBusTpl.getImmediateGenerFlag()&&scheduleBusTpl.getImmediateGenerFlag()==1) {
			//是否立即生成班次
			
			ScheduleBus entity = new ScheduleBus();
		    entity.setBusCode(scheduleBusTpl.getBusCode());
		    entity.setScheduleTplId(scheduleBusTpl.getId());
		    entity.setRunDate(DateHelper.getTodayDate());
		    if(scheduleBusTpl.getRunFlowFlag()==1) {
		    	entity.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
		    }
	        ScheduleBus curBus = scheduleBusDao.selectOne(entity);
	        
	        if(Util.isNull(curBus)) {
	        	ScheduleBus bus = new ScheduleBus(scheduleBusTpl,new Date());
				bus.setCreateBy(scheduleBusTpl.getCreateBy());
				bus.setUpdateBy(scheduleBusTpl.getCreateBy());
				Station startStation = stationService.selectById(line.getStartStateId());
				Station endStation = stationService.selectById(line.getEndStateId());
				bus.setStartStation(startStation.getStationName());
				bus.setEndStation(endStation.getStationName());
				Integer isAddBus =scheduleBusDao.insert(bus);
				// 1判断班次日期是否满足节假日票价设置，满足则从节假日票价复制到执行票价表，否则从基础票价表复制
				String holidayID = holidayPriceService.isHolidayPriceSet(bus);
				try {
					if(StringUtils.isNotBlank(holidayID)) {
						execPriceService.copyHolidayPrice(bus,holidayID);
					}else {
						execPriceService.copyBasicPrice(bus);
					}
				}catch (Exception e) {
					logger.error("===拷贝票价出错===",e);
				}
				
				// 2生成班次座位
				Integer seats = bus.getSeats(); //该班次座位数
				ScheduleBusSeats seat = null;
				List<ScheduleBusSeats> seatList = Lists.newArrayList();
				for(int i = 1; i <= seats.intValue(); i++) {
					seat = new ScheduleBusSeats();
					seat.setScheduleBusId(bus.getId());
					seat.setSeatNum(i);
					seat.setSeatStatus(ScheduleBusSeatStatusEnum.OPTIONAL.getType()); //默认可选
					seatList.add(seat);
				}
				scheduleBusSeatsService.insertBatch(seatList);
				 if(isAddBus<=0) {
					 flag=false;
				 }
	        }
		}
		Wrapper<LineSite> wrapper=new EntityWrapper<LineSite>();
		wrapper.eq("line_id", scheduleBusTpl.getLineId());
		wrapper.orderBy("sort");
		List<LineSite> lineSites=lineSiteDao.selectList(wrapper);
		LineSite firstLineSite=lineSites.get(0);//取第一条线路停靠点，做为班次始发站
		if(Util.isNotNull(lineSites)) {
			List<ScheduleBusSite> busSiteList=Lists.newArrayList();
			for(LineSite lineite:lineSites) {
				ScheduleBusSite busSite=new ScheduleBusSite(lineite);
				if(StringUtils.equals(firstLineSite.getId(), lineite.getId()) ) {
					busSite.setFirstSiteFlag(1);//1 设置班次的停靠点为始发站
				}
				busSite.setScheduleBusTplId(scheduleBusTpl.getId());
				busSite.setCreateBy(scheduleBusTpl.getCreateBy());  
				busSite.setUpdateBy(scheduleBusTpl.getUpdateBy()); 
				busSite.setAllowTicketFlag(1); 
				busSite.setSort(lineite.getSort()); 
				busSiteList.add(busSite);
			}
			if(CollectionUtils.isNotEmpty(busSiteList)) {
				scheduleBusSiteService.insertBatch(busSiteList);
			}
		}
		return flag;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delScheduleBusTpl(List<String> scheduleBusTplId) {
		Integer del= scheduleBusTplDao.deleteBatchIds(scheduleBusTplId);
		Wrapper<ScheduleBusSite> wrapper = new EntityWrapper<ScheduleBusSite>();
		//对应删除相关的班次停靠点
		for(String id:scheduleBusTplId) {
			wrapper.eq("schedule_bus_tpl_id", id);
			scheduleBusSiteService.delete(wrapper);
		}
		return del>0?true:false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateScheduleBusTpl(ScheduleBusTpl scheduleBusTpl) {
		pushFlagValue(scheduleBusTpl);
		boolean flag=false;
		Integer isAdded =scheduleBusTplDao.updateById(scheduleBusTpl);
		Line line= lineDao.selById(scheduleBusTpl.getLineId());
		scheduleBusTpl.setLineName(line.getLineName()); 
		if(isAdded>0) {
			flag=true;
		}
		if(null!=scheduleBusTpl.getImmediateGenerFlag()&&scheduleBusTpl.getImmediateGenerFlag()==1) {
			//是否立即生成班次
			ScheduleBus entity = new ScheduleBus();
		    entity.setBusCode(scheduleBusTpl.getBusCode());
		    entity.setScheduleTplId(scheduleBusTpl.getId());
		    entity.setRunDate(DateHelper.getTodayDate());
		    if(scheduleBusTpl.getRunFlowFlag()==1) {
		    	entity.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
		    }
	        ScheduleBus curBus = scheduleBusDao.selectOne(entity);
	        if(Util.isNull(curBus)) {
	        	ScheduleBus bus = new ScheduleBus(scheduleBusTpl,new Date());
				bus.setCreateBy(scheduleBusTpl.getCreateBy());
				bus.setUpdateBy(scheduleBusTpl.getCreateBy());
				Integer isAddBus =scheduleBusDao.insert(bus);
				 if(isAddBus<=0) {
					 flag=false;
				 }
	        }
		}
		return flag;
	}
	
	
	/**
	 * 空值判断 填充
	 * @param scheduleBusTpl
	 */
	private void pushFlagValue(ScheduleBusTpl scheduleBusTpl) {
		if(Util.isNull(scheduleBusTpl.getRunFlowFlag())) {
			scheduleBusTpl.setRunFlowFlag(0);
		}
		if(Util.isNull(scheduleBusTpl.getOvertimeBusFlag())) {
			scheduleBusTpl.setOvertimeBusFlag(0);
		}
		if(Util.isNull(scheduleBusTpl.getPassBusFlag())) {
			scheduleBusTpl.setPassBusFlag(0);
		}
		if(Util.isNull(scheduleBusTpl.getDoubleBusFlag())) {
			scheduleBusTpl.setDoubleBusFlag(0);
		}
		if(null==scheduleBusTpl.getSpecializeFlag()) {
			scheduleBusTpl.setSpecializeFlag(0);
		}
	}
	
	
	@Override
	public ScheduleBusTpl selOne(String id) {
		ScheduleBusTpl scheduleBusTpl = scheduleBusTplDao.selOne(id);
		
		List<ScheduleBusTpl> list= new ArrayList<ScheduleBusTpl>();
		list.add(scheduleBusTpl);
		pushDictionaryValue(list);
		return scheduleBusTpl;
	}
	
	/**
	 * 填序字典表值
	 * @param list
	 */
	private void pushDictionaryValue(List<ScheduleBusTpl> list) {
		List<Dictionary> oprCategorylist= dictionaryService.getRediesByKey(BusTplEnum.OPR_CATEGORYVARCHAR.getbusType());
		List<Dictionary> runAreallist= dictionaryService.getRediesByKey(BusTplEnum.RUN_AREAVARCHAR.getbusType());
		List<Dictionary> oprModellist= dictionaryService.getRediesByKey(BusTplEnum.LINE_DIRECTIONVARCHAR.getbusType());
		List<Dictionary> busTypellist= dictionaryService.getRediesByKey(BusTplEnum.BUS_TYPEVARCHAR.getbusType());
		for(ScheduleBusTpl le:list) {
			for(Dictionary dr:oprCategorylist) {
					 if(null!=le&&null!=le.getOprCategory()&&
							 dr.getValue().equals(le.getOprCategory().toString())) {
					 le.setOprCategoryName(dr.getKeyName());
				 }
			}
			for(Dictionary dr:runAreallist) {
				 if(null!=le&&null!=le.getRunArea()&&
						 dr.getValue().equals(le.getRunArea().toString())) {
				 le.setRunAreaName(dr.getKeyName());
			 }
			}
			for(Dictionary dr:oprModellist) {
				 if(null!=le&&null!=le.getOprMode()&&
						 dr.getValue().equals(le.getOprMode().toString())) {
				 le.setOprModeName(dr.getKeyName());
			 }
			}
			for(Dictionary dr:busTypellist) {
				 if(null!=le&&null!=le.getVehicleType()&&
						 dr.getValue().equals(le.getVehicleType().toString())) {
				 le.setVehicleTypeName(dr.getKeyName());
			 }
			}
		}
	}

	@Override
	public List<ScheduleBusTpl> selScheduleTplByLineId(String lineId) {
		return scheduleBusTplDao.selScheduleTplByLineId(lineId);
	}
}
