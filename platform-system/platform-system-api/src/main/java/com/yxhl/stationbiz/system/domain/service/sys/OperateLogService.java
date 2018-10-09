package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.OperateLog;

/**
 *  OperateLogService
 *  注释:操作日志表Service
 *  创建人: xjh
 *  创建日期:2018-7-9 17:20:55
 */
public interface OperateLogService extends IELService<OperateLog> {
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param operateLog 条件参数
	 * @return 当前页数据
	 */
	Page<OperateLog> selPageList(Page<OperateLog> page,OperateLog operateLog);
	
	/**
	 * 添加操作日志
	 * @param module 模块
	 * @param type 操作类型
	 * @param ip IP
	 * @param content 内容
	 * @param operater 操作人
	 * @return
	 */
	boolean insertLog(String module,String type,String ip,String content,String operater);
	
	/**
	 * 查询导出数据
	 * @param operateLog
	 * @return
	 */
	List<OperateLog> exportData(OperateLog operateLog);
}
