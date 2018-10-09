package com.yxhl.platform.common.web.aop;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.utils.JsonMapper;
import com.yxhl.platform.common.web.annotation.Logined;

/**
 * 判断用户是否登录，不登录不能访问controller。
 * 需要验证的方法增加注解com.yxhl.platform.common.web.annotation.Logined
 * @see com.yxhl.platform.common.web.annotation.Logined
 *
 */
@Component
public class LoginVerifyAspectHandler extends HandlerInterceptorAdapter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		Logined classAnnotation = handlerMethod.getBeanType().getAnnotation(Logined.class);
		Method method = handlerMethod.getMethod();
		Logined methodAnnotation = method.getAnnotation(Logined.class);
		if(classAnnotation != null || methodAnnotation != null){
			String userJson = request.getHeader("el_user_json_str");
			if(StringUtils.isBlank(userJson)){
				writeNotLogin(response);
				return false;
			}

			String elUserInfo = URLDecoder.decode(userJson, "utf-8");
			if(StringUtils.isBlank(elUserInfo) || elUserInfo.length() < 6 || null == JsonMapper.fromJsonString(elUserInfo, ELUser.class).getId()
					|| StringUtils.isBlank(JsonMapper.fromJsonString(elUserInfo, ELUser.class).getId())) {
				writeNotLogin(response);
				return false;
			}
		}
		return super.preHandle(request, response, handler);
	}

	private void writeNotLogin(HttpServletResponse response) throws IOException {
		CommonResponse cr = CommonResponse.createCustomCommonResponse(HttpStatus.UNAUTHORIZED + "", HttpStatus.UNAUTHORIZED.name());
		response.sendError(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().print(JsonMapper.toJsonString(cr));
		logger.warn("用户没有登录，访问非法");
	}
}
