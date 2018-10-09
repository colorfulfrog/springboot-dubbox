package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.stationbiz.system.domain.entity.schedule.BasicPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.entity.schedule.HolidayPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.enums.TicketCateValueTypeEnum;
import com.yxhl.stationbiz.system.domain.service.schedule.BasicPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayService;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCateValueService;
import com.yxhl.stationbiz.system.provider.dao.schedule.ExecPriceDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusDao;

/**
 * @ClassName: ExecPriceServiceImpl
 * @Description: 执行票价表 serviceImpl
 * @author lw
 * @date 2018-8-21 10:23:17
 */
@Transactional(readOnly = true)
@Service("execPriceService")
public class ExecPriceServiceImpl extends CrudService<ExecPriceDao, ExecPrice> implements ExecPriceService {
	@Autowired
	private ExecPriceDao execPriceDao;

	@Autowired
	private ScheduleBusDao scheduleBusDao;

	@Autowired
	private BasicPriceService basicPriceService;
	@Autowired
	private HolidayPriceService holidayPriceService;
	@Autowired
	private TicketCateValueService ticketCateValueService;
	
	@Autowired
	private HolidayService holidayService;


	@Override
	public Page<ExecPrice> selPageList(Page<ExecPrice> page, ExecPrice execPrice) {
		List<ExecPrice> list = execPriceDao.selPageList(page, execPrice);
		page.setRecords(list);
		return page;
	}

	
	
	
	@Override
	public void addExecPriceByBasic(List<BasicPrice>  basicPriceList,List<TicketCateValue> ticketCateValues) throws IllegalAccessException, InvocationTargetException {
		String scheduleTplId=null;
		if(CollectionUtils.isNotEmpty(basicPriceList)) {
			BasicPrice basicPrice= basicPriceList.get(0);
			//班次模板编号
			scheduleTplId= basicPrice.getScheduleTplId();
			
			if(Util.isNotNull(scheduleTplId)) {
				List<TicketCateValue> execTicketCateValueList=Lists.newArrayList();
				List<ExecPrice> destExecPriceList=Lists.newArrayList();
				
				Wrapper<ScheduleBus> scheduleBusWrapper = new EntityWrapper<ScheduleBus>();
				scheduleBusWrapper.where("schedule_tpl_id={0} and  run_date >={1}", scheduleTplId,Util.getCurrentDate());
				//查询班次模板编号 当天及以后的所有班次
				List<ScheduleBus> scheduleBuslist=scheduleBusDao.selectList(scheduleBusWrapper);
				if(CollectionUtils.isNotEmpty(scheduleBuslist)) {
					for(ScheduleBus scheduleBus:scheduleBuslist) {
						String scheduleBusId=scheduleBus.getId();
						Wrapper<ExecPrice> execPriceWrapper = new EntityWrapper<ExecPrice>();
						execPriceWrapper.eq("schedule_bus_id", scheduleBusId);
						//查询班次编号是否已存在"执行票价"
						List<ExecPrice>  execPriceList=execPriceDao.selectList(execPriceWrapper);

						if(CollectionUtils.isEmpty(execPriceList)) {
							for(BasicPrice bprice:basicPriceList) {
								ExecPrice execPrice=new ExecPrice(bprice);
								execPrice.setId(IdWorker.get32UUID()); 
								execPrice.setScheduleBusId(scheduleBusId); 
								destExecPriceList.add(execPrice);
								for(TicketCateValue tv : ticketCateValues) {
									if(bprice.getId().equals(tv.getPriceId())) { 
										TicketCateValue execTicketCateValue=new TicketCateValue();
										BeanUtils.copyProperties(execTicketCateValue,tv);   
										execTicketCateValue.setId(IdWorker.get32UUID()); 
										execTicketCateValue.setPriceId(execPrice.getId()); 
										execTicketCateValue.setPriceTblType(TicketCateValueTypeEnum.EXEC_TYPE.getValue());
										execTicketCateValueList.add(execTicketCateValue);
									}
								}
							}
						}
					}
				}
				
				if(CollectionUtils.isNotEmpty(destExecPriceList)) {
					//批量添加执行票价
					this.insertBatch(destExecPriceList);
				}
				if(CollectionUtils.isNotEmpty(execTicketCateValueList)) {
					//批量添加执行票价值
					ticketCateValueService.insertBatch(execTicketCateValueList);
				}
				
			}
		}
	}
	
	
	
	@Override
	public void addExecPriceByHoliday(List<HolidayPrice>  holidayPriceList,List<TicketCateValue> ticketCateValues) throws IllegalAccessException, InvocationTargetException {
		String scheduleTplId=null;
		if(CollectionUtils.isNotEmpty(holidayPriceList)) {
			HolidayPrice holidayPrice= holidayPriceList.get(0);
			//班次模板编号
			scheduleTplId= holidayPrice.getScheduleTplId();
			List<TicketCateValue> execTicketCateValueList=Lists.newArrayList();
			
			List<HolidayPrice> filterHolidayPriceList=Lists.newArrayList();
			if(Util.isNotNull(scheduleTplId)) {
				List<String> holidayIds=Lists.newArrayList();
				for(HolidayPrice hdrice:holidayPriceList) {
					if(Util.isNotNull(hdrice.getHolidayId())) { 
						holidayIds.add(hdrice.getHolidayId());
					}
				}
				
				//查询节假日票价的所有节假日
				List<Holiday> holidayList= holidayService.selectBatchIds(holidayIds);
				
				Wrapper<ScheduleBus> scheduleBusWrapper = new EntityWrapper<ScheduleBus>();
				scheduleBusWrapper.where("schedule_tpl_id={0} and  run_date >={1}", scheduleTplId,Util.getCurrentDate());
				//查询班次模板编号 当天及以后的所有班次
				List<ScheduleBus> scheduleBuslist=scheduleBusDao.selectList(scheduleBusWrapper);
				if(CollectionUtils.isNotEmpty(scheduleBuslist)) {
					for(ScheduleBus scheduleBus:scheduleBuslist) {
						String scheduleBusId=scheduleBus.getId();
						Wrapper<ExecPrice> execPriceWrapper = new EntityWrapper<ExecPrice>();
						execPriceWrapper.eq("schedule_bus_id", scheduleBusId);
						
						//查询班次编号是否已存在"执行票价"
						List<ExecPrice>  execPriceList=execPriceDao.selectList(execPriceWrapper);
						
						/**
						 * 如果存在执行票价，先删除 处理节假日范围内的执行票价
						 */
						for(Holiday holiday:holidayList) {
							//判断班次发车日期   是否为该节假日日期范围内
							boolean isHoliday = DateHelper.containDate(scheduleBus.getRunDate(), holiday.getBeginDate(), holiday.getEndDate());
							if(isHoliday) {   //如果为节假日的，先删除，   非节假日范围的执行票价不删除
								if(CollectionUtils.isNotEmpty(execPriceList)) {
									List<String> ids=new ArrayList<String>();
									for(ExecPrice ecPrice:execPriceList) {
										ids.add(ecPrice.getId());
										Wrapper<TicketCateValue> ticketCateValueWrapper = new EntityWrapper<TicketCateValue>();
										ticketCateValueWrapper.eq("price_id", ecPrice.getId());
										ticketCateValueService.delete(ticketCateValueWrapper);
									}
									execPriceDao.deleteBatchIds(ids);
								}
								for(HolidayPrice hdrice:holidayPriceList) {
									if(StringUtils.equals(holiday.getId(), hdrice.getHolidayId()) ) {
										filterHolidayPriceList.add(hdrice);
									}
								}
							}
						}
						
						//filterHolidayPriceList 这里都为属于节假日范围的   执行票价复制
						for(HolidayPrice hdrice:filterHolidayPriceList) {
								ExecPrice execPrice=new ExecPrice(hdrice);
								execPrice.setId(IdWorker.get32UUID()); 
								execPrice.setScheduleBusId(scheduleBusId); 
								execPriceDao.insert(execPrice);
								for(TicketCateValue tv : ticketCateValues) {
									if(hdrice.getId().equals(tv.getPriceId())) { 
										TicketCateValue execTicketCateValue=new TicketCateValue();
										BeanUtils.copyProperties(execTicketCateValue,tv);   
										execTicketCateValue.setId(IdWorker.get32UUID()); 
										execTicketCateValue.setPriceId(execPrice.getId()); 
										execTicketCateValue.setPriceTblType(TicketCateValueTypeEnum.EXEC_TYPE.getValue());
										execTicketCateValueList.add(execTicketCateValue);
									}
								}
						}

					}
				}
				if(CollectionUtils.isNotEmpty(execTicketCateValueList)) {
					//批量添加执行票价
					ticketCateValueService.insertBatch(execTicketCateValueList);
				}
				
			}
		}
	}



	@Override
	public List<ExecPrice> selExecPrice(String scheduleBusId) {
		List<ExecPrice> list= execPriceDao.selExecPrice(scheduleBusId);
		return list;
	}
	
	@Override
	public List<TicketCateValue> selExecPriceBy(String scheduleBusId,String seatCate,Integer seats) {
		List<TicketCateValue> list= execPriceDao.selExecPriceBy(scheduleBusId,seatCate,seats);
		return list;
	}

	


	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void copyHolidayPrice(ScheduleBus bus,String holidayID) throws Exception {
		//1查询班次节日价格固定列部分列表
		Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
		wrapper.where("holiday_id={0} and schedule_tpl_id={1}", holidayID,bus.getScheduleTplId());
		List<HolidayPrice> holidayPriceList = holidayPriceService.selectList(wrapper);
		if(CollectionUtils.isEmpty(holidayPriceList)) {
			return;
		}
		Collection<String> hpIdList = Collections2.transform(holidayPriceList, new Function<HolidayPrice, String>() {
			@Override
			public String apply(HolidayPrice hp) {
				return hp.getId();
			}
		});
		
		//2查询出班次对应车型的所有票价
		List<TicketCateValue> hoTicValList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(hpIdList)) {
			Wrapper<TicketCateValue> ticValWrapper = new EntityWrapper<TicketCateValue>();
			ticValWrapper.where("price_tbl_type={0}", "2"); //2节假日票价表
			ticValWrapper.in("price_id", hpIdList);
			hoTicValList = ticketCateValueService.selectList(ticValWrapper);
		}
		
		//3开始拷贝固定列部分到执行票价表
		List<ExecPrice> epList = Lists.newArrayList();
		ExecPrice entity = null;
		for (HolidayPrice holidayPrice : holidayPriceList) {
			entity = new ExecPrice();
			BeanUtils.copyProperties(entity, holidayPrice);
			entity.setId(null);
			entity.setScheduleBusId(bus.getId());
			epList.add(entity);
		}
		this.insertBatch(epList);
		
		//4拷贝票种对应取值到执行票价
		TicketCateValue epv = null;
		List<TicketCateValue> tcvList = Lists.newArrayList();
		for (TicketCateValue ticketCateValue : hoTicValList) { //循环班次所有车型、票种取值，一项一项复制
			String oriPriceId = ticketCateValue.getPriceId(); //原票价表ID
			for (HolidayPrice holidayPrice : holidayPriceList) {
				if(oriPriceId.equals(holidayPrice.getId())) {
					String onStaId = holidayPrice.getOnStaId();
					String offStaId = holidayPrice.getOffStaId();
					String seatCate = holidayPrice.getSeatCate();
					Integer seats = holidayPrice.getSeats();
					String scheduleTplId = holidayPrice.getScheduleTplId();
					for (ExecPrice execPrice : epList) {
						String eOnStaId = execPrice.getOnStaId();
						String eOffStaId = execPrice.getOffStaId();
						String eSeatCate = execPrice.getSeatCate();
						Integer eSeats = execPrice.getSeats();
						if (onStaId.equals(eOnStaId) && offStaId.equals(eOffStaId) && seatCate.equals(eSeatCate)
								&& seats.intValue() == eSeats.intValue() && scheduleTplId.equals(bus.getScheduleTplId())) {
							epv = new TicketCateValue();
							BeanUtils.copyProperties(epv, ticketCateValue);
							epv.setId(null);
							epv.setPriceId(execPrice.getId());
							epv.setPriceTblType("3"); //执行票价表类型
							tcvList.add(epv);
						}
					}
				}
			}
		}
		ticketCateValueService.insertBatch(tcvList);
	}


	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void copyBasicPrice(ScheduleBus bus) throws Exception {
		//1查询班次基础价格固定列部分列表
		Wrapper<BasicPrice> wrapper = new EntityWrapper<BasicPrice>();
		wrapper.where("schedule_tpl_id={0}", bus.getScheduleTplId());
		List<BasicPrice> basicPriceList = basicPriceService.selectList(wrapper);
		if(CollectionUtils.isEmpty(basicPriceList)) {
			return;
		}
		Collection<String> bpIdList = Collections2.transform(basicPriceList, new Function<BasicPrice, String>() {
			@Override
			public String apply(BasicPrice hp) {
				return hp.getId();
			}
		});
		
		//2查询出班次对应车型的所有票价
		List<TicketCateValue> ticValList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(bpIdList)) {
			Wrapper<TicketCateValue> ticValWrapper = new EntityWrapper<TicketCateValue>();
			ticValWrapper.where("price_tbl_type={0}", "1"); //1基础票价表
			ticValWrapper.in("price_id", bpIdList);
			ticValList = ticketCateValueService.selectList(ticValWrapper);
		}
		
		//3开始拷贝固定列部分到执行票价表
		List<ExecPrice> epList = Lists.newArrayList();
		ExecPrice entity = null;
		for (BasicPrice basicPrice : basicPriceList) {
			entity = new ExecPrice();
			BeanUtils.copyProperties(entity, basicPrice);
			entity.setId(null);
			entity.setScheduleBusId(bus.getId());
			epList.add(entity);
		}
		this.insertBatch(epList);
		
		//4拷贝票种对应取值到执行票价
		TicketCateValue epv = null;
		List<TicketCateValue> tcvList = Lists.newArrayList();
		for (TicketCateValue ticketCateValue : ticValList) { //循环班次所有车型、票种取值，一项一项复制
			String oriPriceId = ticketCateValue.getPriceId(); //原票价表ID
			for (BasicPrice basicPrice : basicPriceList) {
				if(oriPriceId.equals(basicPrice.getId())) {
					String onStaId = basicPrice.getOnStaId();
					String offStaId = basicPrice.getOffStaId();
					String seatCate = basicPrice.getSeatCate();
					Integer seats = basicPrice.getSeats();
					String scheduleTplId = basicPrice.getScheduleTplId();
					for (ExecPrice execPrice : epList) {
						String eOnStaId = execPrice.getOnStaId();
						String eOffStaId = execPrice.getOffStaId();
						String eSeatCate = execPrice.getSeatCate();
						Integer eSeats = execPrice.getSeats();
						if (onStaId.equals(eOnStaId) && offStaId.equals(eOffStaId) && seatCate.equals(eSeatCate)
								&& seats.intValue() == eSeats.intValue() && scheduleTplId.equals(bus.getScheduleTplId())) {
							epv = new TicketCateValue();
							BeanUtils.copyProperties(epv, ticketCateValue);
							epv.setId(null);
							epv.setPriceId(execPrice.getId());
							epv.setPriceTblType("3"); //执行票价表类型
							tcvList.add(epv);
						}
					}
				}
			}
		}
		ticketCateValueService.insertBatch(tcvList);
	}
}
