package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;

/**
 *	
 *  sys_configDao
 *  注释:参数配置表
 *  创建人: xjh
 *  创建日期:2018-7-12 16:14:50
 */
@Mapper
@Repository
public interface ConfigDao extends CrudDao<Config>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param config 条件参数
	 * @return 当前页数据
	 */
	List<Config> selPageList(Pagination page,Config config);
	
	/**
	 * 查询导出数据
	 * @param config
	 * @return
	 */
	List<Config> exportData(Config config);
	
	/**
	 * 根据编码查询参数信息
	 * @param orgId 机构ID
	 * @param code 参数编码
	 * @return
	 */
	Config selByCode(@Param("orgId")String orgId,@Param("code")String code);
}