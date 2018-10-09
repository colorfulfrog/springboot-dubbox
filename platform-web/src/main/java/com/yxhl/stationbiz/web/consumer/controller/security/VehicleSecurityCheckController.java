package com.yxhl.stationbiz.web.consumer.controller.security;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
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
import com.yxhl.stationbiz.system.domain.entity.security.VehicleSecurityCheck;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.security.VehicleSecurityCheckService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  车辆安检表控制器
 *  创建人: xjh
 *  创建日期:2018-8-13 17:04:26
 */
@RestController
@Api(tags = "车辆安检表")
@RequestMapping(value = "/m/vehicleSecurityCheck/")
public class VehicleSecurityCheckController extends BaseController{
	
    @Autowired
    private VehicleSecurityCheckService vehicleSecurityCheckService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody VehicleSecurityCheck vehicleSecurityCheck) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		vehicleSecurityCheck.setOrgId(getLoginUser().getOrgId());
		Page<VehicleSecurityCheck> page = new Page<VehicleSecurityCheck>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<VehicleSecurityCheck> result = vehicleSecurityCheckService.selPageList(page, vehicleSecurityCheck);
		resp.setData(result);
	    List<Dictionary> fuelType = dictionaryService.getRediesByKey("fuel_type"); //查询车辆燃油类型
	    List<Dictionary> operationType = dictionaryService.getRediesByKey("operation_type"); //查询车辆管理中的营运类型
	    resp.extendsRes("fuelType", fuelType);
	    resp.extendsRes("operationType", operationType);
	    return resp;
	}
	
	@ApiOperation(value = "根据条件查询车辆安检表", notes = "根据条件查询车辆安检表信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<VehicleSecurityCheck> wrapper = new EntityWrapper<VehicleSecurityCheck>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<VehicleSecurityCheck> list = vehicleSecurityCheckService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增车辆安检表", notes = "新增车辆安检表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addVehicleSecurityCheck(@RequestBody VehicleSecurityCheck vehicleSecurityCheck) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleSecurityCheck.setOrgId(loginUser.getOrgId());
			vehicleSecurityCheck.setCompId(loginUser.getCompanyId());
			vehicleSecurityCheck.setCreateBy(loginUser.getId());
			vehicleSecurityCheck.setUpdateBy(loginUser.getId());
			vehicleSecurityCheck.setCheckerId(loginUser.getId());
		}
		vehicleSecurityCheck.setCheckDate(new Date());
		vehicleSecurityCheck.setId(IdWorker.get32UUID());
		vehicleSecurityCheck.setStationId(loginUser.getStationId());//当前登录人所属站点
		boolean isAdded = vehicleSecurityCheckService.add(vehicleSecurityCheck);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.VEHICLESECURUTYCHECK.getModule(),"新增车辆安检",getRemoteAddr(),"车辆安检ID【"+vehicleSecurityCheck.getId()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(vehicleSecurityCheck);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改车辆安检表", notes = "修改车辆安检表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{vsId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateVehicleSecurityCheck(@PathVariable("vsId") String vehicleckId,@RequestBody VehicleSecurityCheck vehicleSecurityCheck) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			vehicleSecurityCheck.setUpdateBy(loginUser.getId());
		}
		vehicleSecurityCheck.setId(vehicleckId);
		boolean isUpdated = vehicleSecurityCheckService.updateById(vehicleSecurityCheck);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.VEHICLESECURUTYCHECK.getModule(),"修改车辆安检",getRemoteAddr(),"被修改车辆安检ID【"+vehicleSecurityCheck.getId()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除车辆安检表", notes = "删除车辆安检表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{vsId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("vsId") List<String> vsId) {
		List<VehicleSecurityCheck> list = vehicleSecurityCheckService.selectBatchIds(vsId);
		VehicleSecurityCheck ve = null;
		String id = null;
		for(VehicleSecurityCheck vce : list) {
			ve = vce;
			id += ve.getCarNo()+",";
		}
		id = id.substring(0, id.length());
		boolean isDel = vehicleSecurityCheckService.deleteBatchIds(vsId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.VEHICLESECURUTYCHECK.getModule(),"删除车辆安检",getRemoteAddr(),"被删除车辆安检ID【"+id+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看车辆安检表详情", notes = "查看车辆安检表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{vsId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("vsId") String vsId) {
		VehicleSecurityCheck vehicleSecurityCheck = vehicleSecurityCheckService.selectById(vsId);
		return CommonResponse.createCommonResponse(vehicleSecurityCheck);
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
		VehicleSecurityCheck param = new VehicleSecurityCheck();
		param.setOrgId(getLoginUser().getOrgId());
		List<VehicleSecurityCheck> list = vehicleSecurityCheckService.exportData(param);
		OutputStream out = null;
		try {
			String fileName = "车辆安检导出.xlsx";
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
				workbook = context.createExcelTemplate("VehicleSecurityCheckExport",null,null);
			}else {
				workbook = context.createExcel("VehicleSecurityCheckExport", list);
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
