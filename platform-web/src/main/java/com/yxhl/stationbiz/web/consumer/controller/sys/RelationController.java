package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Relation;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;


/**
 *	
 *  用户角色、角色菜单关联表控制器
 *  创建人: xjh
 *  创建日期:2018-7-18 9:56:13
 */
@RestController
@Api(tags = "用户角色、角色菜单关联表控制器")
@RequestMapping(value = "/m/relation/")
public class RelationController extends BaseController{
	
    @Autowired
    private RelationService relationService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, Relation relation) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Relation> page = new Page<Relation>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Relation> result = relationService.selPageList(page, relation);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation(value = "查询所有用户角色、角色菜单关联表", notes = "查询所有用户角色、角色菜单关联表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Relation relation) {
		Wrapper<Relation> wrapper = new EntityWrapper<Relation>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<Relation> list = relationService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增用户角色、角色菜单关联表", notes = "新增用户角色、角色菜单关联表信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addRelation(@RequestBody Relation relation) {
		relation.setId(IdWorker.get32UUID());
		relation.setUid(getLoginUser().getId());
		boolean isAdded = relationService.relationAdd(relation);
		if(isAdded) {
			return CommonResponse.createCommonResponse(relation);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改用户角色、角色菜单关联表", notes = "修改用户角色、角色菜单关联表信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateRelation(@RequestBody Relation relation) {
		boolean isUpdated = relationService.updateById(relation);
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除用户角色、角色菜单关联表", notes = "删除用户角色、角色菜单关联表信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{relationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("relationId") String relationId) {
		boolean isDel = relationService.deleteById(relationId);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看用户角色、角色菜单关联表详情", notes = "查看用户角色、角色菜单关联表详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/{relationId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("relationId") String relationId) {
		Relation relation = relationService.selectById(relationId);
		return CommonResponse.createCommonResponse(relation);
	}
}
