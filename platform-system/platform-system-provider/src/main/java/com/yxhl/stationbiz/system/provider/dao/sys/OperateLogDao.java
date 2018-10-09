package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.OperateLog;

/**
 *	
 *  sys_operate_logDao
 *  注释:操作日志表
 *  创建人: xjh
 *  创建日期:2018-7-9 17:20:55
 */
@Mapper
@Repository
public interface OperateLogDao extends CrudDao<OperateLog>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param operateLog 条件参数
	 * @return 当前页数据
	 */
	List<OperateLog> selPageList(Pagination page,OperateLog operateLog);
	
	/**
	 * 查询导出数据
	 * @param operateLog
	 * @return
	 */
	List<OperateLog> exportData(OperateLog operateLog);
}