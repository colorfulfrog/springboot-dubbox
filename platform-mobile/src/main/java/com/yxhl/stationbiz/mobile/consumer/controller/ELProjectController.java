package com.yxhl.stationbiz.mobile.consumer.controller;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.ELProject;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.ELProjectService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "项目信息")
@RequestMapping(value = "/c/project")
public class ELProjectController extends BaseController {
	@Autowired
	private ELProjectService projectService;
	
	@Autowired
	private OperateLogService logService;

	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ELProject project) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ELProject> page = new Page<ELProject>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ELProject> result = projectService.selPageList(page, project);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation("查询所有项目")
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody ELProject project) {
		Wrapper<ELProject> wrapper = new EntityWrapper<ELProject>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<ELProject> list = projectService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("查询单个项目基本信息")
	@GetMapping(value = "/{projectId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("projectId") Integer projectId) {
		ELProject project = projectService.selectById(projectId);
		return CommonResponse.createCommonResponse(project);
	}
	
	@ApiOperation("新增项目基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addProject(@RequestBody ELProject project) {
		//checkNotNull(project.getId(), "ID为空");
		//checkArgument(expression, errorMessage);
		boolean isAdded = projectService.insert(project);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.BUS_ENTRANCE.getModule(),"新增项目",getRemoteAddr(),"新增项目【"+project.getName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(project);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增项目失败!");
		}
	}
	
	@ApiOperation("修改项目基本信息")
	@PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateProject(@RequestBody ELProject project) {
		boolean isAdded = projectService.updateById(project);
		if(isAdded) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改项目失败!");
		}
	}
	
	@ApiOperation("删除项目")
	@DeleteMapping(value = "/{projectId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("projectId") Integer projectId) {
		boolean isDel = projectService.deleteById(projectId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除项目失败!");
		}
	}
}