package com.yxhl.stationbiz.web.consumer.controller.check;

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
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.check.VehicleOutboundCheck;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.check.VehicleOutboundCheckService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  车辆出站稽查表控制器
 *  创建人: xjh
 *  创建日期:2018-9-12 10:22:24
 */
@RestController
@Api(tags = "车辆出站稽查表")
@RequestMapping(value = "/m/check")
public class VehicleOutboundCheckController extends BaseController{
	
    @Autowired
    private VehicleOutboundCheckService vehicleOutboundCheckService;
    
    @Autowired
	private OperateLogService logService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody VehicleOutboundCheck vehicleOutboundCheck) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		if(loginUser!=null) {
			vehicleOutboundCheck.setOrgId(loginUser.getOrgId());
			vehicleOutboundCheck.setCompId(loginUser.getCompanyId());
		}
		Page<VehicleOutboundCheck> page = new Page<VehicleOutboundCheck>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<VehicleOutboundCheck> selPageList = vehicleOutboundCheckService.selPageList(page, vehicleOutboundCheck);
		resp.setData(selPageList);
		List<Dictionary> busStatus = dictionaryService.getRediesByKey("bao_bus_status"); //查询班次状态字典
		resp.extendsRes("busStatus", busStatus);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询车辆出站稽查表", notes = "根据条件查询车辆出站稽查表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<VehicleOutboundCheck> wrapper = new EntityWrapper<VehicleOutboundCheck>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<VehicleOutboundCheck> list = vehicleOutboundCheckService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增车辆出站稽查表", notes = "新增车辆出站稽查表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addVehicleOutboundCheck(@RequestBody VehicleOutboundCheck vehicleOutboundCheck) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleOutboundCheck.setOrgId(loginUser.getOrgId());
			vehicleOutboundCheck.setCompId(loginUser.getCompanyId());
			vehicleOutboundCheck.setCreateBy(loginUser.getId());
			vehicleOutboundCheck.setUpdateBy(loginUser.getId());
		}
		boolean isAdded = vehicleOutboundCheckService.add(vehicleOutboundCheck);
		if(isAdded) {
			return CommonResponse.createCommonResponse(vehicleOutboundCheck);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改车辆出站", notes = "修改车辆出站", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{vochekId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateVehicleOutboundCheck(@PathVariable("vochekId") String vochekId,@RequestBody VehicleOutboundCheck vehicleOutboundCheck) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleOutboundCheck.setUpdateBy(loginUser.getId());
		}
		vehicleOutboundCheck.setId(vochekId);
		boolean isUpdated = vehicleOutboundCheckService.updateById(vehicleOutboundCheck);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	
	@ApiOperation(value = "删除车辆出站稽查表", notes = "删除车辆出站稽查表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{vochekId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("vochekId") List<String> vochekId) {
		boolean isDel = vehicleOutboundCheckService.deleteBatchIds(vochekId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看车辆出站稽查表详情", notes = "查看车辆出站稽查表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{vochekId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("vochekId") String vochekId) {
		VehicleOutboundCheck one = vehicleOutboundCheckService.getOne(vochekId);
		return CommonResponse.createCommonResponse(one);
	}
	
	@ApiOperation(value = "查看车辆出站稽查表详情", notes = "查看车辆出站稽查表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/addjc/{busId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoByBusId(@PathVariable("busId") String busId) {
		VehicleOutboundCheck ck = vehicleOutboundCheckService.getByBusId(busId);
		return CommonResponse.createCommonResponse(ck);
	}
	
//	@ApiOperation(value = "查看车辆出站稽查表详情", notes = "查看车辆出站稽查表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
//	@GetMapping(value = "/info/{vochekId}", produces = "application/json;charset=UTF-8")
//	public CommonResponse getInfoById(@PathVariable("vochekId") String vochekId) {
//		VehicleOutboundCheck vehicleOutboundCheck = vehicleOutboundCheckService.selectById(vochekId);
//		return CommonResponse.createCommonResponse(vehicleOutboundCheck);
//	}
}
