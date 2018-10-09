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
import com.yxhl.easy.excel.ExcelContext;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.DriverVehicle;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverVehicleService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  驾驶员车辆绑定表控制器
 *  创建人: xjh
 *  创建日期:2018-7-9 17:39:14
 */
@RestController
@Api(tags = "驾驶员车辆绑定表控制器")
@RequestMapping(value = "/m/driverVehicle/")
public class DriverVehicleController extends BaseController{
	
    @Autowired
    private DriverVehicleService driverVehicleService;
    
    @Autowired
	private OperateLogService logService;
    
    @Autowired
    private DriverService driverService;
    
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody DriverVehicle driverVehicle) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		if(driverVehicle.getCompId()==null || driverVehicle.getCompId()=="") {
			driverVehicle.setCompId(getLoginUser().getCompanyId());
		}
		Page<DriverVehicle> page = new Page<DriverVehicle>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<DriverVehicle> result = driverVehicleService.selPageList(page, driverVehicle);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有驾驶员车辆绑定表", notes = "查询所有驾驶员车辆绑定表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody DriverVehicle driverVehicle) {
		Wrapper<DriverVehicle> wrapper = new EntityWrapper<DriverVehicle>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<DriverVehicle> list = driverVehicleService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增驾驶员车辆绑定表", notes = "新增驾驶员车辆绑定表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addDriverVehicle(@RequestBody DriverVehicle dv) {
		dv.setCreateBy(getLoginUser().getId());
		boolean isAdded = driverVehicleService.add(dv);
		if(isAdded) {  
			Driver dr = driverService.selectById(dv.getDriverId());
			logService.insertLog(OperateLogModelEnum.DRIVER_VEHICLE_BIND.getModule(),"新增驾驶员车辆绑定",getRemoteAddr(),"司机姓名【"+dr.getDriverName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(dv);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改驾驶员车辆绑定表", notes = "修改驾驶员车辆绑定表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{driverId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateDriverVehicle(@RequestBody DriverVehicle dv,@PathVariable("driverId") String driverId) {
		dv.setDriverId(driverId);
		dv.setUpdateBy(getLoginUser().getId());
		boolean updateDv = driverVehicleService.updateDv(dv);
		if(updateDv) {
			Driver dr = driverService.selectById(dv.getDriverId());
			logService.insertLog(OperateLogModelEnum.DRIVER_VEHICLE_BIND.getModule(),"修改驾驶员车辆绑定",getRemoteAddr(),"被修改的司机姓名【"+dr.getDriverName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除驾驶员车辆绑定表", notes = "删除驾驶员车辆绑定表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{driverId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("driverId") List<String> driverId) {
		List<Driver> list = driverService.selectBatchIds(driverId);
		Driver dr = null;
		String drName = null;
		for(Driver driver : list) {
			dr = driver;
			drName += dr.getDriverName()+",";
		}
		drName = drName.substring(0, drName.length());
		boolean isDel = driverVehicleService.deleteBydrId(driverId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.DRIVER_VEHICLE_BIND.getModule(),"删除驾驶员车辆绑定",getRemoteAddr(),"被删除的司机姓名【"+drName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看驾驶员车辆绑定表详情", notes = "查看驾驶员车辆绑定表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{dvId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("dvId") String dvId) {
		DriverVehicle one = driverVehicleService.getOne(dvId);
		return CommonResponse.createCommonResponse(one);
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
		DriverVehicle param = new DriverVehicle();
		param.setCompId(getLoginUser().getCompanyId());
		List<DriverVehicle> list = driverVehicleService.exportData(param);
		OutputStream out = null;
		try {
			String fileName = "驾驶员绑定车辆导出.xlsx";
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
				workbook = context.createExcelTemplate("DriverVehicleExport",null,null);
			}else {
				workbook = context.createExcel("DriverVehicleExport", list);
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
