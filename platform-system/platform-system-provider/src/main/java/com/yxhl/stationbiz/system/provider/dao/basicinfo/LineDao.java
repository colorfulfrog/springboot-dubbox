package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;

/**
 *	
 *  bs_lineDao
 *  注释:线路表
 *  创建人: xjh
 *  创建日期:2018-7-10 14:54:42
 */
@Mapper
@Repository
public interface LineDao extends CrudDao<Line>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param line 条件参数
	 * @return 当前页数据
	 */
	List<Line> selPageList(Pagination page,Line line);
	
	Line selById(@Param("id") String id);
	
}