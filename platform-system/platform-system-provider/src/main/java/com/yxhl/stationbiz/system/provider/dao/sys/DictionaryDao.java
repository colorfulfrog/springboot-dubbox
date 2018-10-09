package com.yxhl.stationbiz.system.provider.dao.sys;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;

/**
 *	
 *  sys_dictionaryDao
 *  注释:
 *  创建人: xjh
 *  创建日期:2018-7-10 15:37:54
 */
@Mapper
@Repository
public interface DictionaryDao extends CrudDao<Dictionary>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param dictionary 条件参数
	 * @return 当前页数据
	 */
	List<Dictionary> selPageList(Pagination page,Dictionary dictionary);
}