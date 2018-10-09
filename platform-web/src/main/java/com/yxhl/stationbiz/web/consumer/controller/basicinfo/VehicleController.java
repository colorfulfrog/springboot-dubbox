package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.easy.excel.ExcelContext;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.VehicleService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  车辆表控制器
 *  创建人: xjh
 *  创建日期:2018-7-12 15:38:43
 */
@RestController
@Api(tags = "车辆表控制器")
@RequestMapping(value = "/m/vehicle/")
public class VehicleController extends BaseController{
	
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Vehicle vehicle) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		vehicle.setOrgId(getLoginUser().getOrgId());
		if(StringUtils.isNotBlank(vehicle.getCompId())){
			vehicle.setCompId(getLoginUser().getCompanyId());
		}
		Page<Vehicle> page = new Page<Vehicle>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Vehicle> result = vehicleService.selPageList(page, vehicle);
		resp.setData(result);
	    List<Dictionary> color = dictionaryService.getRediesByKey("car_brand_color"); //查询车牌颜色字典
	    resp.extendsRes("color", color);
	    return resp;
	}
	
	@ApiOperation(value = "查询所有车辆表", notes = "查询所有车辆表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Vehicle vehicle) {
		ELUser loginUser = getLoginUser();
		Wrapper<Vehicle> wrapper = new EntityWrapper<Vehicle>();
		if(StringUtils.isNotBlank(vehicle.getCarNo())) {
			wrapper.like("car_no", vehicle.getCarNo());
		}
		if(StringUtils.isNotBlank(vehicle.getLineId())) {
			wrapper.eq("line_id", vehicle.getLineId());
		}
		if(StringUtils.isNotBlank(loginUser.getCompanyId())) {
			wrapper.eq("comp_id", loginUser.getCompanyId());
		}
		if(StringUtils.isNotBlank(loginUser.getOrgId())) {
			wrapper.eq("org_id", loginUser.getOrgId());
		}
		List<Vehicle> list = vehicleService.selectList(wrapper);
		Wrapper<Vehicle> wrapper1 = new EntityWrapper<Vehicle>();
		//查本单位所有线路下的车辆
		wrapper1.eq("all_comp_lines", 1);
		wrapper1.eq("org_id", loginUser.getOrgId());
		wrapper1.eq("comp_id", loginUser.getCompanyId());
		List<Vehicle> selectList = vehicleService.selectList(wrapper1);
		for (Vehicle b: list){
            for (Vehicle a: selectList){
                if(b.getCarNo().equals(a.getCarNo())){
                	selectList.remove(a);
                    break;
                }
            }
		}
		selectList.addAll(list);
		return CommonResponse.createCommonResponse(selectList);
	}
	
	@ApiOperation(value = "查询所有车辆", notes = "查询所有车辆信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getVehicle", produces = "application/json;charset=UTF-8")
	public CommonResponse getVehicle(@RequestBody Vehicle vehicle) {
		if(vehicle.getCarNo()!=null) {
			vehicle.setCarNo(vehicle.getCarNo());
		}
		if(getLoginUser().getCompanyId()!=null) {
			vehicle.setCompId(getLoginUser().getCompanyId());
		}
		if(getLoginUser().getOrgId() !=null) {
			vehicle.setOrgId(getLoginUser().getOrgId());
		}
		List<Vehicle> vehicleList = vehicleService.getVehicle(vehicle);
		return CommonResponse.createCommonResponse(vehicleList);
	}

	@ApiOperation(value = "新增车辆表", notes = "新增车辆表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addVehicle(@RequestBody Vehicle vehicle) {
		vehicle.setId(IdWorker.get32UUID());
		vehicle.setCreateBy(getLoginUser().getId());
		vehicle.setOrgId(getLoginUser().getOrgId());
		boolean isAdded = vehicleService.add(vehicle);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.VEHICLE.getModule(),"新增车辆",getRemoteAddr(),"车牌号【"+vehicle.getCarNo()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(vehicle);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改车辆表", notes = "修改车辆表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{vehicleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateVehicle(@RequestBody Vehicle vehicle,@PathVariable("vehicleId") String vehicleId) {
		vehicle.setId(vehicleId);
		vehicle.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = vehicleService.updatev(vehicle);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.VEHICLE.getModule(),"修改车辆",getRemoteAddr(),"被修改车牌号【"+vehicle.getCarNo()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除车辆表", notes = "删除车辆表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{vehicleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("vehicleId") List<String> vehicleId) {
		List<Vehicle> list = vehicleService.selectBatchIds(vehicleId);
		boolean isDel = vehicleService.deleteBatchIds(vehicleId);
		if(isDel) {
			Vehicle ve = null;
			String carno = null;
			for(Vehicle vce : list) {
				ve = vce;
				carno += ve.getCarNo()+",";
			}
			carno = carno.substring(0, carno.length());
			logService.insertLog(OperateLogModelEnum.VEHICLE.getModule(),"删除车辆",getRemoteAddr(),"被删除车牌号【"+carno+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看车辆表详情", notes = "查看车辆表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{vehicleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("vehicleId") String vehicleId) {
		Vehicle vehicle = vehicleService.selectById(vehicleId);
		return CommonResponse.createCommonResponse(vehicle);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "carNo", required = false) String carNo,
			@RequestParam(value = "operationType", required = false) Integer operationType,
			@RequestParam(value = "compId", required = false) String compId) throws Exception {
		Vehicle param = new Vehicle();
		param.setOrgId(getLoginUser().getOrgId());
		List<Vehicle> list = vehicleService.exportData(param);
		OutputStream out = null;
		try {
			String fileName = "车辆导出.xlsx";
			String header = request.getHeader("User-Agent").toUpperCase();
			if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
				fileName = URLEncoder.encode(fileName, "utf-8");
				fileName = fileName.replace("+", "%20"); // IE下载文件名空格变+号问题
			} else {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
			}
			// 设置response参数，可以打开下载页面
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
			// 创建一个字节输出流
			out = response.getOutputStream();
			ExcelContext context = ExcelContext.newInstance("excel/OperateLog-config.xml");
			Workbook workbook = null;
			if(list == null || list.isEmpty()) {
				workbook = context.createExcelTemplate("VehicleExport",null,null);
			}else {
				workbook = context.createExcel("VehicleExport", list);
			}
			workbook.write(out); // 写入流
			out.flush();
		} catch (final IOException e) {
			throw e;
		} finally {
			if (out != null)
				out.close();
		}
	}
}
