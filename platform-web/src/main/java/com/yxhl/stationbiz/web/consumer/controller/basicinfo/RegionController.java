package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Region;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.RegionService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "区域信息")
@RequestMapping(value = "/m/region")
public class RegionController extends BaseController {
	@Autowired
	private RegionService regionService;

    @Autowired
	private OperateLogService logService;
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Region region) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Region> page = new Page<Region>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Region> result = regionService.selPageList(page, region);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation("查询区域基本信息")
	@GetMapping(value = "/{regId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("regId") String regId) {
		Region region = regionService.selectById(regId);
		if(region.getParentId()!=null) {
			Region region1= regionService.selectById(region.getParentId());
			region.setParentName(region1.getRegionName());
		}
		return CommonResponse.createCommonResponse(region);
		
	}
	
	@ApiOperation("新增区域基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addRegion(@RequestBody Region region) {
		Wrapper<Region> wrapper = new EntityWrapper<Region>();
		wrapper.eq("region_code", region.getRegionCode());
		List<Region> list = regionService.selectList(wrapper);
		if(list.size()>0) {
			throw new YxBizException("编码已存在");
		}
		region.setId(IdWorker.get32UUID());
		region.setCreateBy(getLoginUser().getId());
		region.setUpdateBy(getLoginUser().getId());
		boolean isAdded = regionService.insert(region);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.REGION.getModule(),"新增区域",getRemoteAddr(),"区域名称【"+region.getRegionName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增区域失败!");
		}
	}
	
	@ApiOperation("修改区域基本信息")
	@PutMapping(value = "/update/{regionId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateRegion(@RequestBody Region region,@PathVariable("regionId") String regionId) {
		region.setUpdateBy(getLoginUser().getId());
		region.setRegionCode(null);
		region.setId(regionId);
		boolean isAdded = regionService.updateById(region);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.REGION.getModule(),"修改区域",getRemoteAddr(),"被修改的区域名称【"+region.getRegionName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改区域失败!");
		}
	}
	
	@ApiOperation("删除区域信息")
	@DeleteMapping(value = "/delete/{regId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("regId") List<String> regId) {
		List<Region> regions = regionService.selectBatchIds(regId);
		Region region = null;
		String regionName = null;
		for (Region regionId : regions) {
			region = regionId;
			regionName += region.getRegionName()+",";
		}
		regionName = regionName.substring(0, regionName.length());
		boolean isDel = regionService.deleteBatchIds(regId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.REGION.getModule(),"删除区域",getRemoteAddr(),"被删除的区域名称【"+regionName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除区域失败!");
		}
	}
	@ApiOperation("查询所有项目")
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("spell") String spell) {
		Wrapper<Region> wrapper = new EntityWrapper<Region>();
		if(StringUtils.isNotBlank(spell)) {
			wrapper.like("spell", spell);
		}
		//wrapper.where("id={0} and create_by={1}", 9,1);
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<Region> list = regionService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
}