package com.yxhl.stationbiz.web.consumer.controller.schedule;

import java.util.List;

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
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCategoryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  票种设置表控制器
 *  创建人: xjh
 *  创建日期:2018-8-16 10:25:40
 */
@RestController
@Api(tags = "票种设置表")
@RequestMapping(value = "/m/ticketCategory")
public class TicketCategoryController extends BaseController{
	
    @Autowired
    private TicketCategoryService ticketCategoryService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody TicketCategory ticketCategory) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ticketCategory.setOrgId(getLoginUser().getOrgId());
		ticketCategory.setCompId(getLoginUser().getCompanyId());
		Page<TicketCategory> page = new Page<TicketCategory>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<TicketCategory> result = ticketCategoryService.selPageList(page, ticketCategory);
		resp.setData(result);
		/*List<Dictionary> loopTypeList = dictionaryService.getRediesByKey("loop_typeint"); //查询循环类型字典
		resp.extendsRes("loopType", loopTypeList);*/
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询票种设置表", notes = "根据条件查询票种设置表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam(value="ticketCateName",required=false) String ticketCateName) {
		Wrapper<TicketCategory> wrapper = new EntityWrapper<TicketCategory>();
		if(StringUtils.isNotBlank(ticketCateName)) {
			wrapper.like("ticket_cate_name", ticketCateName);
		}
		if(StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		if(StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<TicketCategory> list = ticketCategoryService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增票种设置表", notes = "新增票种设置表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addTicketCategory(@RequestBody TicketCategory ticketCategory) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketCategory.setOrgId(loginUser.getOrgId());
			ticketCategory.setCompId(loginUser.getCompanyId());
			ticketCategory.setCreateBy(loginUser.getId());
			ticketCategory.setUpdateBy(loginUser.getId());
		}
		ticketCategory.setId(IdWorker.get32UUID());
		boolean isAdded = ticketCategoryService.insert(ticketCategory);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.TICKETCATEGROY.getModule(),"新增票种",getRemoteAddr(),"票种名称【"+ticketCategory.getTicketCateName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(ticketCategory);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改票种设置表", notes = "修改票种设置表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{tcId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateTicketCategory(@PathVariable("tcId") String tcId,@RequestBody TicketCategory ticketCategory) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			ticketCategory.setUpdateBy(loginUser.getId());
		}
		ticketCategory.setId(tcId);
		boolean isUpdated = ticketCategoryService.updateById(ticketCategory);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.TICKETCATEGROY.getModule(),"修改票种",getRemoteAddr(),"被修改票种名称【"+ticketCategory.getTicketCateName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除票种设置表", notes = "删除票种设置表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delect/{tcId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("tcId") List<String> tcId) {
		List<TicketCategory> list = ticketCategoryService.selectBatchIds(tcId);
		boolean isDel = ticketCategoryService.deleteBatchIds(tcId);
		if(isDel) {
			TicketCategory ty = null;
			String name = null;
			for(TicketCategory tcy : list) {
				ty = tcy;
				name += ty.getTicketCateName()+",";
			}
			name = name.substring(0, name.length());
			logService.insertLog(OperateLogModelEnum.TICKETCATEGROY.getModule(),"删除票种",getRemoteAddr(),"被删除票种名称【"+name+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看票种设置表详情", notes = "查看票种设置表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{tcId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("tcId") String tcId) {
		TicketCategory ticketCategory = ticketCategoryService.selectById(tcId);
		return CommonResponse.createCommonResponse(ticketCategory);
	}
}
