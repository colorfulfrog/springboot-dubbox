package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.platform.common.utils.ThreadManager;
import com.yxhl.stationbiz.system.domain.entity.sys.OperateLog;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.provider.dao.sys.OperateLogDao;
import com.yxhl.stationbiz.system.provider.task.LogTask;
/**
 * @ClassName: OperateLogServiceImpl
 * @Description: 操作日志表 serviceImpl
 * @author xjh
 * @date 2018-7-9 17:20:55
 */
@Transactional(readOnly = true)
@Service("operateLogService")
public class OperateLogServiceImpl extends CrudService<OperateLogDao, OperateLog> implements OperateLogService {
	@Autowired
	private OperateLogDao operateLogDao;

	@Override
	public Page<OperateLog> selPageList(Page<OperateLog> page, OperateLog operateLog) {
		List<OperateLog> list = operateLogDao.selPageList(page, operateLog);
		page.setRecords(list);
		return page;
	}

	@Override
	public boolean insertLog(String module,String type,String ip,String content,String operater) {
		OperateLog operateLog = new OperateLog();
		operateLog.setModule(module);
		operateLog.setType(type);
		operateLog.setIp(ip);
		operateLog.setContent(content);
		operateLog.setCreateBy(operater);
		operateLog.setUpdateBy(operater);
		ThreadManager.execute(new LogTask(this,operateLog));
		return true;
	}

	@Override
	public List<OperateLog> exportData(OperateLog operateLog) {
		List<OperateLog> exportData = operateLogDao.exportData(operateLog);
		return exportData;
	}
}
