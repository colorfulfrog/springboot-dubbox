package com.yxhl.stationbiz.web.consumer.controller.schedule;

import java.util.ArrayList;
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
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.schedule.HolidayService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  节日设置表控制器
 *  创建人: ypf
 *  创建日期:2018-8-14 9:18:17
 */
@RestController
@Api(tags = "节日设置表")
@RequestMapping(value = "/m")
public class HolidayController extends BaseController{
	
    @Autowired
    private HolidayService holidayService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/holiday/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody Holiday holiday) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Holiday> page = new Page<Holiday>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		if (StringUtils.isNotBlank(getLoginUser().getOrgId()) && StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			holiday.setOrgId(getLoginUser().getOrgId());
			holiday.setCompId(getLoginUser().getCompanyId());
		}
		Page<Holiday> result = holidayService.selPageList(page, holiday);
		resp.setData(result);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询节日设置表", notes = "根据条件查询节日设置表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/holidays", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<Holiday> wrapper = new EntityWrapper<Holiday>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<Holiday> list = holidayService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增节日设置表", notes = "新增节日设置表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/holiday", produces = "application/json;charset=UTF-8")
	public CommonResponse addHoliday(@RequestBody Holiday holiday) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			holiday.setOrgId(loginUser.getOrgId());
			holiday.setCompId(loginUser.getCompanyId());
			holiday.setCreateBy(loginUser.getId());
			holiday.setUpdateBy(loginUser.getId());
		}
		holiday.setId(IdWorker.get32UUID());
		boolean isAdded = holidayService.insert(holiday);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.HOLIDAY.getModule(),"新增节日信息",getRemoteAddr(),"节日名称【"+holiday.getHolidayName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(holiday);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改节日设置表", notes = "修改节日设置表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/holiday/{holidayId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateHoliday(@PathVariable("holidayId") String holidayId,@RequestBody Holiday holiday) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			holiday.setUpdateBy(loginUser.getId());
		}
		holiday.setId(holidayId);
		boolean isUpdated = holidayService.updateById(holiday);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.HOLIDAY.getModule(),"修改节日信息",getRemoteAddr(),"节日名称【"+holiday.getHolidayName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除节日设置表", notes = "删除节日设置表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/holiday/{holidayIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("holidayIds") List<String> holidayIds) {
		boolean isDel = holidayService.deleteBatchIds(holidayIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看节日设置表详情", notes = "查看节日设置表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/holiday/{holidayId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("holidayId") String holidayId) {
		Holiday holiday = holidayService.selectById(holidayId);
		return CommonResponse.createCommonResponse(holiday);
	}
	
	
	@ApiOperation(value = "节日下拉", notes = "节日下拉", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/holidaySelect/{scheduleTplId}", produces = "application/json;charset=UTF-8")
	public CommonResponse holidaySelect(@PathVariable("scheduleTplId")String scheduleTplId) {
		/*Wrapper<HolidayPrice> wrapper = new EntityWrapper<HolidayPrice>();
		wrapper.eq("org_id", getLoginUser().getOrgId());
		wrapper.eq("comp_id", getLoginUser().getCompanyId());
		wrapper.eq("schedule_tpl_id", scheduleTplId);
		List<HolidayPrice> list = holidayPriceService.selectList(wrapper);
		List<String> holidayIds= new ArrayList<String>();
		List<Holiday> holiday =new ArrayList<Holiday>();
		Wrapper<Holiday> wrapper1 = new EntityWrapper<Holiday>();
		if(list.size()>0) {
			for (HolidayPrice holidayPrice : list) {
				holidayIds.add(holidayPrice.getHolidayId());
			}
			wrapper1.notIn("id", holidayIds);
			holiday=holidayService.selectList(wrapper1);
		}else {
			wrapper1.eq("org_id", getLoginUser().getOrgId());
			wrapper1.eq("comp_id", getLoginUser().getCompanyId());
			holiday=holidayService.selectList(wrapper1);
		}
		return CommonResponse.createCommonResponse(holiday);*/
		List<Holiday> holiday =new ArrayList<Holiday>();
		Wrapper<Holiday> wrapper1 = new EntityWrapper<Holiday>();
		wrapper1.eq("org_id", getLoginUser().getOrgId());
		wrapper1.eq("comp_id", getLoginUser().getCompanyId());
		holiday = holidayService.selectList(wrapper1);
		return CommonResponse.createCommonResponse(holiday);
	}
	
}
