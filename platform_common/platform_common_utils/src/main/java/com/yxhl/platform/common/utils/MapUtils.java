/**
 * 
 */
package com.yxhl.platform.common.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author liangtong
 * @date 
 */
public class MapUtils extends org.apache.commons.collections.MapUtils{
	
	/**
	 * 将对象装换为map
	 * @param et
	 * @return
	 */
	public static <T> Map<String, Object> bean2Map(T et){
		Map<String, Object> map = Maps.newHashMap();
		if(et != null){
			BeanMap beanMap = new BeanMap(et);
			for(Object key : beanMap.keySet()){
				map.put(String.valueOf(key), beanMap.get(key));
			}
		}
		return map;
	}
	
	
	/**
	 * 将List<Map<String,Object>>转换为List<T> 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> maps2Entitys(List<Map<String, Object>> maps, Class<T> clazz) throws InstantiationException, IllegalAccessException{
		List<T> list = Lists.newArrayList();
		if(maps != null && maps.size() > 0){
			Map<String, Object> map = null;
			T et = null;
			for(int i=0; i<maps.size(); i++){
				map = maps.get(i);
				et = map2Entity(map, clazz);
				list.add(et);
			}
		}
		return list;
	}
	
	
	/**
	 * Map对象转实体对象
	 * @param m
	 * @param clazz
	 * @return
	 */
	public static <T> T map2Entity(Map<String, Object> m, Class<T> clazz){
		T et = null;
		try{
			if(m != null){
				et = clazz.newInstance();
				BeanUtils.populate(et, m);
			}
		}catch(Exception ex){
		}
		return et;
	}
	
}
