package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

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
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.TicketGate;
import com.yxhl.stationbiz.system.domain.service.basicinfo.TicketGateService;


/**
 *	
 *  检票口表控制器
 *  创建人: lw
 *  创建日期:2018-7-10 9:41:26
 */
@RestController
@Api(tags = "检票口表控制器")
@RequestMapping(value = "/m/ticketGate/")
public class TicketGateController extends BaseController{
	
    @Autowired
    private TicketGateService ticketGateService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody TicketGate ticketGate) {
		ELUser loginUser = getLoginUser();
		ticketGate.setOrgId(loginUser.getOrgId());
		ticketGate.setCompId(loginUser.getCompanyId());
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<TicketGate> page = new Page<TicketGate>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<TicketGate> result = ticketGateService.selPageList(page, ticketGate);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有检票口表", notes = "查询所有检票口表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody TicketGate ticketGate) {
		ELUser loginUser = getLoginUser();
		Wrapper<TicketGate> wrapper = new EntityWrapper<TicketGate>();
		if(StringUtils.isNoneBlank(ticketGate.getStationId())) {
			wrapper.where("station_id={0}", ticketGate.getStationId());
		}
		if(StringUtils.isNoneBlank(ticketGate.getGateName())) {
			wrapper.like("gate_name", ticketGate.getGateName());
		}
		if(StringUtils.isNoneBlank(loginUser.getOrgId())) {
			wrapper.and("org_id={0}", loginUser.getOrgId());
		}
		if(StringUtils.isNoneBlank(loginUser.getCompanyId())) {
			wrapper.and("comp_id={0}", loginUser.getCompanyId());
		}
		List<TicketGate> list = ticketGateService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增检票口表", notes = "新增检票口表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addTicketGate(@RequestBody TicketGate ticketGate) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketGate.setOrgId(loginUser.getOrgId());
			ticketGate.setCompId(loginUser.getCompanyId());
			ticketGate.setCreateBy(loginUser.getId());
			ticketGate.setUpdateBy(loginUser.getId());
		}
		ticketGate.setId(IdWorker.get32UUID());
		boolean isAdded = ticketGateService.insert(ticketGate);
		if(isAdded) {
			return CommonResponse.createCommonResponse(ticketGate);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改检票口表", notes = "修改检票口表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/{ticketGateId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateTicketGate(@PathVariable("ticketGateId") String ticketGateId,@RequestBody TicketGate ticketGate) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketGate.setUpdateBy(loginUser.getId());
		}
		ticketGate.setId(ticketGateId);
		boolean isUpdated = ticketGateService.updateById(ticketGate);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除检票口表", notes = "删除检票口表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{ticketGateIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("ticketGateIds") List<String> ticketGateIds) {
		boolean isDel = ticketGateService.deleteBatchIds(ticketGateIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看检票口表详情", notes = "查看检票口表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{ticketGateId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("ticketGateId") String ticketGateId) {
		TicketGate ticketGate = ticketGateService.selectById(ticketGateId);
		return CommonResponse.createCommonResponse(ticketGate);
	}
}
