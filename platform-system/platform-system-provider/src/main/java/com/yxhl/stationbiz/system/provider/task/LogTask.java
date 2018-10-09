package com.yxhl.stationbiz.system.provider.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.stationbiz.system.domain.entity.sys.OperateLog;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;

/**
 * @author xjh
 * @title
 * @date 2018年7月11日 下午4:57:48
 */
public class LogTask implements Runnable {
	/**
	 * 日志对象
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(LogTask.class);
	
	private OperateLogService logService;
	private OperateLog operateLog;
	
	public LogTask (OperateLogService logService,OperateLog log) {
		this.logService = logService;
		this.operateLog = log;
	}
	
	@Override
	public void run() {
		try {
			operateLog.setId(IdWorker.get32UUID());
			logService.insert(operateLog);
		} catch (Exception e) {
			LOGGER.error("====添加操作日志失败=========", operateLog);
		}
	}

	public OperateLogService getLogService() {
		return logService;
	}

	public void setLogService(OperateLogService logService) {
		this.logService = logService;
	}

	public OperateLog getOperateLog() {
		return operateLog;
	}

	public void setOperateLog(OperateLog operateLog) {
		this.operateLog = operateLog;
	}
}
