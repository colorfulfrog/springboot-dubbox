package com.yxhl.stationbiz.mobile.consumer;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer { 
	  
    @Override 
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) { 
        return application.sources(MobileConsumerApplication.class); 
    } 
} 
