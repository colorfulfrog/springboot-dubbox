package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineSiteService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineSiteDao;
/**
 * @ClassName: LineSiteServiceImpl
 * @Description: 线路停靠点 serviceImpl
 * @author lw
 * @date 2018-7-11 9:56:16
 */
@Transactional(readOnly = true)
@Service("lineSiteService")
public class LineSiteServiceImpl extends CrudService<LineSiteDao, LineSite> implements LineSiteService {
	@Autowired
	private LineSiteDao lineSiteDao;
	
	@Autowired
	private LineDao lineDao;

	@Override
	public Page<LineSite> selPageList(Page<LineSite> page, LineSite lineSite) {
		List<LineSite> list = lineSiteDao.selPageList(page, lineSite);
		page.setRecords(list);
		return page;
	}
	
	@Override
	public List<LineSite> selList(LineSite lineSite) {
		List<LineSite> list = lineSiteDao.selList(lineSite);
		return list;
	}
	
	
	
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean sort(String lineSiteId,String sortType) {
		boolean result=false;
		LineSite lineSite = lineSiteDao.selectById(lineSiteId);
		Integer sort= lineSite.getSort();
		LineSite destlineSite=null;
		if(sortType.equals("up")) {
			destlineSite=lineSiteDao.selSortMaxSite(sort,lineSite.getLineId());
			checkNotNull(destlineSite,"不能再向上排序");
		}else if(sortType.equals("down")) {
			destlineSite=lineSiteDao.selSortMinSite(sort,lineSite.getLineId());
			checkNotNull(destlineSite,"不能再向下排序");
		}
		
		Line line= lineDao.selById(lineSite.getLineId());
		checkArgument(!(StringUtils.equals(destlineSite.getStationId(), line.getStartStateId())||
				StringUtils.equals(destlineSite.getStationId(), line.getEndStateId())||
				StringUtils.equals(lineSite.getStationId(), line.getStartStateId())||
				StringUtils.equals(lineSite.getStationId(), line.getEndStateId())
				),"该站点不允许向"+(sortType.equals("up")?"上":"下")+"移动");
		Integer destSort= destlineSite.getSort();
		destlineSite.setSort(sort);
		lineSite.setSort(destSort); 
		Integer  result1=lineSiteDao.updateById(lineSite);
		Integer  result2=lineSiteDao.updateById(destlineSite);
		if(result1>0&&result2>0) {
			result=true;
		}
		return result;
		
	}



	@Override
	public LineSite selOne(String id) {
		return lineSiteDao.selOne(id);
	}
	
	
}
