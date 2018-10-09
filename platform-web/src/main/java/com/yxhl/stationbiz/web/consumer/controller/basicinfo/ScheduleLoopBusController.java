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
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoopBus;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleLoopBusService;


/**
 *	
 *  班次循环配置中间表控制器
 *  创建人: lw
 *  创建日期:2018-7-11 9:31:17
 */
@RestController
@Api(tags = "班次循环配置中间表控制器")
@RequestMapping(value = "/m/scheduleLoopBus/")
public class ScheduleLoopBusController extends BaseController{
	
    @Autowired
    private ScheduleLoopBusService scheduleLoopBusService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleLoopBus scheduleLoopBus) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleLoopBus> page = new Page<ScheduleLoopBus>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleLoopBus> result = scheduleLoopBusService.selPageList(page, scheduleLoopBus);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有班次循环配置中间表", notes = "查询所有班次循环配置中间表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody ScheduleLoopBus scheduleLoopBus) {
		Wrapper<ScheduleLoopBus> wrapper = new EntityWrapper<ScheduleLoopBus>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<ScheduleLoopBus> list = scheduleLoopBusService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增班次循环配置中间表", notes = "新增班次循环配置中间表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleLoopBus(@RequestBody ScheduleLoopBus scheduleLoopBus) {
		scheduleLoopBus.setId(IdWorker.get32UUID());
		boolean isAdded = scheduleLoopBusService.insert(scheduleLoopBus);
		if(isAdded) {
			return CommonResponse.createCommonResponse(scheduleLoopBus);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次循环配置中间表", notes = "修改班次循环配置中间表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleLoopBus(@RequestBody ScheduleLoopBus scheduleLoopBus) {
		boolean isUpdated = scheduleLoopBusService.updateById(scheduleLoopBus);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除班次循环配置中间表", notes = "删除班次循环配置中间表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{scheduleLoopBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("scheduleLoopBusId") String scheduleLoopBusId) {
		boolean isDel = scheduleLoopBusService.deleteById(scheduleLoopBusId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看班次循环配置中间表详情", notes = "查看班次循环配置中间表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{scheduleLoopBusId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleLoopBusId") String scheduleLoopBusId) {
		ScheduleLoopBus scheduleLoopBus = scheduleLoopBusService.selectById(scheduleLoopBusId);
		return CommonResponse.createCommonResponse(scheduleLoopBus);
	}
}
