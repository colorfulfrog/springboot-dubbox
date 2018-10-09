package com.yxhl.stationbiz.system.provider;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * War包入口
 * @author lw
 *
 */
public class ServletInitializer extends SpringBootServletInitializer { 
	  
    @Override 
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 
        return application.sources(SysProviderApplication.class); 
    } 
} 
