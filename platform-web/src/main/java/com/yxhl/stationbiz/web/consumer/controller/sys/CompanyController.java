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
import com.yxhl.platform.common.utils.DateHelper;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;
import com.yxhl.stationbiz.system.domain.entity.schedule.TicketCategory;
import com.yxhl.stationbiz.system.domain.entity.sys.Company;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.schedule.TicketCategoryService;
import com.yxhl.stationbiz.system.domain.service.sys.CompanyService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "单位信息")
@RequestMapping(value = "/m/company")
public class CompanyController extends BaseController {
	@Autowired
	private CompanyService companyService;

    @Autowired
	private OperateLogService logService;
    
    @Autowired
    private TicketCategoryService ticketCategoryService;
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Company company) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Company> page = new Page<Company>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		if (StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			company.setOrgId(getLoginUser().getOrgId());
		}
		Page<Company> result = companyService.selPageList(page, company);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation("查询单位基本信息")
	@GetMapping(value = "/{companyId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("companyId") String companyId) {
		Company company = companyService.selectById(companyId);
		return CommonResponse.createCommonResponse(company);
	}
	
	@ApiOperation("新增单位基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addCompany(@RequestBody Company company) {
		company.setId(IdWorker.get32UUID());
		company.setCreateBy(getLoginUser().getId());
		company.setUpdateBy(getLoginUser().getId());
		boolean isAdded = companyService.insert(company);
		if(isAdded) {
			TicketCategory ticketCategory= new TicketCategory();
			ticketCategory.setId(IdWorker.get32UUID());
			ticketCategory.setCompId(company.getId());
			ticketCategory.setOrgId(company.getOrgId());
			ticketCategory.setTicketCateName("全票");
			ticketCategory.setTicketCateCode("QP");
			ticketCategory.setFieldName("fullPrice");
			ticketCategory.setPrintName("全");
			ticketCategory.setIsDefault("1");
			ticketCategory.setPointFlag("0");
			ticketCategory.setCreateBy(getLoginUser().getId());
			ticketCategory.setUpdateBy(getLoginUser().getId());
			ticketCategoryService.insert(ticketCategory);
			logService.insertLog(OperateLogModelEnum.COMPANY.getModule(),"新增单位",getRemoteAddr(),"单位名称【"+company.getFullName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增单位失败!");
		}
	}
	
	@ApiOperation("修改单位基本信息")
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateCompany(@RequestBody Company company) {
		company.setUpdateBy(getLoginUser().getId());
		boolean isAdded = companyService.updateById(company);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.COMPANY.getModule(),"修改单位信息",getRemoteAddr(),"被修改单位的名称【"+company.getFullName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改单位失败!");
		}
	}
	
	@ApiOperation("删除项目")
	@DeleteMapping(value = "/{companyId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("companyId") String companyId) {
		boolean isDel = companyService.deleteById(companyId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除单位失败!");
		}
	}
	@ApiOperation("删除单位")
	@PostMapping(value = "/delByIds", produces = "application/json;charset=UTF-8")
	public CommonResponse delByIds(@RequestBody List<String> ids) {
		List<Company> companys = companyService.selectBatchIds(ids);
		Company company = null;
		String companyName = null;
		for (Company companyId : companys) {
			company = companyId;
			companyName += company.getFullName()+",";
		}
		companyName = companyName.substring(0, companyName.length());
		boolean isDel= companyService.deleteBatchIds(ids);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.COMPANY.getModule(),"删除单位",getRemoteAddr(),"被删除单位【"+companyName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除单位失败!");
		}
	}
	
	@ApiOperation("查询所有项目")
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Company company) {
		Wrapper<Company> wrapper = new EntityWrapper<Company>();
		ELUser eluser= getLoginUser();
		if(company!=null ) {
			if(eluser.getOrgId()!=null) {
				wrapper.eq("org_id", eluser.getOrgId());
			}
			if(Util.isNotNull(eluser.getCompanyId()!=null)) {
				wrapper.eq("id", eluser.getCompanyId());
			}
			if(company.getShortSpell()!=null) {
				wrapper.like("short_spell", company.getShortSpell());
			}
		}
		List<Company> list = companyService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("角色所对应的单位")
	@PostMapping(value = "/userlist", produces = "application/json;charset=UTF-8")
	public CommonResponse userlist(@RequestBody Company company) {
		Wrapper<Company> wrapper = new EntityWrapper<Company>();
		if(company!=null ) {
			if(company.getOrgId()!=null && !company.getOrgId().equals("")) {
				wrapper.eq("org_id", company.getOrgId());
			}
			if(company.getShortSpell()!=null) {
				wrapper.like("short_spell", company.getShortSpell());
			}
		}
		List<Company> list = companyService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	/**
	 * 导出
	 */
	@ApiOperation(value = "导出", notes = "导出", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "fullName", required = false) String fullName,
			@RequestParam(value = "companyCode", required = false) String companyCode,
			@RequestParam(value = "areaId", required = false) String areaId,
			@RequestParam(value = "shortSpell", required = false) String shortSpell) throws Exception {
		Company param = new Company();
		param.setFullName(fullName);
		param.setCompanyCode(companyCode);
		param.setAreaId(areaId);
		param.setShortSpell(shortSpell);
		List<Company> list = companyService.exportData(param);

		OutputStream out = null;
		try {
			String fileName = "单位循环导出.xlsx";
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
				workbook = context.createExcelTemplate("CompanyExport",null,null);
			}else {
				workbook = context.createExcel("CompanyExport", list);
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