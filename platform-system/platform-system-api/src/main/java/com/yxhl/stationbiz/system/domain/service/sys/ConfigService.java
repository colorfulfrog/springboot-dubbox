package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;

/**
 *  ConfigService
 *  注释:参数配置表Service
 *  创建人: xjh
 *  创建日期:2018-7-12 16:14:50
 */
public interface ConfigService extends IELService<Config>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param config 条件参数
	 * @return 当前页数据
	 */
	Page<Config> selPageList(Page<Config> page,Config config);
	
	/**
	 * 查询导出数据
	 * @param config
	 * @return
	 */
	List<Config> exportData(Config config);
	
	/**
	 * 根据编码查询参数值
	 * @param orgId 机构ID
	 * @param code 参数编码
	 * @return
	 */
	String selByCode(String orgId,String code);
}
