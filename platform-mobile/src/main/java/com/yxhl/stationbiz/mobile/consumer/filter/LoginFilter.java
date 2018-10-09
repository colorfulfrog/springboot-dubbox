package com.yxhl.stationbiz.mobile.consumer.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.alibaba.fastjson.JSONObject;
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.redis.util.RedisUtil;

@Order(1)
@WebFilter(urlPatterns="/c/*",filterName="loginFilter")
public class LoginFilter implements Filter {
	private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/c/login","/c/code")));

	@Autowired
	private RedisUtil redisUtil;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException,YxBizException{
		HttpServletRequest reqes = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = reqes.getRequestURI().substring(reqes.getContextPath().length()).replaceAll("[/]+$", "");
		boolean allowedPath = ALLOWED_PATHS.contains(path);
		if (allowedPath) {
			System.out.println("这里是不需要处理的url进入的方法");
			chain.doFilter(reqes, resp);
		} else {
			String token= reqes.getHeader("token");
			boolean flag=true;
			if(!StringUtils.isNotBlank(token)) {
				writeJson(resp, 3, "未登录，请登录");
				flag=false;
			}
			if (flag) {
				ELUser eluser = (ELUser) redisUtil.get("SYS_USER_TOKEN:" + token);
				if (eluser == null) {
					writeJson(resp, 3, "登录过期! 请重新登录");
				}else {
					chain.doFilter(reqes, resp);
				}
			}
		}

	}

	@Override
	public void destroy() {
		
	}
	
	public static void writeJson(HttpServletResponse response,int code,String msg){
		response.setCharacterEncoding("UTF-8");    
		response.setContentType("application/json; charset=utf-8");  
		PrintWriter out = null ;  
		try{  
			JSONObject res = new JSONObject();  
			res.put("success", false);
			res.put("code",code);  
			res.put("message",msg);  
			out = response.getWriter();  
			System.out.println("-------------------------" + res.toString());
			out.append(res.toString());  
			//out.write(res.toString());
		}  
		catch (Exception e){  
			e.printStackTrace();  		   
		}  
	}
}
