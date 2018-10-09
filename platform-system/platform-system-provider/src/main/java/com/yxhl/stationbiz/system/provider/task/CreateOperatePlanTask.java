package com.yxhl.stationbiz.system.provider.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yxhl.stationbiz.system.domain.request.CreateScheduleBusRequest;
import com.yxhl.stationbiz.system.domain.service.schedule.ScheduleBusService;

/**
 * 生成制作计划任务
 */
public class CreateOperatePlanTask implements Runnable {
	/**
	 * 日志对象
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(CreateOperatePlanTask.class);
	
	private ScheduleBusService scheduleBusService;
	private CreateScheduleBusRequest req;
	
	public CreateOperatePlanTask (ScheduleBusService scheduleBusService,CreateScheduleBusRequest req) {
		this.scheduleBusService = scheduleBusService;
		this.req = req;
	}
	
	@Override
	public void run() {
		try {
			scheduleBusService.createOperatePlan(req);
		} catch (Exception e) {
			LOGGER.error("====执行制作营运计划任务失败=========", req);
		}
	}

	public ScheduleBusService getScheduleBusService() {
		return scheduleBusService;
	}

	public void setScheduleBusService(ScheduleBusService scheduleBusService) {
		this.scheduleBusService = scheduleBusService;
	}

	public CreateScheduleBusRequest getReq() {
		return req;
	}

	public void setReq(CreateScheduleBusRequest req) {
		this.req = req;
	}
}
