package com.yxhl.stationbiz.web.consumer.controller.check;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.poi.util.ExportExcel;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.request.ScheduleDepartRequest;
import com.yxhl.stationbiz.system.domain.request.SettlementStatisticRequest;
import com.yxhl.stationbiz.system.domain.request.TicketCheckRequest;
import com.yxhl.stationbiz.system.domain.response.SettlementStatisticResponse;
import com.yxhl.stationbiz.system.domain.response.TicketCategoryCountResponse;
import com.yxhl.stationbiz.system.domain.response.TicketCheckSuccessResponse;
import com.yxhl.stationbiz.system.domain.response.TicketCheckVehicleResponse;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;
import com.yxhl.stationbiz.system.domain.service.finance.TicketSellerBillService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.ticket.OrderService;
import com.yxhl.stationbiz.system.domain.service.ticket.TicketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  检票控制器
 *  创建人: lw
 *  创建日期:2018-9-17 03:52:00
 */
@RestController
@Api(tags = "班次检票")
@RequestMapping(value = "/m")
public class TicketCheckController extends BaseController{
	
    @Autowired
    private TicketService ticketService;
    //@Autowired
	//private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ScheduleBusService scheduleBusService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TicketSellerBillService ticketSellerBillService;
    
	@ApiOperation(value="检票班次分页查询",response=ScheduleBus.class)
	@PostMapping(value = "/ticketCheck/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody TicketCheckRequest req) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleBus> page = new Page<ScheduleBus>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBus> result = scheduleBusService.checkBusPageList(page, req);
		resp.setData(result);
		List<Dictionary> runStatus = dictionaryService.getRediesByKey("run_status"); //查询发班状态字典
		List<Dictionary> busType = dictionaryService.getRediesByKey("bus_type"); //查询班次类型字典
		resp.extendsRes("runStatus", runStatus);
		resp.extendsRes("busType", busType);
		return resp;
	}
	
	@ApiOperation(value = "查询班次报班车辆信息", notes = "查询班次报班车辆信息", response=TicketCheckVehicleResponse.class, httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketCheck/reportCar", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("scheduleBusId") String scheduleBusId) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		TicketCheckVehicleResponse data = new TicketCheckVehicleResponse();
		ScheduleBus scheduleBus = scheduleBusService.selectById(scheduleBusId);
		
		Wrapper<Vehicle> wrapper = new EntityWrapper<Vehicle>();
		wrapper.where("car_no={0}", scheduleBus.getReportCarNo());
		Vehicle vehicle = vehicleService.selectOne(wrapper);
		checkNotNull(vehicle, "该班次还没有报班车辆");
		
		String seatCategoryName = "";
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); //查询发班状态字典
		for (Dictionary dictionary : seatCategory) {
			if(dictionary.getValue().equals(String.valueOf(vehicle.getSeatCategory()))) {
				seatCategoryName = dictionary.getKeyName();
			}
		}
		
		data.setReportCarNo(scheduleBus.getReportCarNo());
		data.setApprovedSeats(vehicle.getApprovedSeats());
		data.setReportTime(scheduleBus.getReportTime());
		data.setRunTime(scheduleBus.getRunTime());
		data.setSeatCategoryName(seatCategoryName);
		data.setBrandModel(vehicle.getBrandModel());
		
		resp.setData(data);
		return resp;
	}
	
	@ApiOperation(value = "全检", notes = "全检", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/checkall", produces = "application/json;charset=UTF-8")
	public CommonResponse checkAll(@RequestParam("scheduleBusId") String scheduleBusId) {
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("schedule_bus_id={0}", scheduleBusId);
		List<Ticket> tickets = ticketService.selectList(wrapper);
		for (Ticket ticket : tickets) {
			ticket.setCheckStatus(1);
			ticket.setCheckScheduleBusId(scheduleBusId);
		}
		checkArgument(CollectionUtils.isNotEmpty(tickets), "该班次未售票，不能检票");
		boolean updated = ticketService.updateBatchById(tickets);
		if(updated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "全检失败!");
		}
	}
	
	@ApiOperation(value = "退检", notes = "退检", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/refundcheck", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBus(@RequestParam(value="scheduleBusId",required=true) String scheduleBusId,
			@RequestParam(value="invoiceNo",required=false) String invoiceNo,
			@RequestParam(value="idCardNo",required=false) String idCardNo) {
		ELUser loginUser = getLoginUser();
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("schedule_bus_id={0}", scheduleBusId);
		if(StringUtils.isNotBlank(invoiceNo)) {
			wrapper.and("invoice_no={0}", invoiceNo); //票号
		}
		if(StringUtils.isNotBlank(idCardNo)) {
			wrapper.and("id_card_no={0}", idCardNo); //身份证
		}
		Ticket ticket = ticketService.selectOne(wrapper);
		
		boolean refunded = false;
		if(ticket != null) {
			refunded = ticketService.refundCheck(ticket,loginUser.getId()); //退检
			if(refunded) {
				return CommonResponse.createCommonResponse();
			}else {
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "退检失败!");
			}
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "未找到车票信息，退检失败");
		}
	}
	
	@ApiOperation(value = "混检", notes = "混检",response=TicketCheckSuccessResponse.class, httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/mixedcheck", produces = "application/json;charset=UTF-8")
	public CommonResponse checkAll(@RequestParam(value="scheduleBusId",required=true) String scheduleBusId,
			@RequestParam(value="invoiceNo",required=false) String invoiceNo,
			@RequestParam(value="idCardNo",required=false) String idCardNo) {
		ELUser loginUser = getLoginUser();
		//查询车票
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("schedule_bus_id={0}", scheduleBusId);
		if(StringUtils.isNotBlank(invoiceNo)) {
			wrapper.and("invoice_no={0}", invoiceNo); //票号
		}
		if(StringUtils.isNotBlank(idCardNo)) {
			wrapper.and("id_card_no={0}", idCardNo); //身份证
		}
		Ticket ticket = ticketService.selectOne(wrapper);
		
		boolean checked = false;
		if(ticket != null) {
			//校验：重复检票
			checkArgument(ticket.getCheckStatus()!=1, "重复检票");
			//校验：废票、退票
			checkArgument(!(ticket.getStatus().equals("2")), "废票,检票失败");
			checkArgument(!(ticket.getStatus().equals("3")), "退票,检票失败");
			//校验：已发班车次不能检票
			ScheduleBus scheduleBus = scheduleBusService.selectById(scheduleBusId);
			checkArgument(!(scheduleBus.getRunStatus()==1||scheduleBus.getRunStatus()==2), "已发班车次不能检票,检票失败");
			
			checked = ticketService.mixCheck(scheduleBusId,ticket,loginUser.getId()); //检票
			if(checked) {
				String orderId = ticket.getOrderId();
				Order order = orderService.selectById(orderId);
				TicketCheckSuccessResponse result = new TicketCheckSuccessResponse();
				result.setBusCode(scheduleBus.getBusCode());
				result.setRunDate(scheduleBus.getRunDate());
				result.setRunTime(scheduleBus.getRunTime());
				result.setStart(order.getStart());
				result.setDestination(order.getDestination());
				result.setPassengerName(ticket.getName());
				result.setSex(ticket.getSex());
				result.setIdCardNo(ticket.getIdCardNo());
				return CommonResponse.createCommonResponse(result);
			}else {
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "混检失败!");
			}
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "未找到车票信息，检票失败");
		}
	}
	
	@ApiOperation(value = "检票", notes = "检票", response=TicketCheckSuccessResponse.class, httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/single", produces = "application/json;charset=UTF-8")
	public CommonResponse checkSingle(@RequestParam(value="scheduleBusId",required=true) String scheduleBusId,
			@RequestParam(value="invoiceNo",required=false) String invoiceNo,
			@RequestParam(value="idCardNo",required=false) String idCardNo) {
		ELUser loginUser = getLoginUser();
		//查询车票
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		wrapper.where("schedule_bus_id={0}", scheduleBusId);
		if(StringUtils.isNotBlank(invoiceNo)) {
			wrapper.and("invoice_no={0}", invoiceNo); //票号
		}
		if(StringUtils.isNotBlank(idCardNo)) {
			wrapper.and("id_card_no={0}", idCardNo); //身份证
		}
		Ticket ticket = ticketService.selectOne(wrapper);
		
		boolean checked = false;
		if(ticket != null) {
			//校验：重复检票
			checkArgument(ticket.getCheckStatus()!=1, "重复检票");
			//校验：废票、退票
			checkArgument(!(ticket.getStatus().equals("2")), "废票,检票失败");
			checkArgument(!(ticket.getStatus().equals("3")), "退票,检票失败");
			//校验：已发班车次不能检票
			ScheduleBus scheduleBus = scheduleBusService.selectById(scheduleBusId);
			checkArgument(!(scheduleBus.getRunStatus()==1||scheduleBus.getRunStatus()==2), "已发班车次不能检票,检票失败");
			
			checked = ticketService.checkSingle(ticket,loginUser.getId()); //检票
			if(checked) {
				String orderId = ticket.getOrderId();
				Order order = orderService.selectById(orderId);
				TicketCheckSuccessResponse result = new TicketCheckSuccessResponse();
				result.setBusCode(scheduleBus.getBusCode());
				result.setRunDate(scheduleBus.getRunDate());
				result.setRunTime(scheduleBus.getRunTime());
				result.setStart(order.getStart());
				result.setDestination(order.getDestination());
				result.setPassengerName(ticket.getName());
				result.setSex(ticket.getSex());
				result.setIdCardNo(ticket.getIdCardNo());
				return CommonResponse.createCommonResponse(result);
			}else {
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "检票失败!");
			}
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "未找到车票信息，检票失败");
		}
	}
	
	@ApiOperation(value = "发班", notes = "发班", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/depart", produces = "application/json;charset=UTF-8")
	public CommonResponse depart(@RequestBody ScheduleDepartRequest req) {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketSellerBill> wrapper = new EntityWrapper<TicketSellerBill>();
		wrapper.where("receive_user={0} and bill_type='4' and status='1'", loginUser.getId()); //查询启用的三联单
		TicketSellerBill bill = ticketSellerBillService.selectOne(wrapper);
		checkNotNull(bill, "您没有可以使用的三联单号，请确认是否领用且启用号段！");
		
		//校验输入的三联单号的合法性
		BigInteger input = new BigInteger(req.getTriplicateBillNum());
		BigInteger startNum = new BigInteger(bill.getStartNum());
		BigInteger endNum = new BigInteger(bill.getEndNum());
		if(StringUtils.isBlank(bill.getCurrentNum())) { //第一次设置单号，开始号 <= input <=结束号
			checkArgument(!(input.compareTo(startNum)<0||input.compareTo(endNum)>0), "单号设置不合法，必须在领用的单号范围内！");
		}else { //修改： 当前 <= input <=结束号
			BigInteger currentNum = new BigInteger(bill.getCurrentNum());
			checkArgument(!(input.compareTo(currentNum)<0||input.compareTo(endNum)>0), "单号设置不合法，必须在领用的单号范围内,且不能是已经使用过的单号！");
		}
		
		ScheduleBus scheduleBus = scheduleBusService.selectById(req.getScheduleBusId());
		scheduleBus.setUpdateBy(loginUser.getId());
		boolean departed = scheduleBusService.depart(req,loginUser.getId());
		if(departed) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "取消发班失败!");
		}
	}
	
	@ApiOperation(value = "取消发班", notes = "取消发班", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/cancleDepart", produces = "application/json;charset=UTF-8")
	public CommonResponse cancleDepart(@RequestParam("scheduleBusId") String scheduleBusId) {
		ELUser loginUser = getLoginUser();
		ScheduleBus scheduleBus = scheduleBusService.selectById(scheduleBusId);
		scheduleBus.setUpdateBy(loginUser.getId());
		boolean updated = scheduleBusService.cancleDepart(scheduleBus);
		if(updated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "取消发班失败!");
		}
	}
	
	@ApiOperation(value = "查询检票员当前三联单号", notes = "查询检票员当前三联单号",response=TicketSellerBill.class, httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketCheck/getTriplicateBillNum", produces = "application/json;charset=UTF-8")
	public CommonResponse getTriplicateBillNum() {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketSellerBill> wrapper = new EntityWrapper<TicketSellerBill>();
		wrapper.where("receive_user={0} and bill_type='4' and status='1'", loginUser.getId()); //查询启用的三联单
		TicketSellerBill bill = ticketSellerBillService.selectOne(wrapper);
		return CommonResponse.createCommonResponse(bill);
	}
	
	@ApiOperation(value = "重置三联单号", notes = "重置三联单号", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCheck/resetTriplicateBillNum", produces = "application/json;charset=UTF-8")
	public CommonResponse resetTriplicateBillNum(@RequestParam("triplicateBillNum") String triplicateBillNum) {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketSellerBill> wrapper = new EntityWrapper<TicketSellerBill>();
		wrapper.where("receive_user={0} and bill_type='4' and status='1'", loginUser.getId()); //查询启用的三联单
		TicketSellerBill bill = ticketSellerBillService.selectOne(wrapper);
		checkNotNull(bill, "您没有可以使用的三联单号，请确认是否领用且启用号段！");
		
		//校验输入的三联单号的合法性
		BigInteger input = new BigInteger(triplicateBillNum);
		BigInteger startNum = new BigInteger(bill.getStartNum());
		BigInteger endNum = new BigInteger(bill.getEndNum());
		if(StringUtils.isBlank(bill.getCurrentNum())) { //第一次设置单号，开始号 <= input <=结束号
			checkArgument(!(input.compareTo(startNum)<0||input.compareTo(endNum)>0), "单号设置不合法，必须在领用的单号范围内！");
		}else { //修改： 当前 <= input <=结束号
			BigInteger currentNum = new BigInteger(bill.getCurrentNum());
			checkArgument(!(input.compareTo(currentNum)<0||input.compareTo(endNum)>0), "单号设置不合法，必须在领用的单号范围内,且不能是已经使用过的单号！");
		}
		
		BigInteger remainBI = new BigInteger(bill.getEndNum()).subtract(new BigInteger(triplicateBillNum)).add(new BigInteger("1"));
		bill.setCurrentNum(triplicateBillNum);
		bill.setRemainCount(Integer.valueOf(remainBI.toString()));
		boolean updated = ticketSellerBillService.updateById(bill);
		if(updated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "重置三联单号失败!");
		}
	}
	
	@ApiOperation(value="结算单管理分页查询",response=SettlementStatisticResponse.class)
	@PostMapping(value = "/ticketCheck/settlementPage", produces = "application/json;charset=UTF-8")
	public CommonResponse settlementStatistic(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody SettlementStatisticRequest req) {
		ELUser loginUser = getLoginUser();
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		req.setOrgId(loginUser.getOrgId());
		req.setCompId(loginUser.getCompanyId());
		Page<SettlementStatisticResponse> page = new Page<SettlementStatisticResponse>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<SettlementStatisticResponse> selPageList = scheduleBusService.selSettlementStatisticPageList(page, req);
		resp.setData(selPageList);
		return resp;
	}
	
	/**
	 * http://localhost:8187/platform-web/m/ticketCheck/exportSettlement?stationId=&queryType=1&date=&month=&startDate=&endDate=reportCarNo=
	 * @return
	 */
	@ApiOperation("导出结算单")
	@GetMapping(value = "/ticketCheck/exportSettlement", produces = "application/json;charset=UTF-8")
	public void exportSettlement(HttpServletResponse response,
			@RequestParam(value = "stationId", required = false) String stationId,
			@RequestParam(value = "queryType", required = true) String queryType,
			@RequestParam(value = "date", required = false) String date,
			@RequestParam(value = "month", required = false) String month,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate,
			@RequestParam(value = "reportCarNo", required = false) String reportCarNo) {
		ELUser loginUser = getLoginUser();
		SettlementStatisticRequest req = new SettlementStatisticRequest();
		req.setOrgId(loginUser.getOrgId());
		req.setCompId(loginUser.getCompanyId());
		req.setStationId(stationId);
		req.setQueryType(queryType);
		req.setDate(date);
		req.setMonth(month);
		req.setStartDate(startDate);
		req.setEndDate(endDate);
		req.setReportCarNo(reportCarNo);
		List<SettlementStatisticResponse> list = scheduleBusService.expSettlementStatisticList(req);
		checkNotNull(list, "导出列表无数据");
		
		//设置表头
		SettlementStatisticResponse settlementStatisticResponse = list.get(0);
		List<TicketCategoryCountResponse> tcCountList = settlementStatisticResponse.getTcCountList();
		List<String> headers = Lists.newArrayList("站点名称","三联单号","发车日期","班次号","发车时间","车牌号");
		for (TicketCategoryCountResponse tc : tcCountList) {
			headers.add(tc.getTicketCateName());
		}
		headers.addAll(Lists.newArrayList("合计人数","总金额","客运结算金额","售票金额","站务费","开单员","出站检查员","稽查员","稽查补缴款","补缴人数"));
		
		//数据
		List<LinkedHashMap<String, Object>> dataList = new ArrayList<LinkedHashMap<String, Object>>();
 		LinkedHashMap<String, Object> map = null;
 		List<TicketCategoryCountResponse> tcCount = null;
		for (SettlementStatisticResponse staResp : list) {
			tcCount = staResp.getTcCountList();
			map = new LinkedHashMap<String,Object>();
			map.put("stationName", staResp.getStationName());
			map.put("triplicateBillNum", staResp.getTriplicateBillNum());
			map.put("runDate", DateHelper.formatDate(staResp.getRunDate(), "yyyy-MM-dd"));
			map.put("busCode", staResp.getBusCode());
			map.put("runTime", DateHelper.formatDate(staResp.getRunTime(), "HH:mm"));
			map.put("reportCarNo", staResp.getReportCarNo());
			for (TicketCategoryCountResponse tcc : tcCount) {
				map.put(tcc.getTicketCateName(), tcc.getSaleCount());
			}
			map.put("totalPassengerCount", staResp.getTotalPassengerCount());
			map.put("totalAmount", staResp.getTotalAmount());
			map.put("settlementAmount", staResp.getSettlementAmount());
			map.put("ticketAmount", staResp.getTicketAmount());
			map.put("stationFee", staResp.getStationFee());
			map.put("triplicateBiller", staResp.getTriplicateBiller());
			map.put("outboundChecker", staResp.getOutboundChecker());
			map.put("inspectionUser", staResp.getInspectionUser());
			map.put("paymentFee", staResp.getPaymentFee());
			map.put("paymentPeople", staResp.getPaymentPeople());
			dataList.add(map);
		}
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("结算单导出");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(20);
		
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		XSSFCell cellHeader;
		for (int i = 0; i < headers.size(); i++) {
			cellHeader = row.createCell(i);
			cellHeader.setCellStyle(ExportExcel.getHeaderStyle(workbook));
			cellHeader.setCellValue(new XSSFRichTextString(headers.get(i)));
		}
		
		ExportExcel.exportExcel("结算单导出",workbook, sheet, 1, dataList, response);
	}
}
