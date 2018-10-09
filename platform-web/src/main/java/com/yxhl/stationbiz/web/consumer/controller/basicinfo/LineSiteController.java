package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.Util;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.schedule.Holiday;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineSiteService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *	
 *  线路停靠点控制器
 *  创建人: lw
 *  创建日期:2018-7-11 9:56:16
 */
@RestController
@Api(tags = "线路停靠点控制器")
@RequestMapping(value = "/m/lineSite/")
public class LineSiteController extends BaseController{
	
    @Autowired
    private LineSiteService lineSiteService;
    
    @Autowired
    private LineService lineService;
    
    @Autowired
    private StationService stationService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody LineSite lineSite) {
		checkNotNull(lineSite,"参数错误");
		checkNotNull(lineSite.getLineId(),"查询参数：Error:lineId 不允许为空");
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser user=getLoginUser();
		if(Util.isNotNull(user.getOrgId())) {
			lineSite.setOrgId(user.getOrgId());
		}
		if(Util.isNotNull(user.getCompanyId())) {
			lineSite.setCompId(user.getCompanyId());
		}
		Page<LineSite> page = new Page<LineSite>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<LineSite> result = lineSiteService.selPageList(page, lineSite);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有线路停靠点", notes = "查询所有线路停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody LineSite lineSite) {
		checkNotNull(lineSite,"参数错误");
		checkNotNull(lineSite.getLineId(),"查询参数：Error:lineId 不允许为空");
		List<LineSite> list = lineSiteService.selList(lineSite);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增线路停靠点", notes = "新增线路停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addLineSite(@RequestBody LineSite lineSite) {
		checkNotNull(lineSite,"参数错误");
		checkNotNull(lineSite.getLineId(),"lineId 不能为空");
		checkNotNull(lineSite.getStationId(),"stationId 不能为空");
		lineSite.setId(IdWorker.get32UUID());
		ELUser user= this.getLoginUser();
		lineSite.setCreateBy(user.getId());
		lineSite.setUpdateBy(user.getId()); 
		Station station= stationService.selectById(lineSite.getStationId());
		checkNotNull(station,"操作失败，stationId 必属的站点不存在");
		lineSite.setOrgId(user.getOrgId());
		lineSite.setCompId(user.getCompanyId()); 
		lineSite.setStatus(0); //正常
		lineSite.setContacts(station.getContacts());
		lineSite.setTelephone(station.getTelephone());
		lineSite.setSpell(station.getSpell()); 

		checkArgument(StringUtils.isNotEmpty(lineSite.getLineId()),"新增失败：Error:lineId 不允许为空 ");
		Wrapper<LineSite> wrapper = new EntityWrapper<LineSite>();
		wrapper.eq("station_id", lineSite.getStationId());
		wrapper.eq("line_id", lineSite.getLineId());
		LineSite old=lineSiteService.selectOne(wrapper);
		if(null!=old) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "该站点已存在，不允许重复添加");
		}
		Wrapper<LineSite> siteWrapper=new EntityWrapper<LineSite>();
		siteWrapper.eq("line_id", lineSite.getLineId());
		siteWrapper.orderBy("sort");
		List<LineSite> lineSiteList= lineSiteService.selectList(siteWrapper);
		if(CollectionUtils.isNotEmpty(lineSiteList)) {
			Line line= lineService.selById(lineSite.getLineId());
			if(StringUtils.equals(line.getEndStateId(), lineSite.getStationId())) {
				int size= lineSiteList.size();
				LineSite maxSortSite= lineSiteList.get(size-1);
				lineSite.setSort(maxSortSite.getSort()+1);
			}else if(StringUtils.equals(line.getStartStateId(), lineSite.getStationId())) {
				LineSite minSortSite= lineSiteList.get(0);
				lineSite.setSort(minSortSite.getSort()-1);
			}else {
				int size= lineSiteList.size();
				LineSite maxSortSite= lineSiteList.get(size-1);
				lineSite.setSort(maxSortSite.getSort());
				maxSortSite.setSort(maxSortSite.getSort()+1);
				lineSiteService.updateById(maxSortSite);
			}
		}
		boolean isAdded = lineSiteService.insert(lineSite);
		if(isAdded) {
			return CommonResponse.createCommonResponse(lineSite);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改线路停靠点", notes = "修改线路停靠点信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{lineSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateLineSite(@PathVariable("lineSiteId") String lineSiteId,@RequestBody LineSite lineSite) {
		checkNotNull(lineSite,"参数错误");
		lineSite.setId(lineSiteId); 
		ELUser user= this.getLoginUser();
		lineSite.setUpdateBy(user.getId()); 
		
		Station station= stationService.selectById(lineSite.getStationId());
		LineSite oldLineSite= lineSiteService.selectById(lineSiteId);
		
		Wrapper<LineSite> wrapper = new EntityWrapper<LineSite>();
		wrapper.eq("line_id", lineSite.getLineId());
		wrapper.eq("station_id", lineSite.getStationId());
		LineSite site=lineSiteService.selectOne(wrapper);
		checkArgument(!(Util.isNotNull(site)&&!StringUtils.equals(oldLineSite.getId(), site.getId())),"该停靠点的站点已经存在，不允许重复站点");
		checkNotNull(station,"操作失败，stationId 所属的站点不存在");
		lineSite.setOrgId(user.getOrgId());
		lineSite.setCompId(user.getCompanyId()); 
		lineSite.setStatus(0); //正常
		lineSite.setContacts(station.getContacts());
		lineSite.setTelephone(station.getTelephone());
		lineSite.setSpell(station.getSpell()); 
		
		boolean isUpdated = lineSiteService.updateById(lineSite);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	
	
	@ApiOperation(value = "删除线路停靠点", notes = "删除线路停靠点信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{lineSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("lineSiteId") List<String> lineSiteId) {
		boolean isDel = lineSiteService.deleteBatchIds(lineSiteId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看线路停靠点详情", notes = "查看线路停靠点详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{lineSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("lineSiteId") String lineSiteId) {
		LineSite lineSite = lineSiteService.selOne(lineSiteId);
		return CommonResponse.createCommonResponse(lineSite);
	}
	
	
	@ApiOperation(value = "排序线路停靠点", notes = "排序线路停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/sort/{lineSiteId}/{sortType}", produces = "application/json;charset=UTF-8")
	public CommonResponse sortLineSite(@PathVariable("lineSiteId") String lineSiteId,@PathVariable("sortType") String sortType) {
		checkArgument(StringUtils.isNotEmpty(lineSiteId),"操作失败：lineSiteId 不能为空 ");
		checkArgument(StringUtils.isNotEmpty(sortType),"操作失败：sortType 值必须为：'up' or 'down'");
		if(!sortType.equals("up")&&!sortType.equals("down")) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "操作失败：sortType 值必须为：'up' or 'down'");
		}
		lineSiteService.sort(lineSiteId, sortType);
		return CommonResponse.createCommonResponse();
	}
}
