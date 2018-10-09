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
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  站点表控制器
 *  创建人: xjh
 *  创建日期:2018-7-12 11:45:24
 */
@RestController
@Api(tags = "站点表控制器")
@RequestMapping(value = "/m/station/")
public class StationController extends BaseController{
	
    @Autowired
    private StationService stationService;
    
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Station station) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		station.setOrgId(getLoginUser().getOrgId());
		Page<Station> page = new Page<Station>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Station> result = stationService.selPageList(page, station);
		resp.setData(result);
	    List<Dictionary> level = dictionaryService.getRediesByKey("station_level"); //查询车站级别 字典
	    List<Dictionary> flag = dictionaryService.getRediesByKey("service_station_flag"); //查询是否是快递服务站字典
	    resp.extendsRes("level", level);
	    resp.extendsRes("flag", flag);
	    return resp;
	}
	
	@ApiOperation(value = "查询所有站点表", notes = "查询所有站点表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Station station) {
		Wrapper<Station> wrapper = new EntityWrapper<Station>();
		if(StringUtils.isNotBlank(station.getSpell())) {
			wrapper.like("spell", station.getSpell());
		}
		if(StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		if(StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		List<Station> list = stationService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增站点表", notes = "新增站点表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addStation(@RequestBody Station station) {
		station.setCreateBy(getLoginUser().getId());
		station.setId(IdWorker.get32UUID());
		station.setOrgId(getLoginUser().getOrgId());
		station.setCompId(getLoginUser().getCompanyId());
		boolean isAdded = stationService.add(station);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.STATION.getModule(),"新增站点",getRemoteAddr(),"站点名称【"+station.getStationName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(station);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改站点表", notes = "修改站点表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{stationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateStation(@RequestBody Station station,@PathVariable("stationId") String stationId) {
		station.setId(stationId);
		station.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = stationService.updateSta(station);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.STATION.getModule(),"修改站点",getRemoteAddr(),"被修改站点名称【"+station.getStationName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除站点表", notes = "删除站点表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{stationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("stationId") List<String> stationId) {
		List<Station> list = stationService.selectBatchIds(stationId);
		Station st = null;
		String staName = null;
		for(Station sto : list) {
			st = sto;
			staName += st.getStationName();
		}
		staName = staName.substring(0, staName.length());
		boolean isDel = stationService.deleteBatchIds(stationId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.STATION.getModule(),"删除站点",getRemoteAddr(),"被删除站点名称【"+staName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看站点表详情", notes = "查看站点表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{stationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("stationId") String stationId) {
		Station station = stationService.selectById(stationId);
		return CommonResponse.createCommonResponse(station);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "stationName", required = false) String stationName,
			@RequestParam(value = "regionCode", required = false) String regionCode,
			@RequestParam(value = "oprCode", required = false) String oprCode,
			@RequestParam(value = "spell", required = false) String spell,
			@RequestParam(value = "boardPointFlag", required = false) Integer boardPointFlag) throws Exception {
		Station param = new Station();
		param.setOrgId(getLoginUser().getOrgId());
		List<Station> list = stationService.exportData(param);
		OutputStream out = null;
		try {
			String fileName = "站点导出.xlsx";
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
				workbook = context.createExcelTemplate("StationExport",null,null);
			}else {
				workbook = context.createExcel("StationExport", list);
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
