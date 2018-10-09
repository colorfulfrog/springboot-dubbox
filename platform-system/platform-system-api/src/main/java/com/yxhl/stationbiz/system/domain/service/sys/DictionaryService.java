package com.yxhl.stationbiz.system.domain.service.sys;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;

/**
 *  DictionaryService
 *  注释:Service
 *  创建人: xjh
 *  创建日期:2018-7-10 15:37:54
 */
public interface DictionaryService extends IELService<Dictionary>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param dictionary 条件参数
	 * @return 当前页数据
	 */
	Page<Dictionary> selPageList(Page<Dictionary> page,Dictionary dictionary);
	
	/**
	 * 通过key查数据字典
	 * @param dictionary
	 * @return
	 */
	public List<Dictionary> getByKey(Dictionary dictionary);
	
	/**
	 * 通过key查单个数据字典
	 * @param dictionary
	 * @return
	 */
	public Dictionary getOneByKey(String key);
	
	/**
	 * 通过key取缓存
	 * @param key
	 * @return
	 */
	public List<Dictionary> getRediesByKey(String key);
	
	/**
	 * 批量删除
	 * @param dictionaryId
	 * @return
	 */
	public boolean deleteById(List<String> dictionaryId);
	
}
