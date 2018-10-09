package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;



import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.exception.YxBizException;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Region;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.service.basicinfo.StationService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.RegionDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.StationDao;

/**
 * 站点管理
 * @author xjh
 *
 */
@Service(value = "stationService")
public class StationServiceImpl extends CrudService<StationDao, Station> implements StationService {

	@Autowired
	private StationDao stationDao;
	
	@Autowired
	private RegionDao regionDao;

	@Override
	public Page<Station> selPageList(Page<Station> page, Station station) {
		//是否上车点 0是、1否  如果为0则赋值为10，因为mybatis识别不了0
		if(station.getBoardPointFlag()!=null && station.getBoardPointFlag()==0) {
			station.setBoardPointFlag(10);
		}
		List<Station> list= stationDao.selPageList(page,station);
		page.setRecords(list);
		return page;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean add(Station sta) {
		Wrapper<Station> wrapper = new EntityWrapper<Station>();
		if(sta.getStationName() !=null) {
			wrapper.eq("station_name", sta.getStationName());
		}
		List<Station> list = stationDao.selectList(wrapper);
		if(list!=null && list.size()>0) {
			throw new YxBizException("该站点已存在!");
		}
		Region region = regionDao.selectById(sta.getRegionCode());
		//当前区域最后一个站点
		Station one = stationDao.getOne(sta.getRegionCode());
		Integer result = 1;
		if(region !=null) {
			//当前区域是否有站点
			if(one !=null && one.getRegionCode().equals(region.getId())) {
				String oldCode = one.getStationCode();
				String code = String.valueOf(Integer.parseInt(oldCode)+1); 
				sta.setStationCode(code);
			}else {
				sta.setStationCode(region.getRegionCode()+"001");
			}
			sta.setServiceStationFlag(null);
			result = stationDao.insert(sta);
		}
		return result > 0 ?true : false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateSta(Station sta) {
		Integer result = 1;
		Wrapper<Station> wrapper = new EntityWrapper<Station>();
		if(sta.getStationName() !=null) {
			wrapper.eq("station_name", sta.getStationName());
		}
		List<Station> list = stationDao.selectList(wrapper);
		if(list!=null && list.size()>0) {
			for(Station son : list) {
				if(son.getStationName().equals(sta.getStationName()) && !(son.getId().equals(sta.getId()))) {
					throw new YxBizException("该站点已存在!");
				}
			}
		}
		
		Station selectById = stationDao.selectById(sta.getId());
		//查区域是否改变
		if(selectById!=null && selectById.getRegionCode().equals(sta.getRegionCode())) {
			result = stationDao.updateById(sta);
		}else {
			Region region = regionDao.selectById(sta.getRegionCode());
			//当前区域最后一个站点
			Station one = stationDao.getOne(sta.getRegionCode());
			if(region !=null) {
				if(one !=null && one.getRegionCode().equals(region.getId())) {
					String oldCode = one.getStationCode();
					String code = String.valueOf(Integer.parseInt(oldCode)+1); 
					sta.setStationCode(code);
				}else {
					sta.setStationCode(region.getRegionCode()+"001");
				}
				result = stationDao.updateById(sta);
			}
		}
		return result > 0 ?true : false;
	}

	@Override
	public List<Station> exportData(Station station) {
		List<Station> exportData = stationDao.exportData(station);
		return exportData;
	}
	

}