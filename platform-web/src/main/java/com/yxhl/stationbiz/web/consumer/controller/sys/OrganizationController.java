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
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.entity.sys.Organization;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.OrganizationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  机构表控制器
 *  创建人: xjh
 *  创建日期:2018-7-12 15:58:15
 */
@RestController
@Api(tags = "机构表控制器")
@RequestMapping(value = "/m/organization/")
public class OrganizationController extends BaseController{
	
    @Autowired
    private OrganizationService organizationService;
    
    @Autowired
	private OperateLogService logService;
    @Autowired
    private ConfigService configService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Organization organization) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Organization> page = new Page<Organization>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Organization> result = organizationService.selPageList(page, organization);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有机构表", notes = "查询所有机构表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Organization organization) {
		Wrapper<Organization> wrapper = new EntityWrapper<Organization>();
		if(StringUtils.isNotBlank(organization.getShortSpell())) {
			wrapper.like("short_spell", organization.getShortSpell());
		}
		if(StringUtils.isNoneBlank(getLoginUser().getOrgId())) {
			wrapper.eq("id", getLoginUser().getOrgId());
		}
		List<Organization> list = organizationService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增机构表", notes = "新增机构表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addOrganization(@RequestBody Organization organization) {
		ELUser loginUser = getLoginUser();
		String orgId = IdWorker.get32UUID();
		organization.setId(orgId);
		organization.setCreateBy(loginUser.getId());
		organization.setUpdateBy(loginUser.getId());
		boolean isAdded = organizationService.insert(organization);
		if(isAdded) {
			//查询参数模板
			Wrapper<Config> wrapper = new EntityWrapper<Config>();
			wrapper.where("org_id={0}", "1");
			List<Config> configList = configService.selectList(wrapper);
			for (Config config : configList) {
				config.setId(null);
				config.setOrgId(orgId);
				config.setCreateBy(loginUser.getId());
				config.setUpdateBy(loginUser.getId());
				config.setCreateTime(null);
				config.setUpdateTime(null);
			}
			configService.insertBatch(configList); //添加参数
			logService.insertLog(OperateLogModelEnum.ORGANIZATION.getModule(),"新增机构",getRemoteAddr(),"机构名称【"+organization.getFullName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(organization);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改机构表", notes = "修改机构表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{organizationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateOrganization(@RequestBody Organization organization,@PathVariable("organizationId") String organizationId) {
		organization.setId(organizationId);
		organization.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = organizationService.updateById(organization);
		if(isUpdated) {
			logService.insertLog(OperateLogModelEnum.ORGANIZATION.getModule(),"修改机构",getRemoteAddr(),"被修改机构名称【"+organization.getFullName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除机构表", notes = "删除机构表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{organizationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("organizationId") List<String> organizationId) {
		List<Organization> list = organizationService.selectBatchIds(organizationId);
		Organization on = null;
		String onName = null;
		for(Organization orn : list) {
			on = orn;
			onName += on.getFullName();
		}
		onName = onName.substring(0, onName.length());
		boolean isDel = organizationService.deleteBatchIds(organizationId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.ORGANIZATION.getModule(),"删除机构",getRemoteAddr(),"被删除机构名称【"+onName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看机构表详情", notes = "查看机构表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{organizationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("organizationId") String organizationId) {
		Organization organization = organizationService.selectById(organizationId);
		return CommonResponse.createCommonResponse(organization);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "shortName", required = false) String shortName,
			@RequestParam(value = "orgCode", required = false) String orgCode) throws Exception {
		Organization param = new Organization();
		List<Organization> list = organizationService.exportData(param);

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
				workbook = context.createExcelTemplate("OrganizationExport",null,null);
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
