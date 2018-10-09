package com.yxhl.stationbiz.web.consumer.controller.security;

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
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.security.VehicleInstation;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;
import com.yxhl.stationbiz.system.domain.service.security.VehicleInstationService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  车辆进站表控制器
 *  创建人: ypf
 *  创建日期:2018-8-13 16:52:37
 */
@RestController
@Api(tags = "车辆进站表")
@RequestMapping(value = "/m")
public class VehicleInstationController extends BaseController{
	
    @Autowired
    private VehicleInstationService vehicleInstationService;
    
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
	@ApiOperation("分页查询")
	@PostMapping(value = "/vehicleInstation/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody VehicleInstation vehicleInstation) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<VehicleInstation> page = new Page<VehicleInstation>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		if (StringUtils.isNotBlank(getLoginUser().getOrgId()) && StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			vehicleInstation.setOrgId(getLoginUser().getOrgId());
			vehicleInstation.setCompId(getLoginUser().getCompanyId());
		}
		Page<VehicleInstation> result = vehicleInstationService.selPageList(page, vehicleInstation);
		List<Dictionary> driverTypeList = dictionaryService.getRediesByKey("seat_category"); //查询循环类型字典
	    resp.extendsRes("driverType", driverTypeList);
		resp.setData(result);
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询车辆进站表", notes = "根据条件查询车辆进站表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/vehicleInstations", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<VehicleInstation> wrapper = new EntityWrapper<VehicleInstation>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<VehicleInstation> list = vehicleInstationService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增车辆进站表", notes = "新增车辆进站表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/vehicleInstation", produces = "application/json;charset=UTF-8")
	public CommonResponse addVehicleInstation(@RequestBody VehicleInstation vehicleInstation) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleInstation.setOrgId(loginUser.getOrgId());
			vehicleInstation.setCompId(loginUser.getCompanyId());
			vehicleInstation.setCreateBy(loginUser.getId());
			vehicleInstation.setUpdateBy(loginUser.getId());
		}
		Wrapper<VehicleInstation> wrapper= new EntityWrapper<VehicleInstation>();
		wrapper.eq("vehicle_id", vehicleInstation.getVehicleId());
		wrapper.eq("org_id", vehicleInstation.getOrgId());
		wrapper.eq("comp_id", vehicleInstation.getCompId());
		wrapper.eq("station_id", vehicleInstation.getStationId());
		List<VehicleInstation> list= vehicleInstationService.selectList(wrapper);
		if(list.size()>0) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "该车已添加进站!");
		}
		vehicleInstation.setId(IdWorker.get32UUID());
		boolean isAdded = vehicleInstationService.insert(vehicleInstation);
		if(isAdded) {
			Wrapper<Vehicle> veWrapper= new EntityWrapper<Vehicle>();
			Vehicle ve= vehicleService.selectOne(veWrapper);
			logService.insertLog(OperateLogModelEnum.VEHICLEINSTATION.getModule(),"新增进站车辆",getRemoteAddr(),"车辆号【"+ve.getCarNo()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(vehicleInstation);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改车辆进站表", notes = "修改车辆进站表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/vehicleInstation/{vehicleInstationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateVehicleInstation(@PathVariable("vehicleInstationId") String vehicleInstationId,@RequestBody VehicleInstation vehicleInstation) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleInstation.setUpdateBy(loginUser.getId());
		}
		vehicleInstation.setId(vehicleInstationId);
		boolean isUpdated = vehicleInstationService.updateById(vehicleInstation);
		if(isUpdated) {
			Wrapper<Vehicle> veWrapper= new EntityWrapper<Vehicle>();
			Vehicle ve= vehicleService.selectOne(veWrapper);
			logService.insertLog(OperateLogModelEnum.VEHICLEINSTATION.getModule(),"修改车辆信息",getRemoteAddr(),"车辆号【"+ve.getCarNo()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除车辆进站表", notes = "删除车辆进站表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/vehicleInstation/{vehicleInstationIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("vehicleInstationIds") List<String> vehicleInstationIds) {
		boolean isDel = vehicleInstationService.deleteBatchIds(vehicleInstationIds);
		if(isDel) {
			Wrapper<Vehicle> veWrapper= new EntityWrapper<Vehicle>();
			Vehicle ve= vehicleService.selectOne(veWrapper);
			logService.insertLog(OperateLogModelEnum.VEHICLEINSTATION.getModule(),"删除车辆进站信息",getRemoteAddr(),"车辆号【"+ve.getCarNo()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看车辆进站表详情", notes = "查看车辆进站表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/vehicleInstation/{vehicleInstationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("vehicleInstationId") String vehicleInstationId) {
		VehicleInstation vehicleInstation = vehicleInstationService.selectById(vehicleInstationId);
		return CommonResponse.createCommonResponse(vehicleInstation);
	}
}
