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
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleLoopService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  班次调度循环配置控制器
 *  创建人: lw
 *  创建日期:2018-7-11 9:27:23
 */
@RestController
@Api(tags = "班次调度循环配置控制器")
@RequestMapping(value = "/m/scheduleLoop/")
public class ScheduleLoopController extends BaseController{
	
    @Autowired
    private ScheduleLoopService scheduleLoopService;
    @Autowired
    private LineService lineService;
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
	private ScheduleBusService scheduleBusService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ScheduleLoop scheduleLoop) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser loginUser = getLoginUser();
		scheduleLoop.setOrgId(loginUser.getOrgId());
		Page<ScheduleLoop> page = new Page<ScheduleLoop>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleLoop> result = scheduleLoopService.selPageList(page, scheduleLoop);
		resp.setData(result);
		List<Dictionary> loopTypeList = dictionaryService.getRediesByKey("loop_typeint"); //查询循环类型字典
		resp.extendsRes("loopType", loopTypeList);
		return resp;
	}
	
	@ApiOperation(value = "查询所有班次调度循环配置", notes = "查询所有班次调度循环配置信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody ScheduleLoop scheduleLoop) {
		Wrapper<ScheduleLoop> wrapper = new EntityWrapper<ScheduleLoop>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<ScheduleLoop> list = scheduleLoopService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增班次调度循环配置", notes = "新增班次调度循环配置信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleLoop(@RequestBody ScheduleLoop scheduleLoop) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			scheduleLoop.setOrgId(loginUser.getOrgId());
			scheduleLoop.setCompId(loginUser.getCompanyId());
			scheduleLoop.setCreateBy(loginUser.getId());
			scheduleLoop.setUpdateBy(loginUser.getId());
		}
		
		boolean isAdded = scheduleLoopService.addScheduleLoop(scheduleLoop);
		if(isAdded) {
			//添加操作日志
			Line line = lineService.selectById(scheduleLoop.getLineId());
			logService.insertLog(OperateLogModelEnum.BUS_LOOP.getModule(),"新增班次循环",getRemoteAddr(),"新增班次循环【线路名称："+line.getLineName()+"】",loginUser.getId());
			return CommonResponse.createCommonResponse(scheduleLoop);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次调度循环配置", notes = "修改班次调度循环配置信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/{scheduleLoopId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleLoop(@PathVariable("scheduleLoopId") String scheduleLoopId,@RequestBody ScheduleLoop scheduleLoop) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			scheduleLoop.setUpdateBy(loginUser.getId());
		}
		scheduleLoop.setId(scheduleLoopId);
		boolean isUpdated = scheduleLoopService.updateScheduleLoop(scheduleLoop);
		if(isUpdated) {
			//添加操作日志
			Line line = lineService.selectById(scheduleLoop.getLineId());
			logService.insertLog(OperateLogModelEnum.BUS_LOOP.getModule(),"修改班次循环",getRemoteAddr(),"新增班次循环【线路名称："+line.getLineName()+"】",loginUser.getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除班次调度循环配置", notes = "删除班次调度循环配置信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{scheduleLoopIds}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("scheduleLoopIds") List<String> scheduleLoopIds) {
		ELUser loginUser = getLoginUser();
		List<ScheduleLoop> scheduleLoopList = scheduleLoopService.selectBatchIds(scheduleLoopIds);
		
		Line line = null;
		String lineNames = "";
		for (ScheduleLoop loop : scheduleLoopList) {
			line = lineService.selectById(loop.getLineId());
			lineNames += line.getLineName()+",";
		}
		lineNames = lineNames.substring(0, lineNames.length()-1);
		//添加操作日志
		logService.insertLog(OperateLogModelEnum.BUS_LOOP.getModule(),"删除班次循环",getRemoteAddr(),"删除班次循环【线路名称："+lineNames+"】",loginUser.getId());
		
		boolean isDel = scheduleLoopService.delScheduleLoops(scheduleLoopIds);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看班次调度循环配置详情", notes = "查看班次调度循环配置详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{scheduleLoopId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleLoopId") String scheduleLoopId) {
		ScheduleLoop scheduleLoop = scheduleLoopService.getInfoById(scheduleLoopId);
		return CommonResponse.createCommonResponse(scheduleLoop);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "lineId", required = false) String lineId,
			@RequestParam(value = "startDateBegin", required = false) String startDateBegin,
			@RequestParam(value = "startDateEnd", required = false) String startDateEnd,
			@RequestParam(value = "endDateBegin", required = false) String endDateBegin,
			@RequestParam(value = "endDateEnd", required = false) String endDateEnd) throws Exception {
		ScheduleLoop param = new ScheduleLoop();
		param.setCompanyName(companyName);
		param.setLineId(lineId);
		param.setStartDateBegin(DateHelper.parseDate(startDateBegin));
		param.setStartDateEnd(DateHelper.parseDate(startDateEnd));
		param.setEndDateBegin(DateHelper.parseDate(endDateBegin));
		param.setEndDateEnd(DateHelper.parseDate(endDateEnd));
		List<ScheduleLoop> list = scheduleLoopService.exportData(param);

		OutputStream out = null;
		try {
			String fileName = "班次循环导出.xlsx";
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
			ExcelContext context = ExcelContext.newInstance("excel/ScheduleLoop-config.xml");
			Workbook workbook = null;
			if(list == null || list.isEmpty()) {
				workbook = context.createExcelTemplate("ScheduleLoopExport",null,null);
			}else {
				workbook = context.createExcel("ScheduleLoopExport", list);
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
	
	@ApiOperation(value = "手动触发班次生成任务", notes = "手动触发班次生成任务", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/manualTask", produces = "application/json;charset=UTF-8")
	public CommonResponse manualTask() {
		scheduleBusService.createScheduleTask();
		return CommonResponse.createCommonResponse();
	}
}
