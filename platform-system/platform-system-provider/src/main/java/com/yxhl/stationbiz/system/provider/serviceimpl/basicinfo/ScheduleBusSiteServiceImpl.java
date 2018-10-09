package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusSite;
import com.yxhl.stationbiz.system.domain.service.basicinfo.ScheduleBusSiteService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.ScheduleBusSiteDao;
/**
 * @ClassName: ScheduleBusSiteServiceImpl
 * @Description: 班次停靠点 serviceImpl
 * @author lw
 * @date 2018-7-12 19:15:10
 */
@Transactional(readOnly = true)
@Service("scheduleBusSiteService")
public class ScheduleBusSiteServiceImpl extends CrudService<ScheduleBusSiteDao, ScheduleBusSite> implements ScheduleBusSiteService {
	@Autowired
	private ScheduleBusSiteDao scheduleBusSiteDao;

	@Override
	public Page<ScheduleBusSite> selPageList(Page<ScheduleBusSite> page, ScheduleBusSite scheduleBusSite) {
		List<ScheduleBusSite> list = scheduleBusSiteDao.selPageList(page, scheduleBusSite);
		page.setRecords(list);
		return page;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean sort(String busSiteId,String sortType) {
		boolean result=false;
		ScheduleBusSite busSite = scheduleBusSiteDao.selectById(busSiteId);
		Integer sort= busSite.getSort();
		ScheduleBusSite destBusSite=null;
		if(sortType.equals("up")) {
			destBusSite=scheduleBusSiteDao.selSortMaxSite(sort,busSite.getScheduleBusTplId());
			checkNotNull(destBusSite,"不能再向上排序");
		}else if(sortType.equals("down")) {
			destBusSite=scheduleBusSiteDao.selSortMinSite(sort,busSite.getScheduleBusTplId());
			checkNotNull(destBusSite,"不能再向下排序");
		}
		Integer destSort= destBusSite.getSort();
		destBusSite.setSort(sort);
		busSite.setSort(destSort); 
		Integer  result1=scheduleBusSiteDao.updateById(busSite);
		Integer  result2=scheduleBusSiteDao.updateById(destBusSite);
		if(result1>0&&result2>0) {
			result=true;
		}
		return result;
		
	}

	@Override
	public ScheduleBusSite selOne(String id) {
		return scheduleBusSiteDao.selOne(id);
	}

	/**
	 * 根据班次模板id，站点名称查停靠点
	 * @param st
	 * @return
	 */
	@Override
	public List<ScheduleBusSite> getStation(ScheduleBusSite st) {
		List<ScheduleBusSite> station = scheduleBusSiteDao.getStation(st);
		return station;
	}
}
