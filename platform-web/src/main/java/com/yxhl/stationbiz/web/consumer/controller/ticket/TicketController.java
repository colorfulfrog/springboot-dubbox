package com.yxhl.stationbiz.web.consumer.controller.ticket;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.finance.TicketSellerBill;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.entity.ticket.Order;
import com.yxhl.stationbiz.system.domain.entity.ticket.Ticket;
import com.yxhl.stationbiz.system.domain.request.TicketOrderRequest;
import com.yxhl.stationbiz.system.domain.response.TicketOrderResponse;
import com.yxhl.stationbiz.system.domain.service.ticket.OrderService;
import com.yxhl.stationbiz.system.domain.service.ticket.TicketService;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  售票表控制器
 *  创建人: lw
 *  创建日期:2018-9-14 10:43:42
 */
@RestController
@Api(tags = "售票表")
@RequestMapping(value = "/m")
public class TicketController extends BaseController{
	
    @Autowired
    private TicketService ticketService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
    @ApiOperation("财务模块/售票分页查询")
	@PostMapping(value = "/spPage", produces = "application/json;charset=UTF-8")
	public CommonResponse spPage(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		if(loginUser!=null) {
			ticket.setOrgId(loginUser.getOrgId());
			ticket.setCompId(loginUser.getCompanyId());
		}
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.selPageList(page, ticket);
		resp.setData(result);
		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.extendsRes("seatCategory", seatCategory);
		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); //票状态  
		resp.extendsRes("ticketStatus", ticketStatus);
		List<Dictionary> sellType = dictionaryService.getRediesByKey("sell_type"); //售票类型  
		resp.extendsRes("sellType", sellType);
		return resp;
	}
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/ticket/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.selPageList(page, ticket);
		resp.setData(result);
		/*List<Dictionary> loopTypeList = dictionaryService.getRediesByKey("loop_typeint"); //查询循环类型字典
		resp.extendsRes("loopType", loopTypeList);*/
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询售票表", notes = "根据条件查询售票表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Ticket ticket) {
		Wrapper<Ticket> wrapper = new EntityWrapper<Ticket>();
		if(getLoginUser().getOrgId() !=null) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		if(getLoginUser().getCompanyId()!=null) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		if(ticket.getSellerUserId()!=null) {
			wrapper.eq("seller_user_id", ticket.getSellerUserId());
		}
		if(StringUtils.isNotBlank(ticket.getIdCardNo())) {
			wrapper.eq("id_card_no", ticket.getIdCardNo());
		}
		
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<Ticket> list = ticketService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增售票表", notes = "新增售票表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/ticket", produces = "application/json;charset=UTF-8")
	public CommonResponse addTicket(@RequestBody Ticket ticket) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticket.setOrgId(loginUser.getOrgId());
			ticket.setCompId(loginUser.getCompanyId());
			ticket.setCreateBy(loginUser.getId());
			ticket.setUpdateBy(loginUser.getId());
		}
		ticket.setId(IdWorker.get32UUID());
		boolean isAdded = ticketService.insert(ticket);
		if(isAdded) {
			return CommonResponse.createCommonResponse(ticket);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改售票表", notes = "修改售票表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticket/{ticketId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateTicket(@PathVariable("ticketId") String ticketId,@RequestBody Ticket ticket) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticket.setUpdateBy(loginUser.getId());
		}
		ticket.setId(ticketId);
		boolean isUpdated = ticketService.updateById(ticket);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除售票表", notes = "删除售票表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/ticket/{ticketIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("ticketIds") List<String> ticketIds) {
		boolean isDel = ticketService.deleteBatchIds(ticketIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看售票表详情", notes = "查看售票表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticket/{ticketId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("ticketId") String ticketId) {
		Ticket ticket = ticketService.selectById(ticketId);
		return CommonResponse.createCommonResponse(ticket);
	}
	
    @ApiOperation("保险票查询")
	@PostMapping(value = "/spPageForTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse spPageForTicket(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
    	CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		ticket.setOrgId(loginUser.getOrgId());
		ticket.setCompId(loginUser.getCompanyId());
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.selTicketPageList(page, ticket);
		resp.setData(result);
		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); 
		resp.extendsRes("ticket_status", ticketStatus);
/*		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.extendsRes("seatCategory", seatCategory);*/
		return resp;
	}
    
	@ApiOperation("售票模块/废票分页查询")
	@PostMapping(value = "/fpPage", produces = "application/json;charset=UTF-8")
	public CommonResponse fpPage(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
		CommonResponse resp = CommonResponse.createCommonResponse();
    	ELUser loginUser = getLoginUser();
    	if(loginUser!=null) {
			ticket.setOrgId(loginUser.getOrgId());
			ticket.setCompId(loginUser.getCompanyId());
		}
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.fpPageList(page, ticket);
		resp.setData(result);
		List<Dictionary> outPayType = dictionaryService.getRediesByKey("out_pay_type"); //支付方式
		resp.extendsRes("outPayType", outPayType);
		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); //票状态  
		resp.extendsRes("ticketStatus", ticketStatus);
		List<Dictionary> sellerType = dictionaryService.getRediesByKey("seller_type"); //售票方式 
		resp.extendsRes("sellerType", sellerType);
		List<Dictionary> runStatus = dictionaryService.getRediesByKey("run_status"); //发班状态
		resp.extendsRes("runStatus", runStatus);
		return resp;
	}
    
    @ApiOperation("退保查询")
	@PostMapping(value = "/tbPageForTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse tbPageForTicket(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		ticket.setOrgId(loginUser.getOrgId());
		ticket.setCompId(loginUser.getCompanyId());
		ticket.setInsuranceStatus("3");
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.selTicketPageList(page, ticket);
		resp.setData(result);
		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); 
		resp.extendsRes("ticket_status", ticketStatus);
/*		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.extendsRes("seatCategory", seatCategory);*/
		return resp;
	}
    
    @ApiOperation("费保查询")
	@PostMapping(value = "/fbPageForTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse fbPageForTicket(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		ticket.setOrgId(loginUser.getOrgId());
		ticket.setCompId(loginUser.getCompanyId());
		ticket.setInsuranceStatus("2");
		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Ticket> result = ticketService.selTicketPageList(page, ticket);
		resp.setData(result);
		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); 
		resp.extendsRes("ticket_status", ticketStatus);
/*		//查询座位类型字典
		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
		resp.extendsRes("seatCategory", seatCategory);*/
		return resp;
	}
    
    @ApiOperation("退票查询")
  	@PostMapping(value = "/tpPageForTicket", produces = "application/json;charset=UTF-8")
  	public CommonResponse tpPageForTicket(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Ticket ticket) throws NumberFormatException, Exception {
  		CommonResponse resp = CommonResponse.createCommonResponse();
  		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
  			currentPage="1";
              pageSize="10";
          }
  		ELUser loginUser = getLoginUser();
  		ticket.setOrgId(loginUser.getOrgId());
  		ticket.setCompId(loginUser.getCompanyId());
  		Page<Ticket> page = new Page<Ticket>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
  		Page<Ticket> result = ticketService.selRefundPageList(page, ticket);
  		resp.setData(result);
  		List<Dictionary> ticketStatus = dictionaryService.getRediesByKey("ticket_status"); 
		resp.extendsRes("ticket_status", ticketStatus);
		List<Dictionary> runStatus = dictionaryService.getRediesByKey("run_status"); 
		resp.extendsRes("run_status", runStatus);
  /*		//查询座位类型字典
  		List<Dictionary> seatCategory = dictionaryService.getRediesByKey("seat_category"); 
  		resp.extendsRes("seatCategory", seatCategory);*/
  		return resp;
  	}
		
	
	@ApiOperation(value = "废票", notes = "废票", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/scrapTicket/{ticketId}", produces = "application/json;charset=UTF-8")
	public CommonResponse scrapTicket(@PathVariable("ticketId") String ticketId,@RequestBody Ticket ticket) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticket.setUpdateBy(loginUser.getId());
		}
		ticket.setId(ticketId);
		boolean isUpdated = ticketService.scrapTicket(ticket);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "废票失败!");
		}
	}
	
	@ApiOperation(value = "退票/退保险详情", notes = "退票/退保险详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/retreatTicketInfo", produces = "application/json;charset=UTF-8")
	public CommonResponse retreatTicketInfo(@RequestBody Ticket ticket) throws NumberFormatException, Exception {
		ELUser loginUser = getLoginUser();
  		ticket.setOrgId(loginUser.getOrgId());
  		ticket.setCompId(loginUser.getCompanyId());
  		List<Ticket> ticketList = ticketService.retreatTicketInfo(ticket);
		return CommonResponse.createCommonResponse(ticketList);
	}
	
	@ApiOperation(value = "退票", notes = "退票", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/retreatTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse retreatTicket(@RequestBody Ticket ticket) throws NumberFormatException, Exception {
		ticket.setRefundUserId(getLoginUser().getId());
  		boolean isUpdated = ticketService.retreatTicket(ticket);
  		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "退票失败!");
		}
	}
	
	@ApiOperation(value = "车票打印", notes = "车票打印", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/printing/{orderId}", produces = "application/json;charset=UTF-8")
	public CommonResponse printing(@PathVariable("orderId") String orderId) {
		List<Ticket> getcpdyList = ticketService.getcpdyList(orderId);
		return CommonResponse.createCommonResponse(getcpdyList);
	}
	
	@ApiOperation(value = "窗口售票F3/补票F9-预估订单费用", notes = "窗口售票F3/补票F9-预估订单费用", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/orderCost", produces = "application/json;charset=UTF-8")
	public CommonResponse orderCost(@RequestBody TicketOrderRequest ticketOrderRequest) {
		ELUser loginUser = getLoginUser();
		JSONObject j = orderService.getOrderCost(ticketOrderRequest,loginUser);
		if(j!=null)
			return CommonResponse.createCommonResponse(j);
		return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "预估订单费用失败");
	}

	@ApiOperation(value = "窗口售票F3-下单", notes = "窗口售票F3-下单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/placeOrder", produces = "application/json;charset=UTF-8")
	public CommonResponse placeOrder(@RequestBody TicketOrderRequest ticketOrderRequest) throws Exception {
		ELUser loginUser = getLoginUser();
		//校验基础数据
		ScheduleBus getScheduleBus =orderService.checkPlaceOrder(ticketOrderRequest,loginUser,"1");
		if(getScheduleBus!=null){
			String orderId = orderService.addPlaceOrder(ticketOrderRequest,loginUser,getScheduleBus);
			return CommonResponse.createCommonResponse(orderId);
		}
		return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "下单失败");
	}
	
	@ApiOperation(value = "补票F9-下单", notes = "补票F9-下单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/afterPlaceOrder", produces = "application/json;charset=UTF-8")
	public CommonResponse afterPlaceOrder(@RequestBody TicketOrderRequest	ticketOrderRequest) throws Exception {
		ELUser loginUser = getLoginUser();
		//校验基础数据
		ScheduleBus getScheduleBus =orderService.checkPlaceOrder(ticketOrderRequest,loginUser,"2");
		if(getScheduleBus!=null){
			String orderId = orderService.addPlaceOrder(ticketOrderRequest,loginUser,getScheduleBus);
			return CommonResponse.createCommonResponse(orderId);
		}
		return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "补票失败");
	}

	@ApiOperation(value = "改签F9-旧票查询", notes = "改签F9-旧票查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/oldTicket", produces = "application/json;charset=UTF-8")
	public CommonResponse oldTicket(@RequestBody TicketOrderRequest	ticketOrderRequest) throws Exception {
		ELUser loginUser = getLoginUser();
		if(StringUtils.isBlank(ticketOrderRequest.getInvoiceNo()))
			checkArgument(false, "操作失败，改签票号获取失败");
		TicketOrderResponse order =orderService.getOrderAndTicket(ticketOrderRequest.getInvoiceNo());
		if(order!=null)
			return CommonResponse.createCommonResponse(order);
		return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "旧票没有查到");
	}
	
	@ApiOperation(value = "改签F9-下单", notes = "改签F9-下单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/changePlaceOrder", produces = "application/json;charset=UTF-8")
	public CommonResponse changePlaceOrder(@RequestBody TicketOrderRequest ticketOrderRequest) throws Exception {
		ELUser loginUser = getLoginUser();
		// 校验基础数据
		ScheduleBus getScheduleBus = orderService.checkPlaceOrder(ticketOrderRequest, loginUser, "3");
		if(getScheduleBus!=null){
			String orderId = orderService.resetAddPlaceOrder(ticketOrderRequest,loginUser,getScheduleBus);
			return CommonResponse.createCommonResponse(orderId);
		}
		return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "改签失败");
	}
}
