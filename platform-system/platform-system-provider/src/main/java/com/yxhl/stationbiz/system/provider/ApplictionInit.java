package com.yxhl.stationbiz.system.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yxhl.platform.common.redis.util.RedisUtil;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;

/**
 * 项目启动完成后执行
 * @author lw
 *
 */
@Component
@Order(1)
public class ApplictionInit implements ApplicationRunner {
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		
	}

}
