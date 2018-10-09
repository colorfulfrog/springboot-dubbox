package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
import com.yxhl.platform.common.utils.Util;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  线路表控制器
 *  创建人: xjh
 *  创建日期:2018-7-10 14:54:42
 */
@RestController
@Api(tags = "线路表控制器")
@RequestMapping(value = "/m/line/")
public class LineController extends BaseController{
	
    @Autowired
    private LineService lineService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam(value="currentPage",required=false) String currentPage,@RequestParam(value="pageSize",required=false)String pageSize,@RequestBody Line line) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser user=getLoginUser();
		if(Util.isNotNull(user.getOrgId())) {
			line.setOrgId(user.getOrgId());
		}
		//如果未传 compId  默认查询当前用户所属单位的线路
		if(!Util.isNotNull(line.getCompId())) {
			line.setCompId(user.getCompanyId());
		}
		Page<Line> page = new Page<Line>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Line> result = lineService.selPageList(page, line);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有线路表", notes = "查询所有线路表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Line line) {
		Wrapper<Line> wrapper = new EntityWrapper<Line>();
		ELUser user= this.getLoginUser();
		if(Util.isNotNull(user.getOrgId())) {
			wrapper.eq("org_id", user.getOrgId());
		}
		if(Util.isNotNull(user.getCompanyId())) {
			wrapper.eq("comp_id", user.getCompanyId());
		}
		wrapper.orderBy("create_time", false);
		List<Line> list = lineService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增线路表", notes = "新增线路表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addLine(@RequestBody Line line) {
		checkNotNull(line,"参数错误");
		checkNotNull(line.getStartStateId(),"起点站不能为空");
		checkNotNull(line.getEndStateId(),"终点站不能为空");
		checkArgument(!StringUtils.equals(line.getStartStateId(), line.getEndStateId()),"添加失败！线路【起点站】不能与【终点站】相同 ");
		line.setId(IdWorker.get32UUID());
		ELUser user= this.getLoginUser();
		line.setCreateBy(user.getId());
		line.setUpdateBy(user.getId()); 
		line.setOrgId(user.getOrgId());
		//line.setCompId(user.getCompanyId()); 
		line.setStatus(0); //正常
		boolean isAdded = lineService.insertLine(line);
		if(isAdded) {
			return CommonResponse.createCommonResponse(line);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改线路表", notes = "修改线路表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{lineId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateLine(@PathVariable("lineId") String lineId,@RequestBody Line line) {
		if(null==line||null==lineId) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "参数错误");
		}
		checkNotNull(line.getStartStateId(),"起点站不能为空");
		checkNotNull(line.getEndStateId(),"终点站不能为空");
		checkArgument(!StringUtils.equals(line.getStartStateId(), line.getEndStateId()),"添加失败！线路【起点站】不能与【终点站】相同 ");
		line.setId(lineId); 
		ELUser user= this.getLoginUser();
		line.setUpdateBy(user.getId()); 
		boolean isUpdated = lineService.updateLine(line);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除线路表", notes = "删除线路表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{lineIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("lineIds") List<String> lineIds) {
		boolean isDel = lineService.delLine(lineIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看线路表详情", notes = "查看线路表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{lineId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("lineId") String lineId) {
		Line line = lineService.selById(lineId);
		return CommonResponse.createCommonResponse(line);
	}
	
	
	
	

}
