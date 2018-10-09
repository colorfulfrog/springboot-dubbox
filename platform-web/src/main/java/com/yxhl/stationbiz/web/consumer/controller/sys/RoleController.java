package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "角色信息")
@RequestMapping(value = "/m/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;

    @Autowired
	private OperateLogService logService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Role role) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Role> page = new Page<Role>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		if (StringUtils.isBlank(role.getOrgId())) {
			if (StringUtils.isNotBlank(getLoginUser().getOrgId())) {
				role.setOrgId(getLoginUser().getOrgId());
			}
		}
		Page<Role> result = roleService.selPageList(page, role);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation("查询角色基本信息")
	@GetMapping(value = "/{roleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("roleId") String roleId) {
		Role role = roleService.selectById(roleId);
		return CommonResponse.createCommonResponse(role);
	}
	
	@ApiOperation("新增角色基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addRole(@RequestBody Role role) {
		role.setId(IdWorker.get32UUID());
		role.setCreateBy(getLoginUser().getId());
		role.setUpdateBy(getLoginUser().getId());
		boolean isAdded = roleService.insert(role);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.ROLE.getModule(),"新增角色",getRemoteAddr(),"用户名称【"+role.getRoleName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增角色失败!");
		}
	}
	
	@ApiOperation("修改角色基本信息")
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateRole(@RequestBody Role role) {
		role.setUpdateBy(getLoginUser().getId());
		boolean isAdded = roleService.updateById(role);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.ROLE.getModule(),"修改角色信息",getRemoteAddr(),"被操作的角色名称【"+role.getRoleName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改角色失败!");
		}
	}
	
	@ApiOperation("删除角色")
	@DeleteMapping(value = "/{roleId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("roleId") String roleId) {
		boolean isDel = roleService.deleteById(roleId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除角色失败!");
		}
	}
	
	@ApiOperation("删除角色")
	@PostMapping(value = "/delByIds", produces = "application/json;charset=UTF-8")
	public CommonResponse delByIds(@RequestBody List<String> ids) {
		List<Role> roles = roleService.selectBatchIds(ids);
		Role role = null;
		String roleName = null;
		for (Role rid : roles) {
			role = rid;
			roleName += role.getRoleName()+",";
		}
		roleName = roleName.substring(0, roleName.length());
		boolean isDel= roleService.deleteBatchIds(ids);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.ROLE.getModule(),"删除角色",getRemoteAddr(),"所删除的角色【"+roleName+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除角色失败!");
		}
	}
	
	@ApiOperation("查询所有项目")
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Role role) {
		Wrapper<Role> wrapper = new EntityWrapper<Role>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<Role> list = roleService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("分页查询")
	@PostMapping(value = "/roleList", produces = "application/json;charset=UTF-8")
	public CommonResponse roleList(@RequestBody Role role) {
		if (StringUtils.isBlank(role.getOrgId())) {
			if (StringUtils.isNotBlank(getLoginUser().getOrgId())) {
				role.setOrgId(getLoginUser().getOrgId());
			}
		}
		return CommonResponse.createCommonResponse(roleService.userList(role));
	}
}