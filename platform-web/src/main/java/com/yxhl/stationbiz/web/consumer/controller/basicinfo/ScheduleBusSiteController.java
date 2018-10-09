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
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusSite;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusSiteService;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;


/**
 *	
 *  班次停靠点控制器
 *  创建人: lw
 *  创建日期:2018-7-12 19:15:10
 */
@RestController
@Api(tags = "班次停靠点控制器")
@RequestMapping(value = "/m/scheduleBusSite/")
public class ScheduleBusSiteController extends BaseController{
	
    @Autowired
    private ScheduleBusSiteService scheduleBusSiteService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody ScheduleBusSite scheduleBusSite) {
		checkNotNull(scheduleBusSite.getScheduleBusTplId(),"查询参数：Error:ScheduleBusId 不允许为空");
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<ScheduleBusSite> page = new Page<ScheduleBusSite>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBusSite> result = scheduleBusSiteService.selPageList(page, scheduleBusSite);
		return CommonResponse.createCommonResponse(result);
	}
	
	/**
	 * 根据班次模板id，站点名称查停靠点
	 * @param st
	 * @return
	 */
	@ApiOperation(value = "查询所有班次停靠点", notes = "查询所有班次停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/staList", produces = "application/json;charset=UTF-8")
	public CommonResponse staList(@RequestBody ScheduleBusSite scheduleBusSite) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			scheduleBusSite.setOrgId(loginUser.getOrgId());
			scheduleBusSite.setCompId(loginUser.getCompanyId());
		}
		List<ScheduleBusSite> station = scheduleBusSiteService.getStation(scheduleBusSite);
		return CommonResponse.createCommonResponse(station);
	}
	
	@ApiOperation(value = "查询所有班次停靠点", notes = "查询所有班次停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody ScheduleBusSite scheduleBusSite) {
		Wrapper<ScheduleBusSite> wrapper = new EntityWrapper<ScheduleBusSite>();
		checkNotNull(scheduleBusSite.getScheduleBusTplId(),"查询参数：Error:scheduleBusTplId 不允许为空");
		if(getLoginUser().getCompanyId()!=null) {
			wrapper.eq("comp_id", getLoginUser().getCompanyId());
		}
		if(getLoginUser().getOrgId() !=null) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		wrapper.eq("schedule_bus_tpl_id", scheduleBusSite.getScheduleBusTplId());
		//wrapper.where("id={0} and create_by={1}", 9,1);
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<ScheduleBusSite> list = scheduleBusSiteService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增班次停靠点", notes = "新增班次停靠点信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleBusSite(@RequestBody ScheduleBusSite scheduleBusSite) {
		checkNotNull(scheduleBusSite,"参数错误");
		scheduleBusSite.setId(IdWorker.get32UUID());
		ELUser user= this.getLoginUser();
		scheduleBusSite.setCreateBy(user.getId());
		scheduleBusSite.setOrgId(user.getOrgId());
		scheduleBusSite.setCompId(user.getCompanyId());
		scheduleBusSite.setUpdateBy(user.getId()); 
		checkArgument(StringUtils.isNotEmpty(scheduleBusSite.getScheduleBusTplId()),"新增失败：Error:scheduleBusTplId 不允许为空 ");
		checkArgument(StringUtils.isNotEmpty(scheduleBusSite.getStationId()),"新增失败：Error:stationId 不允许为空 ");
		boolean isAdded = scheduleBusSiteService.insert(scheduleBusSite);
		if(isAdded) {
			return CommonResponse.createCommonResponse(scheduleBusSite);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次停靠点", notes = "修改班次停靠点信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{scheduleBusSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBusSite(@PathVariable("scheduleBusSiteId")String scheduleBusSiteId ,@RequestBody ScheduleBusSite scheduleBusSite) {
		checkNotNull(scheduleBusSite,"参数错误");
		checkNotNull(scheduleBusSiteId,"scheduleBusSiteId不能为空");
		ELUser user= this.getLoginUser();
		scheduleBusSite.setId(scheduleBusSiteId);
		scheduleBusSite.setUpdateBy(user.getId()); 
		boolean isUpdated = scheduleBusSiteService.updateById(scheduleBusSite);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除班次停靠点", notes = "删除班次停靠点信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{scheduleBusSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("scheduleBusSiteId") List<String> scheduleBusSiteId) {
		boolean isDel = scheduleBusSiteService.deleteBatchIds(scheduleBusSiteId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}
	
	

	@ApiOperation(value = "查看班次停靠点详情", notes = "查看班次停靠点详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{scheduleBusSiteId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleBusSiteId") String scheduleBusSiteId) {
		ScheduleBusSite scheduleBusSite = scheduleBusSiteService.selOne(scheduleBusSiteId);
		return CommonResponse.createCommonResponse(scheduleBusSite);
	}
	
	@ApiOperation(value = "班次停靠点排序", notes = "班次停靠点排序信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/sort/{busSiteId}/{sortType}", produces = "application/json;charset=UTF-8")
	public CommonResponse sort(@PathVariable("busSiteId") String busSiteId,@PathVariable("sortType") String sortType) {
		checkNotNull(busSiteId,"操作失败：busSiteId 不能为空");
		if(StringUtils.isEmpty(sortType)||(!sortType.equals("up")&&!sortType.equals("down"))) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "操作失败：sortType 值必须为：'up' or 'down'");
		}
		scheduleBusSiteService.sort(busSiteId, sortType);
		return CommonResponse.createCommonResponse();
	}
}
