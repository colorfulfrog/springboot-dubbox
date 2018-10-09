package com.yxhl.stationbiz.web.consumer.controller.sys;

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
import com.yxhl.stationbiz.system.domain.entity.sys.OperateLog;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  操作日志表控制器
 *  创建人: xjh
 *  创建日期:2018-7-9 17:20:55
 */
@RestController
@Api(tags = "操作日志表控制器")
@RequestMapping(value = "/m/operateLog/")
public class OperateLogController extends BaseController{
	
    @Autowired
    private OperateLogService operateLogService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody OperateLog operateLog) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<OperateLog> page = new Page<OperateLog>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<OperateLog> result = operateLogService.selPageList(page, operateLog);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有操作日志表", notes = "查询所有操作日志表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody OperateLog operateLog) {
		Wrapper<OperateLog> wrapper = new EntityWrapper<OperateLog>();
		List<OperateLog> list = operateLogService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增操作日志表", notes = "新增操作日志表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addOperateLog(@RequestBody OperateLog operateLog) {
		operateLog.setId(IdWorker.get32UUID());
		operateLog.setCreateBy(getLoginUser().getId());
		boolean isAdded = operateLogService.insert(operateLog);
//		boolean insertLog = operateLogService.insertLog(operateLog);
//		System.err.println(insertLog);
		if(isAdded) {
			return CommonResponse.createCommonResponse(operateLog);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改操作日志表", notes = "修改操作日志表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{operateLogId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateOperateLog(@RequestBody OperateLog operateLog,@PathVariable("operateLogId") String operateLogId) {
		operateLog.setId(operateLogId);
		operateLog.setUpdateBy(getLoginUser().getId());
		boolean isAdded = operateLogService.updateById(operateLog);
		if(isAdded) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除操作日志表", notes = "删除操作日志表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{operateLogId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("operateLogId") List<String> operateLogId) {
		boolean isDel = operateLogService.deleteBatchIds(operateLogId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看操作日志表详情", notes = "查看操作日志表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{operateLogId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("operateLogId") String operateLogId) {
		OperateLog operateLog = operateLogService.selectById(operateLogId);
		return CommonResponse.createCommonResponse(operateLog);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "module", required = false) String module,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "ip", required = false) String ip) throws Exception {
		OperateLog param = new OperateLog();
		
		List<OperateLog> list = operateLogService.exportData(param);

		OutputStream out = null;
		try {
			String fileName = "操作日志导出.xlsx";
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
				workbook = context.createExcelTemplate("OperateLogExport",null,null);
			}else {
				workbook = context.createExcel("OperateLogExport", list);
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
