package com.yxhl.stationbiz.system.provider.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;

/**
 * 定时任务-班次循环
 * @author lw
 */
@Component
public class ScheduleBusTask {
	/**
	 * 日志对象
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleBusTask.class);
	
	@Autowired
	private ScheduleBusService scheduleBusService;
	
	/**
	 * 每天0点01分启动定时任务
	 */
	@Scheduled(cron="0 1 0 * * ?")
	public void run() {
		LOGGER.info("=================开始执行班次循环生成班次任务=====================");
		scheduleBusService.createScheduleTask();
		LOGGER.info("=================结束执行班次循环生成班次任务=====================");
	}
	
	/**
	 * 测试方法，每分钟执行一次
	 */
	/*@Scheduled(cron="0 0/10 * * * ?")
	public void runTest() {
		LOGGER.info("=================【测试】开始执行班次循环生成班次任务=====================");
		scheduleBusService.createScheduleTask();
		LOGGER.info("=================【测试】结束执行班次循环生成班次任务=====================");
	}*/
}
