package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.redis.util.RedisUtil;
import com.yxhl.platform.common.service.service.CrudService;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.sys.DictionaryDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
/**
 * @ClassName: DictionaryServiceImpl
 * @Description:  serviceImpl
 * @author xjh
 * @date 2018-7-10 15:37:54
 */
@Transactional(readOnly = true)
@Service("dictionaryService")
public class DictionaryServiceImpl extends CrudService<DictionaryDao, Dictionary> implements DictionaryService {
	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public Page<Dictionary> selPageList(Page<Dictionary> page, Dictionary dictionary) {
		List<Dictionary> list = dictionaryDao.selPageList(page, dictionary);
		page.setRecords(list);
		return page;
	}

	/**
	 * 通过key查数据字典
	 */
	@Override
	public List<Dictionary> getByKey(Dictionary dictionary) {
		Wrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>();
		if(dictionary.getMultiFlag()!=null) {
			wrapper.eq("multi_flag", dictionary.getMultiFlag());
		}
		if(dictionary.getConfigKey()!=null) {
			wrapper.eq("config_key", dictionary.getConfigKey());
		}
		List<Dictionary> selectList = dictionaryDao.selectList(wrapper);
		if(null==selectList) {
			return null;
		}
		return selectList;
	}

	/**
	 * 通过key查单个数据字典
	 */
	@Override
	public Dictionary getOneByKey(String key) {
		List<Dictionary> list = getRediesByKey(key);
		Dictionary dict=list.get(0);
		return dict;
	}

	/**
	 * 通过key查缓存数据字典
	 */
	@Override
	public List<Dictionary> getRediesByKey(String key) {
		List<Dictionary> rList = new ArrayList<Dictionary>();
		if(key !=null) {
			//取缓存
			List<Dictionary> list = (List<Dictionary>) redisUtil.get("SYS_DICT:"+key);
			if(list != null) {
				rList.addAll(list);
			}else {				//缓存没有，查数据库
				List<Dictionary> selectList = new ArrayList<Dictionary>();
				Wrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>();
				wrapper.eq("config_key", key);
				wrapper.orderBy("value",true);
				selectList = dictionaryDao.selectList(wrapper);
				if(selectList != null) {
					rList.addAll(selectList);
					list = selectList;
					//存缓存
					redisUtil.set("SYS_DICT:"+key,list);
				}
			}
		}
		return rList;
	}

	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteById(List<String> dictionaryId) {
		List<Dictionary> getById = dictionaryDao.selectBatchIds(dictionaryId);
		for(Dictionary dict : getById) {
			//取缓存
			List<Dictionary> list = (List<Dictionary>) redisUtil.get("SYS_DICT:"+dict.getConfigKey());
			if(list != null) {
				for(Dictionary dic : list) {
					if(dict.getId().equals(dic.getId())) {
						list.remove(dic);
						//存缓存
						redisUtil.set("SYS_DICT:"+dic.getConfigKey(),list);
						break;
					}
				}
			}
		}
		Integer result = dictionaryDao.deleteBatchIds(dictionaryId);
		return result > 0 ? true : false;
	}
	
	
}
