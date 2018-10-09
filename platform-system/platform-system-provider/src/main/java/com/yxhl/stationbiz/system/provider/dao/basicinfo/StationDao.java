package com.yxhl.stationbiz.system.provider.dao.basicinfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;

@Mapper
@Repository
public interface StationDao extends CrudDao<Station> {

	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param project 条件参数
	 * @return 当前页数据
	 */
	List<Station> selPageList(Pagination page,Station station);
	
	/**
	 * 查当前区域最后一个站点
	 * @return
	 */
	public Station getOne(String regionCode);
	
	/**
	 * 查询导出数据
	 * @param station
	 * @return
	 */
	List<Station> exportData(Station station);
}
