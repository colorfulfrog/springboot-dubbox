package com.yxhl.stationbiz.web.consumer.controller.basicinfo;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Date;
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
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.schedule.ScheduleBus;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusTplService;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  班次模板控制器
 *  创建人: lw
 *  创建日期:2018-7-11 9:58:54
 */
@RestController
@Api(tags = "班次模板控制器")
@RequestMapping(value = "/m/scheduleBusTpl/")
public class ScheduleBusTplController extends BaseController{
	
    @Autowired
    private ScheduleBusTplService scheduleBusTplService;
    
    @Autowired
    private ScheduleBusService scheduleBusService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam(value="currentPage",required=false) String currentPage,@RequestParam(value="pageSize",required=false)String pageSize,@RequestBody ScheduleBusTpl scheduleBusTpl) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		ELUser user= this.getLoginUser();
		if(Util.isNotNull(user.getOrgId())) {
			scheduleBusTpl.setOrgId(user.getOrgId());
		}
		//如果为空，则默认查询当前用户所属单位的班次
		if(!Util.isNotNull(scheduleBusTpl.getCompId())) {
			scheduleBusTpl.setCompId(user.getCompanyId());
		}
		Page<ScheduleBusTpl> page = new Page<ScheduleBusTpl>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<ScheduleBusTpl> result = scheduleBusTplService.selPageList(page, scheduleBusTpl);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有班次模板", notes = "查询所有班次模板信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody ScheduleBusTpl scheduleBusTpl) {
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.eq("line_id", scheduleBusTpl.getLineId());
		ELUser user= this.getLoginUser();
		if(Util.isNotNull(user.getOrgId())) {
			wrapper.eq("org_id", user.getOrgId());
		}
		if(Util.isNotNull(user.getCompanyId())) {
			wrapper.eq("comp_id", user.getCompanyId());
		}
		if(scheduleBusTpl.getOvertimeBusFlag() != null) {
			wrapper.eq("overtime_bus_flag", scheduleBusTpl.getOvertimeBusFlag());
		}
		List<ScheduleBusTpl> list = scheduleBusTplService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增班次模板", notes = "新增班次模板信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addScheduleBusTpl(@RequestBody ScheduleBusTpl scheduleBusTpl) {
		checkNotNull(scheduleBusTpl,"参数错误");
		ELUser user= this.getLoginUser();
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.where("bus_code={0} and org_id={1}", scheduleBusTpl.getBusCode(),user.getOrgId());
		List<ScheduleBusTpl> list = scheduleBusTplService.selectList(wrapper);
		checkArgument(!CollectionUtils.isNotEmpty(list), "班次号重复");
		
		if(scheduleBusTpl.getRunFlowFlag() != null && scheduleBusTpl.getRunFlowFlag() == 1) {
			Wrapper<ScheduleBusTpl> flowWrapper = new EntityWrapper<ScheduleBusTpl>();
			flowWrapper.where("line_id={0} and run_flow_flag={1}", scheduleBusTpl.getLineId(),1);
			List<ScheduleBusTpl> flowList = scheduleBusTplService.selectList(flowWrapper);
			checkArgument(!CollectionUtils.isNotEmpty(flowList), "一条线路不能添加多个流水班班次");
		}
		
		scheduleBusTpl.setId(IdWorker.get32UUID());
		
		scheduleBusTpl.setCreateBy(user.getId());
		scheduleBusTpl.setUpdateBy(user.getId()); 
		scheduleBusTpl.setOrgId(user.getOrgId());
		scheduleBusTpl.setCompId(user.getCompanyId()); 
		boolean isAdded = scheduleBusTplService.addScheduleBusTpl(scheduleBusTpl);
		if(isAdded) {
			return CommonResponse.createCommonResponse(scheduleBusTpl);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改班次模板", notes = "修改班次模板信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{scheduleBusTplId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateScheduleBusTpl(@PathVariable("scheduleBusTplId") String scheduleBusTplId, @RequestBody ScheduleBusTpl scheduleBusTpl) {
		checkNotNull(scheduleBusTpl,"参数错误");
		ELUser user= this.getLoginUser();
		
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.where("bus_code={0} and id <> {1} and org_id={2}", scheduleBusTpl.getBusCode(),scheduleBusTplId,user.getOrgId());
		List<ScheduleBusTpl> list = scheduleBusTplService.selectList(wrapper );
		checkArgument(!CollectionUtils.isNotEmpty(list), "班次号重复");
		
		scheduleBusTpl.setId(scheduleBusTplId); 
		scheduleBusTpl.setUpdateBy(user.getId()); 
		boolean isUpdated = scheduleBusTplService.updateScheduleBusTpl(scheduleBusTpl);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除班次模板", notes = "删除班次模板信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{scheduleBusTplId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("scheduleBusTplId") List<String> scheduleBusTplId) {
		Wrapper<ScheduleBus> wrapper = new EntityWrapper<ScheduleBus>();
		List<String> tplIds=new ArrayList<String>();
		StringBuffer sb=new StringBuffer("操作成功");
		for(String tplId:scheduleBusTplId) {
			wrapper.eq("schedule_tpl_id", tplId);
			wrapper.eq("run_date", new Date());
			int count=scheduleBusService.selectCount(wrapper);//如果存在已报班，则过滤掉不允许删除
			if(count==0) {
				tplIds.add(tplId);
			}else {
				ScheduleBusTpl tplBean=scheduleBusTplService.selOne(tplId);
				sb.append(";班次号："+tplBean.getBusCode()+",发车时间："+tplBean.getRunTime()+"已生成班次，不允许删除");
			}
		}
		boolean isDel=true;
		if(tplIds.size()>0) {
			isDel=scheduleBusTplService.delScheduleBusTpl(tplIds);
		}
		if(isDel) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.SUCCESS_CODE, sb.toString());
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看班次模板详情", notes = "查看班次模板详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{scheduleBusTplId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("scheduleBusTplId") String scheduleBusTplId) {
		ScheduleBusTpl scheduleBusTpl = scheduleBusTplService.selOne(scheduleBusTplId);
		return CommonResponse.createCommonResponse(scheduleBusTpl);
	}
	
	@ApiOperation(value = "根据线路ID查询发班时间", notes = "根据线路ID查询发班时间", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/scheduleBusTpl", produces = "application/json;charset=UTF-8")
	public CommonResponse getScheduleBusTplByLineId(@RequestParam("lineId") String lineId) {
		List<ScheduleBusTpl> list = scheduleBusTplService.selScheduleTplByLineId(lineId);
		for (ScheduleBusTpl scheduleBusTpl : list) {
			Integer runFlowFlag = scheduleBusTpl.getRunFlowFlag();
			if(runFlowFlag != null && runFlowFlag == 1) {
				scheduleBusTpl.setStartTime(new Date());
			}
		}
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation(value = "查询线路班次", notes = "查询线路班次", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/line/{lineId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getOtherTplOfLine(@PathVariable("lineId") String lineId) {
		Wrapper<ScheduleBusTpl> wrapper = new EntityWrapper<ScheduleBusTpl>();
		wrapper.where("line_id={0}", lineId);
		wrapper.orderBy("start_time", true);
		List<ScheduleBusTpl> list = scheduleBusTplService.selectList(wrapper);
		
		//过滤掉传入的班次模板
		//Collection<ScheduleBusTpl> filtedList = Collections2.filter(list, t -> !scheduleBusTplId.equalsIgnoreCase(t.getId()));
		return CommonResponse.createCommonResponse(list);
	}
}
