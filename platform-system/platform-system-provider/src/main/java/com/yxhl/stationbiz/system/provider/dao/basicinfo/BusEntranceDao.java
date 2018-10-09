package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.BusEntrance;

/**
 *	
 *  bs_bus_entranceDao
 *  注释:乘车库表
 *  创建人: lw
 *  创建日期:2018-7-10 9:48:43
 */
@Mapper
@Repository
public interface BusEntranceDao extends CrudDao<BusEntrance>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param busEntrance 条件参数
	 * @return 当前页数据
	 */
	List<BusEntrance> selPageList(Pagination page,BusEntrance busEntrance);
}