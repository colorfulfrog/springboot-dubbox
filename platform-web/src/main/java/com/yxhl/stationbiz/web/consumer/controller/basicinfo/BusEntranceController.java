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
import com.yxhl.stationbiz.system.domain.entity.basicinfo.BusEntrance;
import com.yxhl.stationbiz.system.domain.service.basicinfo.BusEntranceService;


/**
 *	
 *  乘车库表控制器
 *  创建人: lw
 *  创建日期:2018-7-10 9:48:43
 */
@RestController
@Api(tags = "乘车库表控制器")
@RequestMapping(value = "/m/busEntrance/")
public class BusEntranceController extends BaseController{
	
    @Autowired
    private BusEntranceService busEntranceService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody BusEntrance busEntrance) {
		ELUser loginUser = getLoginUser();
		busEntrance.setOrgId(loginUser.getOrgId());
		busEntrance.setCompId(loginUser.getCompanyId());
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<BusEntrance> page = new Page<BusEntrance>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<BusEntrance> result = busEntranceService.selPageList(page, busEntrance);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有乘车库表", notes = "查询所有乘车库表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody BusEntrance busEntrance) {
		ELUser loginUser = getLoginUser();
		Wrapper<BusEntrance> wrapper = new EntityWrapper<BusEntrance>();
		if(StringUtils.isNoneBlank(busEntrance.getStationId())) {
			wrapper.where("station_id={0}", busEntrance.getStationId());
		}
		if(StringUtils.isNoneBlank(busEntrance.getEntranceName())) {
			wrapper.like("entrance_name", busEntrance.getEntranceName());
		}
		if(StringUtils.isNoneBlank(loginUser.getOrgId())) {
			wrapper.and("org_id={0}", loginUser.getOrgId());
		}
		if(StringUtils.isNoneBlank(loginUser.getCompanyId())) {
			wrapper.and("comp_id={0}", loginUser.getCompanyId());
		}
		List<BusEntrance> list = busEntranceService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增乘车库表", notes = "新增乘车库表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addBusEntrance(@RequestBody BusEntrance busEntrance) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			busEntrance.setOrgId(loginUser.getOrgId());
			busEntrance.setCompId(loginUser.getCompanyId());
			busEntrance.setCreateBy(loginUser.getId());
			busEntrance.setUpdateBy(loginUser.getId());
		}
		busEntrance.setId(IdWorker.get32UUID());
		boolean isAdded = busEntranceService.insert(busEntrance);
		if(isAdded) {
			return CommonResponse.createCommonResponse(busEntrance);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改乘车库表", notes = "修改乘车库表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/{busEntranceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateBusEntrance(@PathVariable("busEntranceId") String busEntranceId,@RequestBody BusEntrance busEntrance) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			busEntrance.setUpdateBy(loginUser.getId());
		}
		busEntrance.setId(busEntranceId);
		boolean isUpdated = busEntranceService.updateById(busEntrance);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除乘车库表", notes = "删除乘车库表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{busEntranceIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("busEntranceIds") List<String> busEntranceIds) {
		boolean isDel = busEntranceService.deleteBatchIds(busEntranceIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看乘车库表详情", notes = "查看乘车库表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{busEntranceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("busEntranceId") String busEntranceId) {
		BusEntrance busEntrance = busEntranceService.selectById(busEntranceId);
		return CommonResponse.createCommonResponse(busEntrance);
	}
}
