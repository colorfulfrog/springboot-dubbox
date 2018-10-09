package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.redis.util.RedisUtil;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.User;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.UserService;
import com.yxhl.stationbiz.web.consumer.util.VerifyCodeUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "登录")
@RequestMapping(value = "/m")
public class LoginController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private RedisUtil redisUtil;

    @Autowired
	private OperateLogService logService;
	@ApiOperation("登录")
	@PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
	public CommonResponse login(@RequestBody User user) {
		if(!StringUtils.isNotBlank(user.getUserCode().toString()) || !StringUtils.isNotBlank(user.getUserPwd().toString())){
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "用户名或密码不能为空");
        }
		User user1 = userService.userLogin(user.getUserCode(),user.getUserPwd());
		logService.insertLog(OperateLogModelEnum.USER.getModule(),"用户登录",getRemoteAddr(),"登录用户【"+user1.getUserName()+"】",user1.getId());
		return CommonResponse.createCommonResponse(user1);
	}
	
	@ApiOperation("生成验证码")
	@GetMapping(value = "/code", produces = "application/json;charset=UTF-8")
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "No-cache");  
		response.setHeader("Cache-Control", "no-cache");  
		response.setDateHeader("Expires", 0);  
		response.setContentType("image/jpeg");  
          
        //生成随机字串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);  
        redisUtil.set("SYS_VERIFY_CODE:"+verifyCode, verifyCode, 60L);
        
        //生成图片  
        VerifyCodeUtils.outputImage(100, 40, response.getOutputStream(), verifyCode);
	}
}