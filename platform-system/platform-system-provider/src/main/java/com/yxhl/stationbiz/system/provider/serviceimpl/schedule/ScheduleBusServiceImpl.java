package com.yxhl.stationbiz.system.provider.serviceimpl.schedule;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.Lunar;
import com.yxhl.platform.common.utils.ThreadManager;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.stationbiz.system.domain.constants.SysConfigConstant;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.check.VehicleOutboundCheck;
import com.yxhl.stationbiz.system.domain.entity.schedule.ExecPrice;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBusStowage;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusReportStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusRunStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusSeatStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusStatusEnum;
import com.yxhl.stationbiz.system.domain.request.BusStowageRequest;
import com.yxhl.stationbiz.system.domain.request.CreateScheduleBusRequest;
import com.yxhl.stationbiz.system.domain.request.ScheduleDepartRequest;
import com.yxhl.stationbiz.system.domain.request.SettlementStatisticRequest;
import com.yxhl.stationbiz.system.domain.request.TicketCheckRequest;
import com.yxhl.stationbiz.system.domain.response.SettlementStatisticResponse;
import com.yxhl.stationbiz.system.domain.response.StatisticTCResponse;
import com.yxhl.stationbiz.system.domain.response.TicketCategoryCountResponse;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.domain.service.check.VehicleOutboundCheckService;
import com.yxhl.stationbiz.system.domain.service.schedule.ExecPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayPriceService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusStowageService;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCategoryService;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;
import com.yxhl.stationbiz.system.domain.service.ticket.TicketService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleBusTplDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleLoopDao;
import com.yxhl.stationbiz.system.provider.dao.schedule.ScheduleBusDao;
import com.yxhl.stationbiz.system.provider.task.CreateOperatePlanTask;
/**
 * @ClassName: ScheduleBusServiceImpl
 * @Description: 班次 serviceImpl
 * @author lw
 * @date 2018-7-10 16:42:40
 */
@Transactional(readOnly = true)
@Service("scheduleBusService")
public class ScheduleBusServiceImpl extends CrudService<ScheduleBusDao, ScheduleBus> implements ScheduleBusService {
	@Autowired
	private ScheduleBusDao scheduleBusDao;
	@Autowired
	private ScheduleLoopDao scheduleLoopDao;
	@Autowired
	private ScheduleBusTplDao scheduleBusTplDao;
	@Autowired
	private LineDao lineDao;
	@Autowired
	private HolidayPriceService holidayPriceService;
	@Autowired
	private ExecPriceService execPriceService;
    @Autowired
    private LineService lineService;
    @Autowired
    private ScheduleBusSeatsService scheduleBusSeatsService;
    @Autowired
    private ScheduleBusStowageService scheduleBusStowageService;
    @Autowired
    private VehicleOutboundCheckService vehicleOutboundCheckService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketCategoryService ticketCategoryService;
    @Autowired
    private StationService stationService;

	@Override
	public Page<ScheduleBus> selPageList(Page<ScheduleBus> page, ScheduleBus scheduleBus) {
		List<ScheduleBus> list = scheduleBusDao.selPageList(page, scheduleBus);
		page.setRecords(list);
		return page;
	}
	
	@Override
	public Page<ScheduleBus> selSchedulePageList(Page<ScheduleBus> page, ScheduleBus scheduleBus) {
		List<ScheduleBus> list = scheduleBusDao.selSchedulePageList(page, scheduleBus);
		page.setRecords(list);
		return page;
	}
	
	@Override
	public Page<ScheduleBus> checkBusPageList(Page<ScheduleBus> page, TicketCheckRequest req) {
		List<ScheduleBus> list = scheduleBusDao.checkBusPageList(page, req);
		page.setRecords(list);
		return page;
	}
	
	@Override
	public Page<SettlementStatisticResponse> selSettlementStatisticPageList(Page<SettlementStatisticResponse> page, SettlementStatisticRequest req) {
		String queryType = req.getQueryType();
		String orgId = req.getOrgId();
		String compId = req.getCompId();
		if(queryType.equals("2")) { //按月
			req.setStartDate(DateHelper.getFirstDateOfMonth(req.getMonth())+" 00:00:00");
			req.setEndDate(DateHelper.getLastDateOfMonth(req.getMonth())+" 23:59:59");
		}else if(queryType.equals("3")) { //按时段
			req.setStartDate(req.getStartDate()+" 00:00:00");
			req.setEndDate(req.getEndDate()+" 23:59:59");
		}
		//查询出班次基本信息及一些基本统计信息
		List<SettlementStatisticResponse> list = scheduleBusDao.selSettlementStatisticPageList(page, req);
		
		if(CollectionUtils.isNotEmpty(list)) {
			//设置出站检查员，各个票种售票数，站务费，客运结算金额统计结果
			settlementStatistic(orgId, compId, list);
		}
		
		page.setRecords(list);
		return page;
	}

	/**
	 * 设置出站检查员，各个票种售票数，站务费，客运结算金额
	 * @param orgId 机构ID
	 * @param compId 单位ID
	 * @param list 基本的统计信息
	 */
	private void settlementStatistic(String orgId, String compId, List<SettlementStatisticResponse> list) {
		//班次ID
		Collection<String> busIds = Collections2.transform(list, new Function<SettlementStatisticResponse, String>() {
			@Override
			public String apply(SettlementStatisticResponse resp) {
				return resp.getId();
			}
		});
		
		//站务费收取类型：1按固定金额收取，2按票面比例收取
		int stationFeeType = Integer.parseInt(configService.selByCode(orgId, SysConfigConstant.STATION_FEE_TYPE));
		float stationFeePercent = Float.parseFloat(configService.selByCode(orgId, SysConfigConstant.STATION_FEE_PERCENT)); //按每张票收取站务费的百分比
		float stationFeePerTicket = Float.parseFloat(configService.selByCode(orgId, SysConfigConstant.STATION_FEE_PER_TICKET)); //按每张车票收取X元站务费
		
		//查询稽查数据,包含出站检查员
		List<VehicleOutboundCheck> inspList = vehicleOutboundCheckService.selByScheduleIds(new ArrayList<String>(busIds));
		float stationFee = 0;
		for (SettlementStatisticResponse resp : list) {
			String scheduleBusId = resp.getId(); //班次ID
			Integer totalPassengerCount = resp.getTotalPassengerCount(); //合计人数
			Integer paymentPeople = resp.getPaymentPeople(); //补缴人数
			Float ticketAmount = resp.getTicketAmount(); //售票金额
			Float paymentFee = resp.getPaymentFee(); //补缴金额
			for (VehicleOutboundCheck outboundCheck : inspList) {
				String checkBusId = outboundCheck.getScheduleBusId();
				if(scheduleBusId.equals(checkBusId)) {
					//1 设置出站检查员
					resp.setOutboundChecker(outboundCheck.getCreator());
					continue;
				}
			}
			//2设置站务费
			if(stationFeeType == 1) {
				//站务费=（合计人数+补缴人数）*固定每张收取x元
				stationFee = (totalPassengerCount+paymentPeople) * stationFeePerTicket;
				resp.setStationFee(stationFee);
			}else if(stationFeeType == 2) {
				//站务费=（售票金额+补缴金额）* 站务抽成百分比
				stationFee = (ticketAmount+paymentFee) * stationFeePercent;
				resp.setStationFee(stationFee);
			}
		}
		
		Wrapper<TicketCategory> wrapper = new EntityWrapper<TicketCategory>();
		if(StringUtils.isNotBlank(orgId)) {
			wrapper.where("org_id={0}", orgId);
		}
		if(StringUtils.isNotBlank(compId)) {
			wrapper.and("comp_id={0}", compId);
		}
		List<TicketCategory> cateList = ticketCategoryService.selectList(wrapper); //全部票种
		for (SettlementStatisticResponse resp : list) {
			//3设置客运结算金额=总金额-站务费
			Float totalAmount = resp.getTotalAmount(); //总金额
			Float staFee = resp.getStationFee(); //站务费
			if(totalAmount == null || staFee == null) {
				resp.setSettlementAmount(Float.valueOf(0));
			}else {
				resp.setSettlementAmount(totalAmount-staFee);
			}
			
			//4设置各个票种售票数
			String scheduleBusId = resp.getId(); //班次ID
			List<StatisticTCResponse> statisticTCCount = ticketService.statisticTCCount(scheduleBusId);
			List<TicketCategoryCountResponse> tcCountList = getTCCount(cateList,statisticTCCount);
			resp.setTcCountList(tcCountList);
		}
	}

	/**
	 * 封装各票种售票数
	 * @param cateList 全部票种
	 * @param statisticTCCount 班次售票票种统计结果
	 * @return
	 */
	private List<TicketCategoryCountResponse> getTCCount(List<TicketCategory> cateList,
			List<StatisticTCResponse> statisticTCCount) {
		List<TicketCategoryCountResponse> list = Lists.newArrayList();
		TicketCategoryCountResponse resp = null;
		//循环班次售票票种
		for (StatisticTCResponse statisticTCResponse : statisticTCCount) {
			resp = new TicketCategoryCountResponse();
			String ticketCategoryId = statisticTCResponse.getTicketCategoryId();
			for (TicketCategory ticketCategory : cateList) {
				String id = ticketCategory.getId();
				if(ticketCategoryId.equals(id)) {
					resp.setTicketCategoryId(ticketCategoryId);
					resp.setSaleCount(statisticTCResponse.getSaleCount());
					resp.setTicketCateCode(ticketCategory.getTicketCateCode());
					resp.setTicketCateName(ticketCategory.getTicketCateName());
					list.add(resp);
					continue;
				}
			}
		}
		
		//票种排序
		Collections.sort(list,new Comparator<TicketCategoryCountResponse>() {
			@Override
			public int compare(TicketCategoryCountResponse o1, TicketCategoryCountResponse o2) {
				return o1.getTicketCateName().compareTo(o2.getTicketCateName());
			}
		});
		return list;
	}

	/**
	 * 生成班次规则，在开始日期基础上，根据循环类型计算
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void createScheduleTask() {
		//1 查询所有循环配置
		List<ScheduleLoop> loopList = scheduleLoopDao.selAllScheduleLoop();
		
		//生成流水班次 begin
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.where("run_flow_flag={0}", 1);
		List<ScheduleBusTpl> runFlowList = scheduleBusTplDao.selectList(wrapper);
		for (ScheduleBusTpl scheduleBusTpl : runFlowList) {
			//生成班次之前先判断今日该班次是否已经生成
			ScheduleBus entity = new ScheduleBus();
			entity.setBusCode(scheduleBusTpl.getBusCode());
			entity.setScheduleTplId(scheduleBusTpl.getId());
			entity.setRunDate(DateHelper.getTodayDate());
			entity.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
			ScheduleBus curBus = scheduleBusDao.selectOne(entity);
			if(curBus != null) {
				continue;
			}
			ScheduleBus bus = new ScheduleBus(scheduleBusTpl,new Date());
			Line line = lineDao.selectById(scheduleBusTpl.getLineId());
			bus.setLineName(line != null ? line.getLineName() : "");
			Station startStation = stationService.selectById(line.getStartStateId());
			Station endStation = stationService.selectById(line.getEndStateId());
			bus.setStartStation(startStation.getStationName());
			bus.setEndStation(endStation.getStationName());
			scheduleBusDao.insert(bus);
			
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
		}
		//生成流水班次 end
		
		//固定班次生成
		for (ScheduleLoop scheduleLoop : loopList) {
			/*Integer runFlowFlag = scheduleLoop.getRunFlowFlag();
			
			//流水班，不需要根据循环配置生成，只生成一条
			if(runFlowFlag == 1) {
				ScheduleBus bus = new ScheduleBus(scheduleLoop);
				scheduleBusDao.insert(bus);
			}else {
				createBus(scheduleLoop,DateHelper.getTodayDate());
			}*/
			createBus(scheduleLoop,DateHelper.getTodayDate());
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void createOperatePlan(CreateScheduleBusRequest req) {
		String operateType = req.getOperateType(); //操作类型；create:制作计划；delete:删除计划
		String operation = req.getOperation(); //操作；currentSel:当前选中；line:本线路所有班次；company:本单位所有班次
		String lineId = req.getLineId(); // 线路ID
		Date planStartDate = req.getStartDate(); //开始日期
		Date planEndDate = req.getEndDate(); //结束日期
		
		//制作运营计划
		if("create".equalsIgnoreCase(operateType)) {
			if("currentSel".equalsIgnoreCase(operation)) //当前选中班次
			{
				//选中的班次模板ID列表
				List<String> scheduleTplList = req.getScheduleTplList();
				
				//循环每个班次模板，根据模板配置的规则依次生成班次
				for (String tplId : scheduleTplList) {
					//查询出该班次的循环配置信息
					ScheduleLoop loop = new ScheduleLoop();
					loop.setTplId(tplId);
					List<ScheduleLoop> selScheduleLoop = scheduleLoopDao.selScheduleLoop(loop);
					createScheduleOfTpl(planStartDate, planEndDate, selScheduleLoop);
				}
			}else if("line".equalsIgnoreCase(operation)) { //line:本线路所有班次
				ScheduleLoop loop = new ScheduleLoop();
				loop.setLineId(lineId);
				List<ScheduleLoop> selScheduleLoop = scheduleLoopDao.selScheduleLoop(loop);
				createScheduleOfTpl(planStartDate, planEndDate, selScheduleLoop);
			}else if("company".equalsIgnoreCase(operation)) { //company:本单位所有班次
				ScheduleLoop loop = new ScheduleLoop();
				loop.setCompId(req.getCompId());
				List<ScheduleLoop> selScheduleLoop = scheduleLoopDao.selScheduleLoop(loop);
				createScheduleOfTpl(planStartDate, planEndDate, selScheduleLoop);
			}
		}
		//删除运营计划
		else if("delete".equalsIgnoreCase(operateType)) {
			Collection<String> scheduleTplList = null;
			if("currentSel".equalsIgnoreCase(operation)) //删除当前选中班次
			{
				//选中的班次模板ID列表
				scheduleTplList = req.getScheduleTplList();
			}else if("line".equalsIgnoreCase(operation)) { //line:删除本线路所有班次
				ScheduleLoop loop = new ScheduleLoop();
				loop.setLineId(lineId);
				List<ScheduleLoop> selScheduleLoop = scheduleLoopDao.selScheduleLoop(loop);
				scheduleTplList = Collections2.transform(selScheduleLoop, new Function<ScheduleLoop, String>() {
					@Override
					public String apply(ScheduleLoop loop) {
						return loop.getTplId();
					}
				});
				
			}else if("company".equalsIgnoreCase(operation)) { //company:删除本单位所有班次
				ScheduleLoop loop = new ScheduleLoop();
				loop.setCompId(req.getCompId());
				List<ScheduleLoop> selScheduleLoop = scheduleLoopDao.selScheduleLoop(loop);
				scheduleTplList = Collections2.transform(selScheduleLoop, new Function<ScheduleLoop, String>() {
					@Override
					public String apply(ScheduleLoop loop) {
						return loop.getTplId();
					}
				});
			}
			
			//删除指定日期内选中班次模板的班次
			Wrapper<ScheduleBus> wrapper = new EntityWrapper<ScheduleBus>();
			if(CollectionUtils.isNotEmpty(scheduleTplList)) {
				wrapper.in("schedule_tpl_id", scheduleTplList);
				wrapper.between("run_date", planStartDate, planEndDate);
				scheduleBusDao.delete(wrapper);
			}
		}
	}
	
	@Override
	public void createOperPlanTask(CreateScheduleBusRequest req) {
		ThreadManager.execute(new CreateOperatePlanTask(this, req));
	}

	/**
	 * 根据班次模板生成班次
	 * @param planStartDate 计划开始日期
	 * @param planEndDate 计划结束日期
	 * @param selScheduleLoop 班次模板列表
	 */
	private void createScheduleOfTpl(Date planStartDate, Date planEndDate, List<ScheduleLoop> selScheduleLoop) {
		for (ScheduleLoop scheduleLoop : selScheduleLoop) {
			//1获得日期交集,这些日期是需要生成班次的日期
			Map<String, Date> intersectionBetDates = DateHelper.getIntersectionBetDates(planStartDate, planEndDate, scheduleLoop.getStartDate(), scheduleLoop.getEndDate());
			if(!intersectionBetDates.isEmpty()) {
				Date startDate = intersectionBetDates.get("start_date");
				Date endDate = intersectionBetDates.get("end_date");
				
				double distanceOfTwoDate = DateHelper.getDistanceOfTwoDate(startDate, endDate); //相隔多少天，就需要循环多少次去生成班次
				for(int i=0; i<(Double.valueOf(distanceOfTwoDate).intValue()+1); i++) {
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.add(Calendar.DATE, i);
					
					//2生成班次：取交集日期一个个判断，是否生成当天班次
					createBus(scheduleLoop,c.getTime());
				}
			}
		}
	}
	
	/**
	 * 根据传入的日期生成指定班次
	 * @param scheduleLoop 班次模板信息
	 * @param createDate 生成日期
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private void createBus(ScheduleLoop scheduleLoop,Date createDate) {
		Integer runFlowFlag = scheduleLoop.getRunFlowFlag();
		
		//生成班次之前先判断今日该班次是否已经生成
		ScheduleBus entity = new ScheduleBus();
		entity.setBusCode(scheduleLoop.getBusCode());
		entity.setScheduleTplId(scheduleLoop.getTplId());
		entity.setRunDate(createDate);
		if(runFlowFlag == 1) { //流水班，只查询模板数据
			entity.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
		}
		ScheduleBus curBus = scheduleBusDao.selectOne(entity);
		if(curBus != null) {
			logger.info("-----------班次已经生成：" + curBus.getLineName() + "["+curBus.getBusCode()+"]------------");
			return;
		}
		
		Date startDate = scheduleLoop.getStartDate(); //循环开始日期
		Date endDate = scheduleLoop.getEndDate(); // 循环结束日期
		Integer loopType = scheduleLoop.getLoopType(); //循环类型(0每日、1农历单、2农历双、3每周、4隔周、5隔日、6月班)
		boolean createBus = isCreateBus(startDate,endDate,loopType,createDate);
		if(createBus) {
			ScheduleBusTpl tpl = scheduleBusTplDao.selectById(scheduleLoop.getTplId());
			ScheduleBus bus = new ScheduleBus(tpl,createDate);
			Line line = lineDao.selectById(tpl.getLineId());
			bus.setLineName(line != null ? line.getLineName() : "");
			Station startStation = stationService.selectById(line.getStartStateId());
			Station endStation = stationService.selectById(line.getEndStateId());
			bus.setStartStation(startStation.getStationName());
			bus.setEndStation(endStation.getStationName());
			scheduleBusDao.insert(bus);
			
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
		}
	}
	
	/**
	 * 判断createDate这天是不是符合循环规则，符合则需要生成createDate那天的班次
	 * @param createDate 
	 * @return true：符合；false：不符合
	 */
	private boolean isCreateBus(Date startDate,Date endDate,Integer loopType, Date createDate) {
		ArrayList<String> oddList = Lists.newArrayList("一", "三", "五", "七","九");
		ArrayList<String> evenList = Lists.newArrayList("二", "四", "六", "八", "十");
		boolean isCreate = false;
		switch (loopType) {
			case 0:{ //每日
				isCreate = true;
				break;
			}
			case 1:{ //1农历单
				Calendar c = Calendar.getInstance();
				c.setTime(createDate);
				Lunar lunar = new Lunar(c);
				String date = lunar.getDate();
				if(oddList.contains(date)) {
					isCreate = true;
				}
				break;
			}
			case 2:{ //2农历双
				Calendar c = Calendar.getInstance();
				c.setTime(createDate);
				Lunar lunar = new Lunar(c);
				String date = lunar.getDate();
				if(evenList.contains(date)) {
					isCreate = true;
				}
				break;
			}
			case 3:{ //3每周 
				int stepVal = 7; 
				isCreate = iscreateDateMatch(startDate, endDate, stepVal,createDate);
				break;
			}
			case 4:{ //4隔周
				int stepVal = 14; 
				isCreate = iscreateDateMatch(startDate, endDate, stepVal,createDate);
				break;
			}
			case 5:{ //5隔日
				int stepVal = 2; 
				isCreate = iscreateDateMatch(startDate, endDate, stepVal,createDate);
				break;
			}
			case 6:{ //6月班
				int monthSpace = DateHelper.getMonthSpace(startDate, endDate);
				for (int i=1; i<=monthSpace; i++) {
					Calendar c = Calendar.getInstance();
					c.setTime(startDate);
					c.add(Calendar.MONTH, i*1);
					
					Calendar cc = Calendar.getInstance();
					cc.setTime(createDate);
					if (DateHelper.isSameDay(c, cc)) {
						isCreate = true;
						break;
					}
				}
				break;
			}
			default:{
				break;
			}
		}
		return isCreate;
	}

	private boolean iscreateDateMatch(Date startDate, Date endDate, int stepVal, Date createDate) {
		boolean isMatch = false;
		//生成日期=开始日期，允许生成
		if(DateHelper.isSameDay(startDate, createDate)) {
			return true;
		}
		double diffDay = DateHelper.getDistanceOfTwoDate(startDate, endDate);
		double floor = Math.floor(diffDay / stepVal);
		int diffSetps = Double.valueOf(floor).intValue(); //相隔多少个步长
		for (int i=1; i<=diffSetps; i++) {
			Calendar c = Calendar.getInstance();
			c.setTime(startDate);
			c.add(Calendar.DATE, stepVal*i);
			
			Calendar cc = Calendar.getInstance();
			cc.setTime(createDate);
			if (DateHelper.isSameDay(c, cc)) {
				isMatch = true;
				break;
			}else {
				continue;
			}
		}
		return isMatch;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean addOverTimeBus(ScheduleBus scheduleBus) {
		Integer a = DateHelper.differentDays(scheduleBus.getStartDate(), scheduleBus.getEndDate());
		Wrapper<Line> wrapper = new EntityWrapper<Line>();
		wrapper.eq("id", scheduleBus.getLineId());
		Line line = lineService.selectOne(wrapper);
		List<ScheduleBus> list = new ArrayList<ScheduleBus>();
		boolean isAdded = false;
		if (a == 0) {
			Wrapper<ScheduleBus> scwrapper = new EntityWrapper<ScheduleBus>();
			scwrapper.eq("bus_code", scheduleBus.getBusCode());
			scwrapper.eq("run_date", scheduleBus.getStartDate());
			scwrapper.eq("org_id", scheduleBus.getOrgId());
			scwrapper.eq("comp_id", scheduleBus.getCompId());
			List<ScheduleBus> list1 = this.selectList(scwrapper);
			if (list1.size() > 0) {
				throw new YxBizException("该班次此时间已存在!");
			}
			scheduleBus.setId(IdWorker.get32UUID());
			scheduleBus.setRunDate(scheduleBus.getStartDate());
			scheduleBus.setOvertimeBusFlag(1);
			scheduleBus.setBusStatus(1);
			scheduleBus.setCheckStatus(2);
			scheduleBus.setReportStatus(2);
			scheduleBus.setPrintSeatFlag(1);
			scheduleBus.setNeedDriverReportFlag(0);
			scheduleBus.setPrintStartTimeFlag(1);
			scheduleBus.setAutoReportFlag(1);
			scheduleBus.setRunStatus(0);
			scheduleBus.setLineName(line.getLineName());
			list.add(scheduleBus);
			isAdded = this.insert(scheduleBus);
		} else {
			for (int i = 0; i <= a; i++) {
				ScheduleBus sa = new ScheduleBus();
				// scheduleBus.setId(IdWorker.get32UUID());
				scheduleBus.setOrgId(scheduleBus.getOrgId());
				scheduleBus.setCompId(scheduleBus.getCompId());
				Calendar c = Calendar.getInstance();
				c.setTime(scheduleBus.getStartDate());
				c.add(Calendar.DAY_OF_MONTH, i);// 今天+1天
				Wrapper<ScheduleBus> scwrapper = new EntityWrapper<ScheduleBus>();
				scwrapper.eq("bus_code", scheduleBus.getBusCode());
				scwrapper.eq("run_date", c.getTime());
				scwrapper.eq("org_id", scheduleBus.getOrgId());
				scwrapper.eq("comp_id", scheduleBus.getCompId());
				List<ScheduleBus> slist = this.selectList(scwrapper);
				if (slist.size() > 0) {
					continue;
				}
				scheduleBus.setRunDate(c.getTime());
				scheduleBus.setOvertimeBusFlag(1);
				scheduleBus.setBusStatus(1);
				scheduleBus.setCheckStatus(2);
				scheduleBus.setReportStatus(2);
				scheduleBus.setPrintSeatFlag(1);
				scheduleBus.setNeedDriverReportFlag(0);
				scheduleBus.setPrintStartTimeFlag(1);
				scheduleBus.setAutoReportFlag(1);
				scheduleBus.setRunStatus(0);
				scheduleBus.setLineName(line.getLineName());
				try {
					BeanUtils.copyProperties(sa, scheduleBus);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
				list.add(sa);
			}
			if (list.size() > 0) {
				isAdded = this.insertBatch(list);
			}
			for (ScheduleBus scheduleBus2 : list) {
				// 判断班次日期是否满足节假日票价设置，满足则从节假日票价复制到执行票价表，否则从基础票价表复制
				String holidayID = holidayPriceService.isHolidayPriceSet(scheduleBus2);
				try {
					if (StringUtils.isNotBlank(holidayID)) {
						execPriceService.copyHolidayPrice(scheduleBus2, holidayID);
					} else {
						execPriceService.copyBasicPrice(scheduleBus2);
					}
				} catch (Exception e) {
					logger.error("===拷贝票价出错===", e);
				}
			}
		}
		return isAdded;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean mergeBus(String beMergedBusId, String mergeBusId, String mergeReason) {
		//1并入班次占座（占座数=被并班次已售座位数）
		//查询被并班次已售座位数
		Wrapper<ScheduleBusSeats> bmt = new EntityWrapper<ScheduleBusSeats>();
		bmt.where("schedule_bus_id={0} and seat_status={1}", beMergedBusId, ScheduleBusSeatStatusEnum.SALED.getType());
		int bmCount = scheduleBusSeatsService.selectCount(bmt);
		//查询并入班次剩余座位
		Wrapper<ScheduleBusSeats> mt = new EntityWrapper<ScheduleBusSeats>();
		mt.where("schedule_bus_id={0} and seat_status={1}", mergeBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
		mt.orderBy("seat_num", true);
		List<ScheduleBusSeats> msList = scheduleBusSeatsService.selectList(mt);
		List<ScheduleBusSeats> subList = msList.subList(0, bmCount);
		List<String> ids = Lists.newArrayList();
		for (ScheduleBusSeats scheduleBusSeats : subList) {
			ids.add(scheduleBusSeats.getId());
		}
		boolean saleFlag = scheduleBusSeatsService.updateScheduleBusSeatStatus(ids); //批量修改座位为已售
		
		//2被并班次停班
		ScheduleBus bus = new ScheduleBus();
		bus.setId(beMergedBusId);
		bus.setBusStatus(ScheduleBusStatusEnum.BE_MERGED.getType());
		bus.setMergeReason(mergeReason);
		boolean stopFlag = updateById(bus);
		
		//3并入班次关联被并班次ID
		ScheduleBus mergedBus = new ScheduleBus();
		mergedBus.setId(mergeBusId);
		mergedBus.setMergedBusId(beMergedBusId);
		mergedBus.setBusStatus(ScheduleBusStatusEnum.MERGE.getType());
		mergedBus.setMergeReason(mergeReason);
		boolean merged = updateById(mergedBus);
		return (saleFlag && stopFlag && merged);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean stowageBus(BusStowageRequest req) {
		List<String> candidateBusIds = req.getCandidateBusIds();
		String belongBusId = req.getBelongBusId();
		ScheduleBusStowage stowage = null;
		ScheduleBus beStowagedBus = null;
		List<ScheduleBusStowage> list = Lists.newArrayList();
		List<ScheduleBus> beStowagedBusList = Lists.newArrayList();
		for (String candidateBusId : candidateBusIds) {
			stowage = new ScheduleBusStowage();
			stowage.setCandidateBusId(candidateBusId);
			stowage.setBelongBusId(belongBusId);
			list.add(stowage);
			
			beStowagedBus = new ScheduleBus();
			beStowagedBus.setId(candidateBusId);
			beStowagedBus.setBusStatus(ScheduleBusStatusEnum.BE_STOWAGED.getType());
			beStowagedBusList.add(beStowagedBus);
		}
		
		//批量修改待选班次状态为被配载
		updateBatchById(beStowagedBusList);
		ScheduleBus belongBus = new ScheduleBus();
		belongBus.setId(belongBusId);
		belongBus.setBusStatus(ScheduleBusStatusEnum.STOWAGE.getType());
		updateById(belongBus); //修改从属班次状态为配载
		
		//添加关联关系
		return scheduleBusStowageService.insertBatch(list);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean cancleDepart(ScheduleBus scheduleBus) {
		scheduleBus.setRunStatus(ScheduleBusRunStatusEnum.NOT_RUN.getType());
		scheduleBus.setTriplicateBillNum(null); //三联单号置空
		boolean updated = updateAllColumnById(scheduleBus);
		return updated;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean depart(ScheduleDepartRequest req,String biller) {
		String triplicateBillNum = req.getTriplicateBillNum();
		ScheduleBus scheduleBus = selectById(req.getScheduleBusId());
		scheduleBus.setRunStatus(ScheduleBusRunStatusEnum.RUNNING.getType());
		scheduleBus.setTriplicateBillNum(triplicateBillNum); //三联单号
		scheduleBus.setTriplicateBiller(biller);
		boolean updated = updateById(scheduleBus);
		return updated;
	}

	@Override
	public List<SettlementStatisticResponse> expSettlementStatisticList(SettlementStatisticRequest req) {
		String queryType = req.getQueryType();
		String orgId = req.getOrgId();
		String compId = req.getCompId();
		if(queryType.equals("2")) { //按月
			req.setStartDate(DateHelper.getFirstDateOfMonth(req.getMonth())+" 00:00:00");
			req.setEndDate(DateHelper.getLastDateOfMonth(req.getMonth())+" 23:59:59");
		}else if(queryType.equals("3")) { //按时段
			req.setStartDate(req.getStartDate()+" 00:00:00");
			req.setEndDate(req.getEndDate()+" 23:59:59");
		}
		List<SettlementStatisticResponse> list = scheduleBusDao.expSettlementStatisticList(req);
		if(CollectionUtils.isNotEmpty(list)) {
			//设置出站检查员，各个票种售票数，站务费，客运结算金额统计结果
			settlementStatistic(orgId, compId, list);
		}
		return list;
	}

	
	@Override
	public Page<ScheduleBus> queryScheduleBusList(Page<ScheduleBus> page, ScheduleBus bus) {
		List<ScheduleBus> list = scheduleBusDao.queryScheduleBusList(page, bus);
		for(ScheduleBus scheduleBus:list) {
			//查询执行票价
			List<TicketCateValue> ticketCateValues = execPriceService.selExecPriceBy(scheduleBus.getId(), scheduleBus.getVehicleType() , scheduleBus.getSeats());
			scheduleBus.setTicketCateValues(ticketCateValues); 
			
			if(Util.isNotNull(scheduleBus.getBelongBusId())) {
				//这里是 处理配载班次
				List<Map<String,Object>> seatsList=scheduleBusDao.queryBelongStowageSeats(scheduleBus.getBelongBusId());
				if(null!=seatsList&&seatsList.size()>0) {
					for(int i=0;i<seatsList.size();i++) {
						Map<String,Object> map=seatsList.get(i); 
						String seatStatus= map.get("seatStatus").toString(); //seatStatus 2 表示可售车票数   5 表示已售车票数
						Integer counts= Integer.valueOf(map.get("counts").toString());
						if(StringUtils.equals(seatStatus, "5")) { //seatStatus 2 表示可售车票数   5 表示已售车票数
							Integer availableSeats= scheduleBus.getAvailableSeats()-counts;
							Integer alreadySeat= scheduleBus.getAlreadySeat()+counts;
							checkArgument(availableSeats>=0,"操作失败，已售车票数超出了可售车票范围");
							checkArgument(alreadySeat<=scheduleBus.getSeats(),"操作失败，已售车票数超出了可售车票范围");
							scheduleBus.setAvailableSeats(availableSeats);
							scheduleBus.setAlreadySeat(alreadySeat);
						}
					}
				}
			}
			
			if(Util.isNotNull(scheduleBus.getCandidateBusId())) {
				List<Map<String,Object>> seatsList=scheduleBusDao.queryCandidateStowageSeats(scheduleBus.getCandidateBusId());
				List<Map<String,Object>> listMap=Lists.newArrayList();
				if(null!=seatsList&&seatsList.size()>0) {
					for(int i=0;i<seatsList.size();i++) {
						Map<String,Object> map=seatsList.get(i); 
						
						String seatStatus= map.get("seatStatus").toString(); //seatStatus 2 表示可售车票数   5 表示已售车票数
						Integer counts= Integer.valueOf(map.get("counts").toString());
						if(listMap.size()==0) {
							listMap=scheduleBusDao.queryStowageByScheduleId(map.get("scheduleBusId").toString()); //查询 从属班 的   可售座位  和已售座位
						}
						if(StringUtils.equals(seatStatus, "5")) {//seatStatus 2 表示可售车票数   5 表示已售车票数

							if(listMap.size()==1) {
								Map<String,Object> parentMap= listMap.get(0);
								String mainSeatStatus= parentMap.get("seatStatus").toString(); //seatStatus 2 表示可售车票数   5 表示已售车票数
								Integer mainCounts= Integer.valueOf(parentMap.get("counts").toString());
								if(StringUtils.equals(mainSeatStatus, "5")) {
									Integer alreadySeat=mainCounts+counts;   //从属班次的已售座位   + 可选班次的已售座位的总数
									scheduleBus.setAlreadySeat(alreadySeat);
								}else if(StringUtils.equals(mainSeatStatus, "2")) {
									scheduleBus.setAvailableSeats(mainCounts);
								}
							}else if(listMap.size()==2) {
								Map<String,Object> parentMap1= listMap.get(0);
								String mainSeatStatus1= parentMap1.get("seatStatus").toString(); //seatStatus 2 表示可售车票数   5 表示已售车票数
								Integer mainCounts1= Integer.valueOf(parentMap1.get("counts").toString());
								
								Map<String,Object> parentMap2= listMap.get(1);
								String mainSeatStatus2= parentMap2.get("seatStatus").toString(); //seatStatus 2 表示可售车票数   5 表示已售车票数
								Integer mainCounts2= Integer.valueOf(parentMap2.get("counts").toString());
								
								
								if(StringUtils.equals(mainSeatStatus1, "5")) {//    5 表示已售车票数
									Integer alreadySeat=mainCounts1+counts;   //从属班次的已售座位   + 可选班次的已售座位的总数
									scheduleBus.setAlreadySeat(alreadySeat);
									
								}else if(StringUtils.equals(mainSeatStatus2, "5")) {//   5 表示已售车票数
									Integer alreadySeat=mainCounts2+counts;   //从属班次的已售座位   + 可选班次的已售座位的总数
									scheduleBus.setAlreadySeat(alreadySeat);
								}
								
								if(StringUtils.equals(mainSeatStatus1, "2")) { //2 表示可售车票数
									Integer availableSeats= mainCounts1-counts;  //从属班次的可售座位数   - 可选班次的可售座位总和
									checkArgument(availableSeats>=0,"操作失败，可售座位数计算出错");
									scheduleBus.setAvailableSeats(availableSeats);
									
								}else if(StringUtils.equals(mainSeatStatus2, "2")) { //2 表示可售车票数
									Integer availableSeats= mainCounts2-counts;   //从属班次的可售座位数   - 可选班次的可售座位总和
									checkArgument(availableSeats>=0,"操作失败，可售座位数计算出错");
									scheduleBus.setAvailableSeats(availableSeats);
								}
								
							}
							
						}
						
					}
				}
				
			}
			
		}
		page.setRecords(list);
		return page;
	}

	
}