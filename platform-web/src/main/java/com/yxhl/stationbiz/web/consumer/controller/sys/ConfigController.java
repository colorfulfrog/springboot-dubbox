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
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  参数配置表控制器
 *  创建人: xjh
 *  创建日期:2018-7-12 16:14:50
 */
@RestController
@Api(tags = "参数配置表控制器")
@RequestMapping(value = "/m/config/")
public class ConfigController extends BaseController{
	
    @Autowired
    private ConfigService configService;
    
    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Config config) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		config.setOrgId(getLoginUser().getOrgId());
		Page<Config> page = new Page<Config>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Config> result = configService.selPageList(page, config);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有参数配置表", notes = "查询所有参数配置表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Config config) {
		Wrapper<Config> wrapper = new EntityWrapper<Config>();
		if(config.getCode()!=null) {
			wrapper.eq("code", config.getCode());
		}
		List<Config> list = configService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增参数配置表", notes = "新增参数配置表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addConfig(@RequestBody Config config) {
		config.setId(IdWorker.get32UUID());
		config.setCreateBy(getLoginUser().getId());
		config.setOrgId(getLoginUser().getOrgId());
		boolean isAdded = configService.insert(config);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.CONFIG.getModule(),"新增参数配置",getRemoteAddr(),"参数编码【"+config.getCode()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(config);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改参数配置表", notes = "修改参数配置表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{configId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateConfig(@RequestBody Config config,@PathVariable("configId") String configId) {
		config.setId(configId);
		config.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = configService.updateById(config);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.CONFIG.getModule(),"修改参数配置",getRemoteAddr(),"被修改参数编码【"+config.getCode()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除参数配置表", notes = "删除参数配置表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{configId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("configId") List<String> configId) {
		List<Config> list = configService.selectBatchIds(configId);
		boolean isDel = configService.deleteBatchIds(configId);
		if(isDel) {
			Config cg = null;
			String code = null;
			for(Config cog : list) {
				cg = cog;
				code += cg.getCode();
			}
			code = code.substring(0, code.length());
			logService.insertLog(OperateLogModelEnum.CONFIG.getModule(),"删除参数配置",getRemoteAddr(),"被删除参数编码【"+code+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看参数配置表详情", notes = "查看参数配置表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{configId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("configId") String configId) {
		Config config = configService.selectById(configId);
		return CommonResponse.createCommonResponse(config);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "orgId", required = false) String orgId,
			@RequestParam(value = "type", required = false) Integer type,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "remark", required = false) String remark) throws Exception {
		Config param = new Config();
		param.setOrgId(getLoginUser().getOrgId());
		List<Config> list = configService.exportData(param);

		OutputStream out = null;
		try {
			String fileName = "机构导出.xlsx";
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
				workbook = context.createExcelTemplate("ConfigExport",null,null);
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
