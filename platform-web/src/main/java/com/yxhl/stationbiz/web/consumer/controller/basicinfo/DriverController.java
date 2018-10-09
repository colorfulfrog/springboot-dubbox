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
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.DriverService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  驾驶员表控制器
 *  创建人: xjh
 *  创建日期:2018-7-12 15:26:07
 */
@RestController
@Api(tags = "驾驶员表控制器")
@RequestMapping(value = "/m/driver/")
public class DriverController extends BaseController{
	
    @Autowired
    private DriverService driverService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Driver driver) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		driver.setOrgId(getLoginUser().getOrgId());
		Page<Driver> page = new Page<Driver>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Driver> result = driverService.selPageList(page, driver);
		resp.setData(result);
	    List<Dictionary> levelint = dictionaryService.getRediesByKey("educational_levelint"); //查询文化程度 字典
	    resp.extendsRes("levelint", levelint);
	    return resp;
	}
	
	@ApiOperation(value = "查询所有驾驶员表", notes = "查询所有驾驶员表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Driver driver) {
		Wrapper<Driver> wrapper = new EntityWrapper<Driver>();
		if(StringUtils.isNotBlank(driver.getShortSpell())) {
			wrapper.like("short_spell", driver.getShortSpell());
		}
		if(StringUtils.isNotBlank(driver.getLine())) {
			wrapper.eq("line", driver.getLine());
		}
		if(StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		if(StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		List<Driver> list = driverService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增驾驶员表", notes = "新增驾驶员表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addDriver(@RequestBody Driver driver) {
		driver.setCreateBy(getLoginUser().getId());
		driver.setId(IdWorker.get32UUID());
		driver.setOrgId(getLoginUser().getOrgId());
		boolean isAdded = driverService.add(driver);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.DRIVER.getModule(),"新增司机",getRemoteAddr(),"司机姓名【"+driver.getDriverName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(driver);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改驾驶员表", notes = "修改驾驶员表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{driverId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateDriver(@RequestBody Driver driver,@PathVariable("driverId") String driverId) {
		driver.setId(driverId);
		driver.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = driverService.updated(driver);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.DRIVER.getModule(),"修改司机",getRemoteAddr(),"被修改司机姓名【"+driver.getDriverName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除驾驶员表", notes = "删除驾驶员表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{driverId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("driverId") List<String> driverId) {
		List<Driver> list = driverService.selectBatchIds(driverId);
		Driver dv = null;
		String drName = null;
		for(Driver der : list) {
			dv = der;
			drName += dv.getDriverName();
		}
		drName = drName.substring(0, drName.length());
		boolean isDel = driverService.deleteBatchIds(driverId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.DRIVER.getModule(),"删除司机",getRemoteAddr(),"被删除司机姓名【"+drName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看驾驶员表详情", notes = "查看驾驶员表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{driverId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("driverId") String driverId) {
		Driver driver = driverService.selectById(driverId);
		return CommonResponse.createCommonResponse(driver);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "driverName", required = false) String driverName,
			@RequestParam(value = "identityCard", required = false) String identityCard,
			@RequestParam(value = "compId", required = false) String compId) throws Exception {
		Driver param = new Driver();
		param.setOrgId(getLoginUser().getOrgId());
		List<Driver> list = driverService.exportData(param);
		OutputStream out = null;
		try {
			String fileName = "驾驶员导出.xlsx";
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
				workbook = context.createExcelTemplate("DriverExport",null,null);
			}else {
				workbook = context.createExcel("DriverExport", list);
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
