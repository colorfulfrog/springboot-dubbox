package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.TreeService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Region;
import com.yxhl.stationbiz.system.domain.service.basicinfo.RegionService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.RegionDao;

@Service(value = "regionService")
public class RegionServiceImpl extends TreeService<RegionDao, Region> implements RegionService {

	@Autowired
	private RegionDao regionDao;

	@Override
	public Page<Region> selPageList(Page<Region> page, Region region) {
		List<Region> list= regionDao.selPageList(page,region);
		page.setRecords(list);
		return page;
	}
	

}