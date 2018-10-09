package com.yxhl.stationbiz.web.consumer.controller.schedule;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleInstation;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusReportStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusRunStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusStatusEnum;
import com.yxhl.stationbiz.system.domain.request.CreateScheduleBusRequest;
import com.yxhl.stationbiz.system.domain.response.VehicleResponse;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverVehicleService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusTplService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.security.VehicleInstationService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *	
 *  排班、报班
 *  创建人: lw
 *  创建日期:2018-7-10 16:42:40
 */
@RestController
@Api(tags = "排班、报班控制器")
@RequestMapping(value = "/m/scheduleBus/")
public class ScheduleBusController extends BaseController{
	
    @Autowired
    private ScheduleBusService scheduleBusService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private LineService lineService;
    @Autowired
    private DriverVehicleService driverVehicleService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private VehicleInstationService vehicleInstationService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private ScheduleBusTplService scheduleBusTplService;
    //@Autowired
    //private ScheduleBusStowageService scheduleBusStowageService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleBus scheduleBus) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleBus> page = new Page<ScheduleBus>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBus> result = scheduleBusService.selPageList(page, scheduleBus);
		resp.setData(result);
		List<Dictionary> busStatus = dictionaryService.getRediesByKey("bao_bus_status"); //查询班次状态字典
		List<Dictionary> reportStatus = dictionaryService.getRediesByKey("report_status"); //查询报班状态字典
		List<Dictionary> busType = dictionaryService.getRediesByKey("bus_type"); //查询班次类型字典
		resp.extendsRes("busStatus", busStatus);
		resp.extendsRes("reportStatus", reportStatus);
		resp.extendsRes("busType", busType);
		return resp;
	}
	
	@ApiOperation("排班分页查询")
	@PostMapping(value = "/schedule/page", produces = "application/json;charset=UTF-8")
	public CommonResponse schedulePage(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleBus scheduleBus) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleBus> page = new Page<ScheduleBus>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBus> result = scheduleBusService.selSchedulePageList(page, scheduleBus);
		resp.setData(result);
		List<Dictionary> busStatus = dictionaryService.getRediesByKey("bao_bus_status"); //查询班次状态字典
		List<Dictionary> reportStatus = dictionaryService.getRediesByKey("report_status"); //查询报班状态字典
		List<Dictionary> busType = dictionaryService.getRediesByKey("bus_type"); //查询班次类型字典
		List<Dictionary> oprCategory = dictionaryService.getRediesByKey("opr_categoryvarchar"); //查询班次运营类别字典
		List<Dictionary> oprMode = dictionaryService.getRediesByKey("opr_modevarchar"); //查询班次运营类别字典
		List<Dictionary> runStatus = dictionaryService.getRediesByKey("run_status"); //查询发班状态
		resp.extendsRes("busStatus", busStatus);
		resp.extendsRes("reportStatus", reportStatus);
		resp.extendsRes("busType", busType);
		resp.extendsRes("oprCategory", oprCategory);
		resp.extendsRes("oprMode", oprMode);
		resp.extendsRes("runStatus", runStatus);
		return resp;
	}
	
	@ApiOperation(value = "查询所有班次", notes = "查询所有班次信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("runDate") String runDate) {
		ELUser loginUser = getLoginUser();
		Wrapper<ScheduleBus> wrapper = new EntityWrapper<ScheduleBus>();
		if(StringUtils.isNoneBlank(loginUser.getOrgId())) {
			wrapper.and("org_id={0}", loginUser.getOrgId());
		}
		if(StringUtils.isNoneBlank(loginUser.getCompanyId())) {
			wrapper.and("comp_id={0}", loginUser.getCompanyId());
		}
		if(StringUtils.isNoneBlank(runDate)) {
			wrapper.and("run_date={0}", runDate);
		}
		
		List<ScheduleBus> list = scheduleBusService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增班次", notes = "新增班次信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleBus(@RequestBody ScheduleBus scheduleBus) {
		scheduleBus.setId(IdWorker.get32UUID());
		boolean isAdded = scheduleBusService.insert(scheduleBus);
		if(isAdded) {
			return CommonResponse.createCommonResponse(scheduleBus);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次", notes = "修改班次信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBus(@RequestBody ScheduleBus scheduleBus) {
		boolean isUpdated = scheduleBusService.updateById(scheduleBus);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除班次", notes = "删除班次信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{scheduleBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("scheduleBusId") String scheduleBusId) {
		boolean isDel = scheduleBusService.deleteById(scheduleBusId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看班次详情", notes = "查看班次详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{scheduleBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleBusId") String scheduleBusId) {
		ScheduleBus scheduleBus = scheduleBusService.selectById(scheduleBusId);
		return CommonResponse.createCommonResponse(scheduleBus);
	}
	
	@ApiOperation(value = "根据车辆查询车辆、线路、司机等信息", notes = "根据车牌号查询车辆、线路、司机等信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/vehicle/{vehicleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getVehicleInfo(@PathVariable("vehicleId") String vehicleId) {
		VehicleResponse resp = new VehicleResponse();
		//1查询车辆信息
		Vehicle vehicle = vehicleService.selectById(vehicleId); 
		List<Dictionary> dicList = dictionaryService.getRediesByKey("seat_category"); //查询座位类型字典
		String seatCategoryName = "";
		for (Dictionary dictionary : dicList) {
			if (dictionary.getValue().equalsIgnoreCase(String.valueOf(vehicle.getSeatCategory()))){
				seatCategoryName = dictionary.getKeyName();
				break;
			}
		}
		vehicle.setSeatCategoryName(seatCategoryName);
		resp.setVehicle(vehicle);
		
		//2查询线路信息
		// 如果车辆设置本单位所有线路，这里查询出车辆所属单位的所有线路
		List<Line> lines = Lists.newArrayList();
		if("1".equals(vehicle.getAllCompLines())) {
			String compId = vehicle.getCompId();
			Wrapper<Line> lineWrapper = new EntityWrapper<Line>();
			lineWrapper.where("comp_id={0}", compId);
			lines = lineService.selectList(lineWrapper);
		}else { // 查询车辆绑定的线路
			String lineId = vehicle.getLineId();
			Line line = lineService.selectById(lineId);
			lines.add(line);
		}
		//根据车辆进站设置，查询车辆临时线路
		Wrapper<VehicleInstation> wrapper = new EntityWrapper<VehicleInstation>();
		wrapper.where("vehicle_id={0}", vehicleId);
		wrapper.and("begin_date <= now() and end_date >= now()"); //进站许可在有效期之内
		List<VehicleInstation> inStaList = vehicleInstationService.selectList(wrapper);
		Collection<String> lineIdList = Collections2.transform(inStaList, new Function<VehicleInstation, String>(){
			@Override
			public String apply(VehicleInstation vi) {
				return vi.getLineId();
			}
		});
		if(CollectionUtils.isNotEmpty(lineIdList)) {
			Wrapper<Line> lineWrapper = new EntityWrapper<Line>();
			lineWrapper.in("id", lineIdList);
			List<Line> tempLines = lineService.selectList(lineWrapper);
			lines.addAll(tempLines);
		}
		
		resp.setLines(lines);
		
		//3根据车辆查询司机信息
		Wrapper<DriverVehicle> dvWrapper = new EntityWrapper<DriverVehicle>();
		dvWrapper.setSqlSelect("driver_id");
		dvWrapper.where("vehicle_id={0}", vehicle.getId());
		List<DriverVehicle> dvList = driverVehicleService.selectList(dvWrapper);
		Collection<String> driverIdList = Collections2.transform(dvList, new Function<DriverVehicle, String>(){
			@Override
			public String apply(DriverVehicle dv) {
				return dv.getDriverId();
			}
		});
		
		//查询司机
		List<Driver> drivers = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(driverIdList)) {
			Wrapper<Driver> driverWp = new EntityWrapper<Driver>();
			driverWp.in("id", driverIdList);
			drivers = driverService.selectList(driverWp);
		}
		resp.setDrivers(drivers);
		return CommonResponse.createCommonResponse(resp);
	}
	
	@ApiOperation(value = "司机报班", notes = "司机报班", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/driverReport", produces = "application/json;charset=UTF-8")
	public CommonResponse driverReport(@RequestBody ScheduleBus scheduleBus) {
		ELUser loginUser = getLoginUser();
		String vehicleId = scheduleBus.getVehicleId(); //车辆ID
		Vehicle vehicle = vehicleService.selectById(vehicleId);
		
		String lineId = scheduleBus.getLineId(); //线路ID
		String scheduleTplId = scheduleBus.getScheduleTplId(); //班次模板ID
		String driverId = scheduleBus.getDriverId(); // 司机ID
		
		//查找司机要报班的班次
		Wrapper<ScheduleBus> wrapper = new EntityWrapper<ScheduleBus>();
		wrapper.where("line_id={0} and schedule_tpl_id={1} and run_date={2} and report_status <> {3}", lineId,scheduleTplId,DateHelper.getTodayDate(),ScheduleBusReportStatusEnum.REPORTED.getType());
		ScheduleBus scheBuso = scheduleBusService.selectOne(wrapper);
		if(scheBuso == null) {
			throw new YxBizException("未找到报班班次");
		}
		
		//已停班的班次不能报班
		checkArgument(!(scheBuso.getBusStatus()==ScheduleBusStatusEnum.STOP.getType()), "已停班的班次不能报班!");
		
		//检查司机、车辆是否已经报了其他班次，不能同时报多个班次
		Wrapper<ScheduleBus> driverWrapper = new EntityWrapper<ScheduleBus>();
		driverWrapper.where("main_driver_id={0} AND bus_status={1} AND report_status={2} AND run_status <> {3}",
				driverId, ScheduleBusStatusEnum.NORMAL.getType(), ScheduleBusReportStatusEnum.REPORTED.getType(),
				ScheduleBusRunStatusEnum.FINISHED.getType());
		List<ScheduleBus> driverScheBusList = scheduleBusService.selectList(driverWrapper);
		
		Wrapper<ScheduleBus> carWrapper = new EntityWrapper<ScheduleBus>();
		carWrapper.where("report_car_no={0} AND bus_status={1} AND report_status={2} AND run_status <> {3}",
				vehicle.getCarNo(), ScheduleBusStatusEnum.NORMAL.getType(), ScheduleBusReportStatusEnum.REPORTED.getType(),
				ScheduleBusRunStatusEnum.FINISHED.getType());
		List<ScheduleBus> carScheBusList = scheduleBusService.selectList(carWrapper);
		if(CollectionUtils.isNotEmpty(driverScheBusList)) {
			throw new YxBizException("该司机已经报了其他班次，不能同时报多个班次");
		}
		if(CollectionUtils.isNotEmpty(carScheBusList)) {
			throw new YxBizException("该车辆已经报了其他班次，不能同时报多个班次");
		}
		
		//验证司机证件
		Driver driver = driverService.selectById(driverId);
		if(!driver.verifyCert()) {
			throw new YxBizException("司机【"+driver.getDriverName()+"】有证件过期，不能继续报班");
		}
		
		//验证车辆证件
		if(!vehicle.verifyCert()) {
			throw new YxBizException("车辆【"+vehicle.getCarNo()+"】有证件过期，不能继续报班");
		}
		ScheduleBus scheBus = new ScheduleBus();
		scheBus.setId(scheBuso.getId());
		scheBus.setReportCarNo(vehicle.getCarNo());
		scheBus.setReportStaId(loginUser.getStationId());
		//scheBus.setReportStaId("1");
		scheBus.setReportTime(new Date());
		scheBus.setReportStatus(ScheduleBusReportStatusEnum.REPORTED.getType());
		scheBus.setSeats(vehicle.getPassengerSeats());
		scheBus.setMainDriverId(driverId);
		scheBus.setUpdateBy(loginUser.getId());
		
		//添加操作日志
		logService.insertLog(OperateLogModelEnum.VEHICLEBUS.getModule(),"调度员报班",getRemoteAddr(),"线路："+scheBuso.getLineName()+",班次号："+scheBuso.getBusCode()+",发车时间："+DateHelper.formatDate(scheBuso.getRunTime(),"yyyy-MM-dd"),loginUser.getId());

		boolean isUpdated = scheduleBusService.updateById(scheBus);
		if(isUpdated) {
			return CommonResponse.createCommonResponse(scheduleBus);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "司机报班失败!");
		}
	}
	
	@ApiOperation(value = "取消报班", notes = "取消报班", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/cancelReport/{scheduleBusIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse cancelReport(@PathVariable("scheduleBusIds") List<String> scheduleBusIds) {
		ELUser loginUser = getLoginUser();
		boolean updated = false;
		List<ScheduleBus> list = Lists.newArrayList();
		for (String scheduleBusId : scheduleBusIds) {
			ScheduleBus bus = scheduleBusService.selectById(scheduleBusId);
			bus.setReportStatus(ScheduleBusReportStatusEnum.CANCLE.getType()); //已取消
			bus.setReportStaId(null);
			bus.setReportCarNo(null);
			bus.setReportTime(null);
			bus.setMainDriverId(null);
			bus.setSeats(null);
			bus.setUpdateBy(loginUser.getId());
			updated = scheduleBusService.updateAllColumnById(bus);
			list.add(bus);
		}
		
		//添加日志
		Collection<String> logList = Collections2.transform(list, new Function<ScheduleBus, String>() {
			@Override
			public String apply(ScheduleBus bus) {
				return DateHelper.formatDate(bus.getRunDate(),"yyyy-MM-dd")+"-【"+bus.getLineName()+"】-"+bus.getBusCode();
			}
		});
		logService.insertLog(OperateLogModelEnum.VEHICLEBUS.getModule(),"调度员取消报班",getRemoteAddr(),StringUtils.join(logList, ";"),loginUser.getId());
		
		if(updated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "取消报班失败!");
		}
	}
	
	@ApiOperation(value = "制作运营计划", notes = "制作运营计划", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/plan", produces = "application/json;charset=UTF-8")
	public CommonResponse createOperatePlan(@RequestBody CreateScheduleBusRequest req) {
		ELUser loginUser = getLoginUser();
		req.setCompId(loginUser.getCompanyId());
		scheduleBusService.createOperPlanTask(req);
		
		StringBuffer content = new StringBuffer();
		Line line = lineService.selectById(req.getLineId());
		List<ScheduleBusTpl> tpls = scheduleBusTplService.selectBatchIds(req.getScheduleTplList());
		Collection<String> busCodeList = Collections2.transform(tpls, new Function<ScheduleBusTpl, String>() {
			@Override
			public String apply(ScheduleBusTpl tpl) {
				return tpl.getBusCode();
			}
		});
		
		content.append("线路:【" + line.getLineName() + "】-")
			   .append("选中班次：【" + StringUtils.join(busCodeList, ";") + "】-")
			   .append("操作类型：【" + req.getOperateType() + "】-")
			   .append("操作：【" + req.getOperation() + "】-")
			   .append("制作日期：【" + DateHelper.formatDate(req.getStartDate(), "yyyy-MM-dd") + "至"+ DateHelper.formatDate(req.getEndDate(), "yyyy-MM-dd") + "】"); 
		logService.insertLog(OperateLogModelEnum.SCHEDULEBUS.getModule(),"制作运营计划",getRemoteAddr(),content.toString(),loginUser.getId());
		return CommonResponse.createCommonResponse();
	}
	
	
	@ApiOperation("售票查询  班次查询")
	@PostMapping(value = "/queryScheduleBusList", produces = "application/json;charset=UTF-8")
	public CommonResponse queryScheduleBusList(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleBus scheduleBus) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		checkNotNull(scheduleBus,"参数异常");
		checkNotNull(scheduleBus.getRunDateStr(),"发车时间不能为空");
		checkNotNull(scheduleBus.getStartStationId(),"始发站编号不能为空");
		checkNotNull(scheduleBus.getReportStaId(),"到达车站编号不能为空");
		Page<ScheduleBus> page = new Page<ScheduleBus>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBus> result = scheduleBusService.queryScheduleBusList(page, scheduleBus);
		resp.setData(result);
		List<Dictionary> busStatus = dictionaryService.getRediesByKey("bao_bus_status"); //查询班次状态字典  ( 正常、停班、被配载、被并/停班、并班、配载)
		List<Dictionary> busType = dictionaryService.getRediesByKey("bus_type"); //查询班次类型字典    ( 固定、流水)
		List<Dictionary> busTypevarchar = dictionaryService.getRediesByKey("bus_typevarchar"); //查询班次类型字典（快车、豪华、普通 、卧铺 ）
		resp.extendsRes("busStatus", busStatus);
		resp.extendsRes("busType", busType);
		resp.extendsRes("busTypevarchar", busTypevarchar);
		return resp;
	}
	
}
