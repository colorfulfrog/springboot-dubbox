package com.yxhl.stationbiz.web.consumer.controller.schedule;

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
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCateValue;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCateValueService;


/**
 *	
 *  票种取值表控制器
 *  创建人: ypf
 *  创建日期:2018-8-15 14:12:46
 */
@RestController
@Api(tags = "票种取值表")
@RequestMapping(value = "/m")
public class TicketCateValueController extends BaseController{
	
    @Autowired
    private TicketCateValueService ticketCateValueService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/ticketCateValue/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody TicketCateValue ticketCateValue) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<TicketCateValue> page = new Page<TicketCateValue>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<TicketCateValue> result = ticketCateValueService.selPageList(page, ticketCateValue);
		resp.setData(result);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询票种取值表", notes = "根据条件查询票种取值表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketCateValues", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<TicketCateValue> wrapper = new EntityWrapper<TicketCateValue>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<TicketCateValue> list = ticketCateValueService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增票种取值表", notes = "新增票种取值表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/ticketCateValue", produces = "application/json;charset=UTF-8")
	public CommonResponse addTicketCateValue(@RequestBody TicketCateValue ticketCateValue) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketCateValue.setOrgId(loginUser.getOrgId());
			ticketCateValue.setCompId(loginUser.getCompanyId());
			ticketCateValue.setCreateBy(loginUser.getId());
			ticketCateValue.setUpdateBy(loginUser.getId());
		}
		ticketCateValue.setId(IdWorker.get32UUID());
		boolean isAdded = ticketCateValueService.insert(ticketCateValue);
		if(isAdded) {
			return CommonResponse.createCommonResponse(ticketCateValue);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改票种取值表", notes = "修改票种取值表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/ticketCateValue/{ticketCateValueId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateTicketCateValue(@PathVariable("ticketCateValueId") String ticketCateValueId,@RequestBody TicketCateValue ticketCateValue) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketCateValue.setUpdateBy(loginUser.getId());
		}
		ticketCateValue.setId(ticketCateValueId);
		boolean isUpdated = ticketCateValueService.updateById(ticketCateValue);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除票种取值表", notes = "删除票种取值表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/ticketCateValue/{ticketCateValueIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("ticketCateValueIds") List<String> ticketCateValueIds) {
		boolean isDel = ticketCateValueService.deleteBatchIds(ticketCateValueIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看票种取值表详情", notes = "查看票种取值表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/ticketCateValue/{ticketCateValueId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("ticketCateValueId") String ticketCateValueId) {
		TicketCateValue ticketCateValue = ticketCateValueService.selectById(ticketCateValueId);
		return CommonResponse.createCommonResponse(ticketCateValue);
	}
}
