package com.yxhl.stationbiz.system.provider.serviceimpl.basicinfo;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.LineSite;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Station;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.enums.LineEnum;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineService;
import com.yxhl.stationbiz.system.domain.service.basicinfo.LineSiteService;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.LineSiteDao;
import com.yxhl.stationbiz.system.provider.dao.basicinfo.StationDao;
/**
 * @ClassName: LineServiceImpl
 * @Description: 线路表 serviceImpl
 * @author xjh
 * @date 2018-7-10 14:54:42
 */
@Transactional(readOnly = true)
@Service("lineService")
public class LineServiceImpl extends CrudService<LineDao, Line> implements LineService {
	@Autowired
	private LineDao lineDao;
	
	@Autowired
	private LineSiteDao lineSiteDao;
	
	@Autowired
	private LineSiteService lineSiteService;
	
	@Autowired
	private StationDao stationDao;
	
	@Autowired
	private DictionaryService dictionaryService;

	@Override
	public Page<Line> selPageList(Page<Line> page, Line line) {
		List<Line> list = lineDao.selPageList(page, line);
		pushDictionaryName(list);
		page.setRecords(list);
		return page;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insertLine(Line line) {
		Integer isAdd= lineDao.insert(line);
		Station startStation=stationDao.selectById(line.getStartStateId());
		Station endStation=stationDao.selectById(line.getEndStateId());
		checkNotNull(startStation,"操作失败，起点站 不存在！");
		checkNotNull(endStation,"操作失败，终点站 不存在！");
		//默认添加起始点
		LineSite startLineSite=new LineSite(line,startStation) ;
		startLineSite.setId(IdWorker.get32UUID());
		startLineSite.setSort(1);
		lineSiteDao.insert(startLineSite);
		//默认终点站
		LineSite endLineSite=new LineSite(line,endStation) ;
		endLineSite.setId(IdWorker.get32UUID());
		endLineSite.setSort(99); 
		lineSiteDao.insert(endLineSite);
		
		return isAdd>0?true:false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateLine(Line line) {
		Line oldLine=lineDao.selectById(line.getId());
		Integer isupdate= lineDao.updateById(line);
		
		/*if(!StringUtils.equals(oldLine.getStartStateId(), line.getStartStateId())) {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("line_id", line.getId());
			paramMap.put("station_id", oldLine.getStartStateId());
			List<LineSite> lineSiteList=lineSiteDao.selectByMap(paramMap);
			
		
			paramMap.put("station_id", line.getStartStateId());
			List<LineSite> lineSiteList1=lineSiteDao.selectByMap(paramMap);
			if(Util.isNotNull(lineSiteList)&&Util.isNull(lineSiteList1)) {
				LineSite lineSite=lineSiteList.get(0);
				lineSite.setStationId(line.getStartStateId()); 
				lineSite.setUpdateBy(line.getUpdateBy());
				lineSiteDao.updateById(lineSite);
			}
		}
		
		if(!StringUtils.equals(oldLine.getEndStateId(), line.getEndStateId())) {
			Map<String,Object> paramMap=new HashMap<String,Object>();
			paramMap.put("line_id", line.getId());
			paramMap.put("station_id", oldLine.getEndStateId());
			List<LineSite> lineSiteList=lineSiteDao.selectByMap(paramMap);
			
			paramMap.put("station_id", line.getEndStateId());
			List<LineSite> lineSiteList2=lineSiteDao.selectByMap(paramMap);
			
			if(Util.isNotNull(lineSiteList)&&Util.isNull(lineSiteList2)) {
				LineSite lineSite=lineSiteList.get(0);
				lineSite.setStationId(line.getEndStateId()); 
				lineSite.setUpdateBy(line.getUpdateBy());
				lineSiteDao.updateById(lineSite);
			}
		}*/
		if(!StringUtils.equals(oldLine.getStartStateId(), line.getStartStateId())||
				!StringUtils.equals(oldLine.getEndStateId(), line.getEndStateId())) {
			Wrapper<LineSite> wrapper = new EntityWrapper<LineSite>();
			wrapper.eq("line_id", line.getId());
			wrapper.orderBy("sort");
			List<LineSite> lineSiteList=lineSiteDao.selectList(wrapper);
			if(CollectionUtils.isNotEmpty(lineSiteList)) {
				int idx=1;
				int maxIdx=lineSiteList.size();
				for(int i=0;i<lineSiteList.size();i++) {
					LineSite site=lineSiteList.get(i);
					if(StringUtils.equals(site.getStationId(), line.getStartStateId())) {
						site.setSort(1);
					}else if(StringUtils.equals(site.getStationId(), line.getEndStateId())) {
						site.setSort(maxIdx);
					}else {
						idx++;
						site.setSort(idx);
					}
					
				}
			}
			lineSiteService.updateBatchById(lineSiteList);
		}
		
		
		
		
		return isupdate>0?true:false;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean delLine(List<String> lineIds) {
		Integer isDel= lineDao.deleteBatchIds(lineIds);
		
		//将关联的线路停靠站也删除
		for(String lineId:lineIds) {
			Map<String,Object> delPamras=new HashMap<String,Object>();
			delPamras.put("line_id", lineId);
			lineSiteDao.deleteByMap(delPamras);
		}
		return isDel>0?true:false;
	}
	
	
	
	@Override
	public Line selById( String id) {
		Line line= lineDao.selById(id);
		List<Line> list=new ArrayList<Line>();
		pushDictionaryName(list);
		return line;
	}
	
	
	
	/**
	 * 匹配字典表返回结果
	 * @param list
	 */
	private void pushDictionaryName(List<Line> list) {
		List<Dictionary> rangeTypelist= dictionaryService.getRediesByKey(LineEnum.REGION_TYPE.getLineType());
		List<Dictionary> levellist= dictionaryService.getRediesByKey(LineEnum.LINE_LEVE.getLineType());
		for(Line le:list) {
			for(Dictionary dr:rangeTypelist) {
					 if(null!=le&&null!=le.getRangeType()&&
							 dr.getValue().equals(le.getRangeType().toString())) {
					 le.setRangeTypeName(dr.getKeyName());
				 }
			}
			for(Dictionary dr:levellist) {
				 if(null!=le&&null!=le.getLineLevel()&&
						 dr.getValue().equals(le.getLineLevel().toString())) {
				 le.setLineLevelName(dr.getKeyName());
			 }
			}
		}
	}
	
	
	
	
	
}
