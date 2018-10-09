package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.util.ArrayList;
import java.util.List;

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
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;
import com.yxhl.stationbiz.system.domain.entity.sys.User;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;
import com.yxhl.stationbiz.system.domain.service.sys.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "用户信息")
@RequestMapping(value = "/m/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	
    @Autowired
    private RelationService relationService;
    
    @Autowired
	private OperateLogService logService;

	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody User user) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<User> page = new Page<User>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		user.setOrgId(getLoginUser().getOrgId());
		user.setCompanyId(getLoginUser().getCompanyId());
		Page<User> result = userService.selPageList(page, user);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "根据条件查询用户", notes = "根据条件查询用户表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody User user) {
		Wrapper<User> wrapper = new EntityWrapper<User>();
		if(user.getUserName()!=null) {
			wrapper.like("user_name", user.getUserName());
		}
		if(getLoginUser().getCompanyId()!=null) {
			wrapper.eq("company_id", getLoginUser().getCompanyId());
		}
		if(getLoginUser().getOrgId() !=null) {
			wrapper.eq("org_id", getLoginUser().getOrgId());
		}
		List<User> list = userService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation(value="查询用户基本信息",notes = "根据条件查询用户表信息",httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/getUser", produces = "application/json;charset=UTF-8")
	public CommonResponse getUserByRole(@RequestBody User user) {
		if(StringUtils.isNotBlank(getLoginUser().getCompanyId())) {
			user.setCompanyId(getLoginUser().getCompanyId());
		}
		if(StringUtils.isNotBlank(getLoginUser().getOrgId())) {
			user.setOrgId(getLoginUser().getOrgId());
		}
		user.setRoleName("conductor"); //角色：售票员
		List<User> userByRole = userService.getUserByRole(user);
		return CommonResponse.createCommonResponse(userByRole);
	}
	
	@ApiOperation("查询用户基本信息")
	@GetMapping(value = "/{userId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("userId") String userId) {
		User user = userService.selectById(userId);
		Wrapper<Relation> wrapper = new EntityWrapper<Relation>();
		wrapper.where("aid={0} and type={1}", user.getId(), 1);
		List<Relation> list = relationService.selectList(wrapper);//
		List<String> ids=new ArrayList<String>();
		for (Relation relation : list) {
			ids.add(relation.getBid());
		}
		user.setIds(ids);
		return CommonResponse.createCommonResponse(user);
	}
	
	@ApiOperation("新增用户基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addUser(@RequestBody User user) {
		boolean isAdded = userService.insert(user);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.USER.getModule(),"新增用户",getRemoteAddr(),"用户名称【"+user.getUserName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增用户失败!");
		}
	}
	
	@ApiOperation("修改用户基本信息")
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateUser(@RequestBody User user) {
		boolean isAdded = userService.updateById(user);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.USER.getModule(),"修改用户信息",getRemoteAddr(),"修改用户【"+user.getUserName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改用户失败!");
		}
	}
	
	@ApiOperation("删除用户")
	@DeleteMapping(value = "/delete/{userId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("userId") Integer userId) {
		boolean isDel = userService.deleteById(userId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除用户失败!");
		}
	}
	
	@ApiOperation("新增用户基本信息")
	@PostMapping(value = "/userAdd", produces = "application/json;charset=UTF-8")
	public CommonResponse userAdd(@RequestBody User user) {
		Wrapper<User> wrapper = new EntityWrapper<User>();
		wrapper.eq("user_code", user.getUserCode());
		List<User> list=userService.selectList(wrapper);
		if(list.size()>0) {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败用户编码:"+user.getUserCode()+"已存在");
		}
		user.setCreateBy(getLoginUser().getId());
		user.setUpdateBy(getLoginUser().getId());
		boolean isAdded = userService.userAdd(user);
		if(isAdded) {
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增用户失败!");
		}
	}
	
	@ApiOperation("修改用户基本信息")
	@PutMapping(value = "/userUpdate", produces = "application/json;charset=UTF-8")
	public CommonResponse userUpdate(@RequestBody User user) {
		user.setUpdateBy(getLoginUser().getId());
		boolean isAdded = userService.userUpdate(user);
		if(isAdded) {
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增用户失败!");
		}
	}
	
	@ApiOperation("删除用户")
	@DeleteMapping(value = "/deleteUser/{userId}", produces = "application/json;charset=UTF-8")
	public CommonResponse deleteUser(@PathVariable("userId") List<String> userId) {
		List<User> users = userService.selectBatchIds(userId);
		User user = null;
		String userNames = null;
		for (User uid : users) {
			user = uid;
			userNames += user.getUserName()+",";
		}
		userNames = userNames.substring(0, userNames.length());
		boolean isDel = userService.userDelete(userId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.USER.getModule(),"删除用户",getRemoteAddr(),"删除用户【"+userNames+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除用户失败!");
		}
	}
}