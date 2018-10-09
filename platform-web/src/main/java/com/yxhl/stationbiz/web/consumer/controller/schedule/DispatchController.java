package com.yxhl.stationbiz.web.consumer.controller.schedule;




import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.redis.util.RedisLockUtil;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.constants.SysConfigConstant;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.entity.ticket.ScheduleBusSeats;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusRunStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusSeatStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusStatusEnum;
import com.yxhl.stationbiz.system.domain.request.BusStowageRequest;
import com.yxhl.stationbiz.system.domain.request.ScheduleBusRequest;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.ticket.ScheduleBusSeatsService;
import com.yxhl.stationbiz.system.domain.service.ticket.TicketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  排班、报班
 *  创建人: lw
 *  创建日期:2018-7-10 16:42:40
 */
@RestController
@Api(tags = "调度")
@RequestMapping(value = "/m/dispatch/")
public class DispatchController extends BaseController{
	
    @Autowired
    private ScheduleBusService scheduleBusService;
    
    @Autowired
	private OperateLogService logService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ScheduleBusSeatsService scheduleBusSeatsService;
    @Autowired
    private RedisLockUtil lock;
    @Autowired
    private ConfigService configService;
    
	@ApiOperation(value = "查寻该线路指定时间的班次", notes = "查寻该线路指定时间的班次", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/busList", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@RequestParam("lineId") String lineId,@RequestParam("runDate")String runDate) {
		Wrapper<ScheduleBus> wrapper= new EntityWrapper<ScheduleBus>();
		wrapper.eq("line_id", lineId);
		wrapper.eq("run_date", runDate);
		List<ScheduleBus> list=scheduleBusService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}


	@ApiOperation(value = "新增加班车", notes = "新增加班车", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleBus(@RequestBody ScheduleBus scheduleBus) {
		scheduleBus.setOrgId(getLoginUser().getOrgId());
		scheduleBus.setCompId(getLoginUser().getCompanyId());
        boolean isAdded=scheduleBusService.addOverTimeBus(scheduleBus);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.DISPATCH.getModule(),"添加加班班次",getRemoteAddr(),"班次号【"+scheduleBus.getBusCode()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(scheduleBus);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次", notes = "修改班次信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBus(@RequestBody ScheduleBusRequest scheduleBus) {
		List<String> list= scheduleBus.getIds();
		List<ScheduleBus> busList=new ArrayList<ScheduleBus>();
		if(list.size()==0) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "请选择你要修改的班次信息");
		}
		for (String ids : list) {
			ScheduleBus bus= new ScheduleBus();
			bus.setId(ids);
			bus.setRunTime(scheduleBus.getRunTime());
			bus.setTicketGateId(scheduleBus.getTicketGateId());
			bus.setBusEntranceId(scheduleBus.getBusEntranceId());
			bus.setCarryChildrenNum(scheduleBus.getCarryChildrenNum());
			busList.add(bus);
		}
		boolean isUpdate= scheduleBusService.updateBatchById(busList);
		if(isUpdate) {
			logService.insertLog(OperateLogModelEnum.DISPATCH.getModule(),"修改班次计划",getRemoteAddr(),"班次时间【"+scheduleBus.getRunTime()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(scheduleBus);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}
	
	@ApiOperation(value = "停班", notes = "停班", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/stop/{scheduleBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse stopBus(@PathVariable("scheduleBusId") String scheduleBusId) {
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("schedule_bus_id={0} and status={1}", scheduleBusId, 1);
		List<Ticket> ticketList = ticketService.selectList(wrapper);
		checkArgument(CollectionUtils.isEmpty(ticketList), "已售车票班次不能停班！");
		
		ScheduleBus bus = new ScheduleBus();
		bus.setId(scheduleBusId);
		bus.setBusStatus(ScheduleBusStatusEnum.STOP.getType());
		boolean isUpdated = scheduleBusService.updateById(bus);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "停班失败!");
		}
	}
	
	@ApiOperation(value = "并班", notes = "并班", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/merge", produces = "application/json;charset=UTF-8")
	public CommonResponse mergeBus(@RequestParam("beMergedBusId") String beMergedBusId,@RequestParam("mergeBusId") String mergeBusId,@RequestParam("mergeReason") String mergeReason) {
		ELUser loginUser = getLoginUser();
		List<ScheduleBusSeats> bmosList = Lists.newArrayList();
		List<ScheduleBusSeats> mosList = Lists.newArrayList();
		boolean merged = false;
		try {
			//锁定被并班次以及并入班次可选座位，防止在并班过程中被售出  begin
			//查询锁票时间
			Wrapper<Config> lt = new EntityWrapper<Config>();
			lt.where("org_id={0} and code={1}", loginUser.getOrgId(),SysConfigConstant.SEAT_LOCK_EXPIRE);
			Config config = configService.selectOne(lt);
			String lockTime = config.getValue(); //锁票时长(分钟)
			long expire = Long.parseLong(lockTime) * 60 * 1000; //转化为毫秒 
			
			//被并班次可选座位
			Wrapper<ScheduleBusSeats> bmos = new EntityWrapper<ScheduleBusSeats>();
			bmos.where("schedule_bus_id={0} and seat_status={1}", beMergedBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
			bmosList = scheduleBusSeatsService.selectList(bmos);
			//并入班次可选座位
			Wrapper<ScheduleBusSeats> mos = new EntityWrapper<ScheduleBusSeats>();
			mos.where("schedule_bus_id={0} and seat_status={1}", mergeBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
			mosList = scheduleBusSeatsService.selectList(mos);
			for (ScheduleBusSeats scheduleBusSeats : bmosList) {
				lock.lock(scheduleBusSeats.getId(), loginUser.getId(), expire);
			}
			for (ScheduleBusSeats scheduleBusSeats : mosList) {
				lock.lock(scheduleBusSeats.getId(), loginUser.getId(), expire);
			}
			//锁定被并班次以及并入班次可选座位，防止在并班过程中被售出  end
					
			//1被并班次未售票，不能并班！
			/*Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
			wrapper.where("schedule_bus_id={0} and status={1}", beMergedBusId, 1);
			List<Ticket> ticketList = ticketService.selectList(wrapper);
			checkArgument(CollectionUtils.isNotEmpty(ticketList), "被并班次未售票，不能并班！");*/
			
			//2并入班次<被并班次座位数，并入失败
			//查询被并班次已售座位数
			Wrapper<ScheduleBusSeats> bmt = new EntityWrapper<ScheduleBusSeats>();
			bmt.where("schedule_bus_id={0} and seat_status={1}", beMergedBusId, ScheduleBusSeatStatusEnum.SALED.getType());
			int bmCount = scheduleBusSeatsService.selectCount(bmt);
			checkArgument(bmCount>0, "被并班次未售票，不能并班！");
			
			//查询并入班次剩余座位数
			Wrapper<ScheduleBusSeats> mt = new EntityWrapper<ScheduleBusSeats>();
			mt.where("schedule_bus_id={0} and seat_status={1}", mergeBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
			int mCount = scheduleBusSeatsService.selectCount(mt);
			checkArgument(bmCount<=mCount, "被并班次已售座位数大于并入班次可售座位数，不能并班！");
			
			//3已发班班次不能并班
			ScheduleBus bmb = scheduleBusService.selectById(beMergedBusId);
			checkArgument(bmb.getRunStatus() !=ScheduleBusRunStatusEnum.RUNNING.getType(), "班次："+bmb.getBusCode()+"已经发班，不能并班！");
			ScheduleBus mb = scheduleBusService.selectById(mergeBusId);
			checkArgument(mb.getRunStatus() !=ScheduleBusRunStatusEnum.RUNNING.getType(), "班次："+mb.getBusCode()+"已经发班，不能并班！");
			
			//被并班次不能重复被并班
			if(bmb.getBusStatus() == ScheduleBusStatusEnum.BE_MERGED.getType()) {
				throw new YxBizException("班次："+bmb.getBusCode()+"已经被并过班，不能重复并班！");
			}
			//并入班次不能重复并班
			if(mb.getBusStatus() == ScheduleBusStatusEnum.BE_MERGED.getType()) {
				throw new YxBizException("班次："+mb.getBusCode()+"已经被并过班，不能重复并班！");
			}
			
			//4执行并班
			merged = scheduleBusService.mergeBus(beMergedBusId,mergeBusId,mergeReason);
		}finally {
			//5释放锁
			for (ScheduleBusSeats scheduleBusSeats : bmosList) {
				lock.unlock(scheduleBusSeats.getId(), loginUser.getId());
			}
			for (ScheduleBusSeats scheduleBusSeats : mosList) {
				lock.unlock(scheduleBusSeats.getId(), loginUser.getId());
			}
		}
		
		if(merged) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "并班失败!");
		}
	}
	
	@ApiOperation(value = "配载", notes = "配载", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/stowage", produces = "application/json;charset=UTF-8")
	public CommonResponse stowageBus(@RequestBody BusStowageRequest req) {
		ELUser loginUser = getLoginUser();
		List<String> candidateBusIds = req.getCandidateBusIds();
		String belongBusId = req.getBelongBusId();
		
		//校验，待选班次不能被重复配载
		List<ScheduleBus> candidateBuss = scheduleBusService.selectBatchIds(candidateBusIds);
		for (ScheduleBus scheduleBus : candidateBuss) {
			Integer busStatus = scheduleBus.getBusStatus();
			checkArgument(busStatus!=ScheduleBusStatusEnum.BE_STOWAGED.getType(), "待选班次:【"+scheduleBus.getBusCode()+"】已经被配载到其它班次，配载失败！");
		}
		
		List<ScheduleBusSeats> candidateSeatList = Lists.newArrayList(); //待选班次可选座位
		List<ScheduleBusSeats> belongSeatList = Lists.newArrayList(); //从属班次可选座位
		boolean stowageFlag = false;
		try {
			//锁定被并班次以及并入班次可选座位，防止在并班过程中被售出  begin
			//查询锁票时间
			Wrapper<Config> lt = new EntityWrapper<Config>();
			lt.where("org_id={0} and code={1}", loginUser.getOrgId(),SysConfigConstant.SEAT_LOCK_EXPIRE);
			Config config = configService.selectOne(lt);
			String lockTime = config.getValue(); //锁票时长(分钟)
			long expire = Long.parseLong(lockTime) * 60 * 1000; //转化为毫秒 
			
			//待选班次可选座位
			for (String candidateBusId : candidateBusIds) {
				Wrapper<ScheduleBusSeats> bmos = new EntityWrapper<ScheduleBusSeats>();
				bmos.where("schedule_bus_id={0} and seat_status={1}", candidateBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
				candidateSeatList.addAll(scheduleBusSeatsService.selectList(bmos));
			}
			//从属班次可选座位
			Wrapper<ScheduleBusSeats> mos = new EntityWrapper<ScheduleBusSeats>();
			mos.where("schedule_bus_id={0} and seat_status={1}", belongBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
			belongSeatList = scheduleBusSeatsService.selectList(mos);
			for (ScheduleBusSeats scheduleBusSeats : candidateSeatList) {
				lock.lock(scheduleBusSeats.getId(), loginUser.getId(), expire);
			}
			for (ScheduleBusSeats scheduleBusSeats : belongSeatList) {
				lock.lock(scheduleBusSeats.getId(), loginUser.getId(), expire);
			}
			//锁定被并班次以及并入班次可选座位，防止在并班过程中被售出  end
		
			//1配载班次已售座位数不能大于从属班次可选座位数
			//查询候选班次已售座位数
			Wrapper<ScheduleBusSeats> bmt = null;
			int saledSeatsNum = 0; //已售座位数
			for (String canBusId : candidateBusIds) {
				bmt = new EntityWrapper<ScheduleBusSeats>();
				bmt.where("schedule_bus_id={0} and seat_status={1}", canBusId, ScheduleBusSeatStatusEnum.SALED.getType());
				saledSeatsNum += scheduleBusSeatsService.selectCount(bmt);
			}
			
			//查询从属班次剩余座位数
			Wrapper<ScheduleBusSeats> mt = new EntityWrapper<ScheduleBusSeats>();
			mt.where("schedule_bus_id={0} and seat_status={1}", belongBusId, ScheduleBusSeatStatusEnum.OPTIONAL.getType());
			int mCount = scheduleBusSeatsService.selectCount(mt);
			checkArgument(saledSeatsNum<=mCount, "待选班次已售座位数之和不能大于从属班次可选座位数，配载失败！");
			
			//2执行配载
			stowageFlag = scheduleBusService.stowageBus(req);
		}finally {
			//释放锁
			for (ScheduleBusSeats scheduleBusSeats : candidateSeatList) {
				lock.unlock(scheduleBusSeats.getId(), loginUser.getId());
			}
			for (ScheduleBusSeats scheduleBusSeats : belongSeatList) {
				lock.unlock(scheduleBusSeats.getId(), loginUser.getId());
			}
		}
		if(stowageFlag) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "配载失败!");
		}
	}
}
