package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.redis.util.RedisUtil;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Dictionary;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *	
 *  控制器
 *  创建人: xjh
 *  创建日期:2018-7-10 15:37:54
 */
@RestController
@Api(tags = "字典管理")
@RequestMapping(value = "/m/dictionary/")
public class DictionaryController extends BaseController{
	
    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
	private RedisUtil redisUtil;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Dictionary dictionary) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Dictionary> page = new Page<Dictionary>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Dictionary> result = dictionaryService.selPageList(page, dictionary);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有", notes = "查询所有信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Dictionary dictionary) {
		Wrapper<Dictionary> wrapper = new EntityWrapper<Dictionary>();
		wrapper.eq("configKey", dictionary.getConfigKey());
		List<Dictionary> list = dictionaryService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "新增", notes = "新增信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addDictionary(@RequestBody Dictionary dictionary) {
		dictionary.setId(IdWorker.get32UUID());
		dictionary.setCreateBy(getLoginUser().getId());
		boolean isAdded = dictionaryService.insert(dictionary);
		if(isAdded) {
			//取缓存
			List<Dictionary> list = (List<Dictionary>) redisUtil.get("SYS_DICT:"+dictionary.getConfigKey());
			if(list!=null) {
				list.add(dictionary);
			}else {
				list = new ArrayList<Dictionary>();
				list.add(dictionary);
			}
			//存缓存
			redisUtil.set("SYS_DICT:"+dictionary.getConfigKey(),list);
			return CommonResponse.createCommonResponse(dictionary);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}
	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "批量新增", notes = "批量新增数据字典", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/batchAdd", produces = "application/json;charset=UTF-8")
	public CommonResponse batchAdd(@RequestBody List<Dictionary> DicList) {
		for (Dictionary dictionary : DicList) {
			dictionary.setId(IdWorker.get32UUID());
			dictionary.setCreateBy(getLoginUser().getId());
			boolean isAdded = dictionaryService.insert(dictionary);
			if(isAdded) {
				//取缓存
				List<Dictionary> list = (List<Dictionary>) redisUtil.get("SYS_DICT:"+dictionary.getConfigKey());
				if(list!=null) {
					list.add(dictionary);
				}else {
					list = new ArrayList<Dictionary>();
					list.add(dictionary);
				}
				//存缓存
				redisUtil.set("SYS_DICT:"+dictionary.getConfigKey(),list);
			}
		}
		return CommonResponse.createCommonResponse();
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "修改", notes = "修改信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update/{dictionaryId}", produces = "application/json;charset=UTF-8")
	public CommonResponse updateDictionary(@RequestBody Dictionary dictionary,@PathVariable("dictionaryId") String dictionaryId) {
		dictionary.setId(dictionaryId);
		dictionary.setUpdateBy(getLoginUser().getId());
		boolean isUpdated = dictionaryService.updateById(dictionary);
		if(isUpdated) {
			//取缓存
			List<Dictionary> list = (List<Dictionary>) redisUtil.get("SYS_DICT:"+dictionary.getConfigKey());
			for(Dictionary dict : list) {
				if(dictionary.getId().equals(dict.getId())) {
					list.remove(dict);
					list.add(dictionary);
					break;
				}
			}
			//存缓存
			redisUtil.set("SYS_DICT:"+dictionary.getConfigKey(),list);
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除", notes = "删除信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/delete/{dictionaryId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("dictionaryId") List<String> dictionaryId) {
		boolean isDel = dictionaryService.deleteById(dictionaryId);
		if(isDel) {
			for (String key : dictionaryId) {
				redisUtil.remove("SYS_DICT:"+key);
			}
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看详情", notes = "查看详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/info/{getInfo}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("dictionaryId") String dictionaryId) {
		Dictionary dictionary = dictionaryService.selectById(dictionaryId);
		return CommonResponse.createCommonResponse(dictionary);
	}
	
	@ApiOperation(value = "查数据字典", notes = "查数据字典", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/getByKey/{configKey}", produces = "application/json;charset=UTF-8")
	public CommonResponse getByKey(@PathVariable("configKey") String configKey) {
		Dictionary dictionary = new Dictionary();
		dictionary.setConfigKey(configKey);
		List<Dictionary> list = dictionaryService.getRediesByKey(configKey);
		if(list.size()>0 && list!=null) {
			return CommonResponse.createCommonResponse(list);
		}else {
			return CommonResponse.createCommonResponse(new ArrayList<>());
		}
		
	}
	
	@ApiOperation(value = "根据key删除redis缓存", notes = "根据key删除redis缓存", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/redis/{key}", produces = "application/json;charset=UTF-8")
	public CommonResponse delRedisDic(@PathVariable("key") String key) {
			redisUtil.remove(key);
			return CommonResponse.createCommonResponse();
	}
}
