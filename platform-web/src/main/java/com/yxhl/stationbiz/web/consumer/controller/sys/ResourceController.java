package com.yxhl.stationbiz.web.consumer.controller.sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.system.domain.entity.sys.Resource;
import com.yxhl.stationbiz.system.domain.entity.sys.Role;
import com.yxhl.stationbiz.system.domain.enums.OperateLogModelEnum;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;
import com.yxhl.stationbiz.system.domain.service.sys.RelationService;
import com.yxhl.stationbiz.system.domain.service.sys.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "菜单信息")
@RequestMapping(value = "/m/resource")
public class ResourceController extends BaseController {
	@Autowired
	private ResourceService resourceService;
	
    @Autowired
	private OperateLogService logService;

	@ApiOperation("分页查询")
	@PostMapping(value = "/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize,@RequestBody Resource resource) {
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<Resource> page = new Page<Resource>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<Resource> result = resourceService.selPageList(page, resource);
		return CommonResponse.createCommonResponse(result);
	}
	
	@ApiOperation("查询所有项目")
	@GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestBody Resource resource) {
		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
		//wrapper.where("id={0} and create_by={1}", 9,1);
		
		//like
		//wrapper.where("id={0}", 10).like("name", "6");
		List<Resource> list = resourceService.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("查询菜单基本信息")
	@GetMapping(value = "/{resourceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("resourceId") String resourceId) {
		Resource resource = resourceService.selectById(resourceId);
		return CommonResponse.createCommonResponse(resource);
	}
	
	@ApiOperation("新增菜单基本信息")
	@PostMapping(value = "/add", produces = "application/json;charset=UTF-8")
	public CommonResponse addResource(@RequestBody Resource resource) {
		ELUser eluser= getLoginUser();
		resource.setId(IdWorker.get32UUID()+"");
		resource.setVersion(0L);
		resource.setType(1);
		resource.setCreateBy(eluser.getId());
		resource.setUpdateBy(eluser.getId());
		boolean isAdded = resourceService.add(resource);
		if(isAdded) {
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增菜单失败!");
		}
	}
	
	@ApiOperation("修改菜单基本信息")
	@PostMapping(value = "/update", produces = "application/json;charset=UTF-8")
	public CommonResponse updateResource(@RequestBody Resource resource) {
		Resource re=resourceService.selectById(resource.getId());
		resource.setUpdateBy(getLoginUser().getId());
		boolean isAdded = resourceService.updateById(resource);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.RESOURCE.getModule(),"修改菜单",getRemoteAddr(),"被修改的菜单名称【"+re.getResourceName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改菜单失败!");
		}
	}
	
	@ApiOperation("删除菜单")
	@DeleteMapping(value = "/{resourceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("resourceId") String resourceId) {
		Resource re= resourceService.selectById(resourceId);
		boolean isDel = resourceService.deleteById(resourceId);
		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
		wrapper.where("type={0} and parent_id={1}", 2, resourceId);
		List<Resource> list1 = resourceService.selectList(wrapper);// 查询所删除菜单下面的按钮
		if(list1.size()>0) {
			for (Resource resource : list1) {
				resourceService.deleteById(resource.getId());//删除菜单下面所有的按钮
			}
		}
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.RESOURCE.getModule(),"删除菜单",getRemoteAddr(),"菜单名称【"+re.getResourceName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除菜单失败!");
		}
	}
	@ApiOperation("查询角色所对应的一级菜单")
	@GetMapping(value = "/getresource", produces = "application/json;charset=UTF-8")
	public CommonResponse getresourceByUid() {
		ELUser eluser= getLoginUser();
		List<Resource> resource = resourceService.selectRidResource("0",1,eluser.getId());
		return CommonResponse.createCommonResponse(resource);
	}
	
	@ApiOperation("查询角色所对应的二级菜单")
	@GetMapping(value = "/getresource1/{parentId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getresourceByUid1(@PathVariable("parentId") String parentId) {
		ELUser eluser= getLoginUser();
		List<Resource> resource = resourceService.selectRidResource(parentId,1,eluser.getId());
		return CommonResponse.createCommonResponse(resource);
	}
	@ApiOperation("查询角色所对应的按钮")
	@GetMapping(value = "/getresource2/{parentId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getresourceByUid2(@PathVariable("parentId") String parentId) {
		ELUser eluser= getLoginUser();
		List<Resource> resource = resourceService.selectRidResource(parentId,2,eluser.getId());
		return CommonResponse.createCommonResponse(resource);
	}
	
	@ApiOperation("tree结构")
	@PostMapping(value = "/getresourceList", produces = "application/json;charset=UTF-8")
	public CommonResponse getresourceList() {
//		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
//		wrapper.where("type={0} and parent_id={1}", 1,"0").orderBy("resource_level").orderBy("sort_num");
//		List<Resource> list=resourceService.selectList(wrapper);//一级菜单
//		List<Resource> resourceList = new ArrayList<Resource>();
//		if (list.size() > 0) {
//			for (Resource resource2 : list) {
//				Wrapper<Resource> wrapper1 = new EntityWrapper<Resource>();
//				wrapper1.where("type={0} and parent_id={1}", 1, resource2.getId()).orderBy("resource_level").orderBy("sort_num");;
//				List<Resource> list1 = resourceService.selectList(wrapper1);// 二级菜单
//				if (list1.size() > 0) {
//					for (Resource resource3 : list1) {
//						Wrapper<Resource> wrapper2 = new EntityWrapper<Resource>();
//						wrapper2.where("type={0} and parent_id={1}", 2, resource3.getId()).orderBy("resource_level").orderBy("sort_num");;
//						List<Resource> list2 = resourceService.selectList(wrapper2);// 按钮
//						resource3.setChridendList(list2);
//					}
//				}
//				resource2.setChridendList(list1);
//				resourceList.add(resource2);//一级菜单
//			}
//		}
//		return CommonResponse.createCommonResponse(resourceList);
		List<Resource> treeList = new ArrayList<Resource>();// 第一级
		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
		wrapper.orderBy("resource_level").orderBy("sort_num");
		List<Resource> list = resourceService.selectList(wrapper);// 一级菜单
		List<Resource> list1 = new ArrayList<Resource>();// 一级
		List<Resource> list2 = new ArrayList<Resource>();// 二级
		if (list.size() > 0) {
			Iterator<Resource> it = list.iterator();// 去除一级二级留下三级
			while (it.hasNext()) {
				Resource resource = (Resource) it.next();
				if (resource.getParentId().equals("0") && resource.getType() == 1) {
					list1.add(resource);
					it.remove();
				}
				if (!resource.getParentId().equals("0") && resource.getType() == 1) {
					list2.add(resource);
					it.remove();
				}
			}
		}
		if (list1.size() > 0) {
			for (Resource resource : list1) {
				List<Resource> treeList1 = new ArrayList<Resource>();// 第二级
				if (list2.size() > 0) {
					for (Resource resource1 : list2) {
						List<Resource> treeList2 = new ArrayList<Resource>();// 第三级
						for (Resource resource2 : list) {
							if (resource1.getId().equals(resource2.getParentId())) {
								treeList2.add(resource2);
							}
						}
						if (resource.getId().equals(resource1.getParentId())) {
							treeList1.add(resource1);
						}
						resource1.setChridendList(treeList2);
					}
				}
				resource.setChridendList(treeList1);
				treeList.add(resource);
			}
		}
		return CommonResponse.createCommonResponse(treeList);
	}
	
	@ApiOperation("查询所有的一级菜单")
	@GetMapping(value = "/getresource3", produces = "application/json;charset=UTF-8")
	public CommonResponse getresource3() {
		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
		wrapper.where("type={0} and parent_id={1}", 1, 0);
		List<Resource> list=resourceService.selectList(wrapper);//一级菜单
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("查询所有的二级所对应的按钮")
	@GetMapping(value = "/getbutton/{parentId}", produces = "application/json;charset=UTF-8")
	public CommonResponse getbutton(@PathVariable("parentId") String parentId) {
		Wrapper<Resource> wrapper = new EntityWrapper<Resource>();
		wrapper.where("type={0} and parent_id={1}", 2, parentId);
		List<Resource> list=resourceService.selectList(wrapper);//一级菜单
		return CommonResponse.createCommonResponse(list);
	}
	
	@ApiOperation("新增菜单下的按钮")
	@PostMapping(value = "/addbotton", produces = "application/json;charset=UTF-8")
	public CommonResponse addbotton(@RequestBody Resource resource) {
		ELUser eluser= getLoginUser();
		resource.setId(IdWorker.get32UUID());
		resource.setVersion(0L);
		resource.setType(2);
		resource.setFunctionType(2);
		resource.setCreateBy(eluser.getId());
		resource.setUpdateBy(eluser.getId());
		resource.setIsShow(1);
		boolean isAdded = resourceService.insert(resource);
		if(isAdded) {
			logService.insertLog(OperateLogModelEnum.RESOURCE.getModule(),"新增菜单",getRemoteAddr(),"菜单名称【"+resource.getResourceName()+"】",eluser.getId());
			return CommonResponse.createCommonResponse(isAdded);
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增菜单失败!");
		}
	}
	
	@ApiOperation("删除菜单")
	@DeleteMapping(value = "/delByIds/{resourceId}", produces = "application/json;charset=UTF-8")
	public CommonResponse delByIds(@PathVariable("resourceId") List<String> resourceId) {
		List<Resource> resources = resourceService.selectBatchIds(resourceId);
		Resource resource = null;
		String resourceName = null;
		for (Resource resourceid : resources) {
			resource = resourceid;
			resourceName += resource.getResourceName()+",";
		}
		resourceName = resourceName.substring(0, resourceName.length());
		for (String string : resourceId) {
			Resource res= resourceService.selectById(string);
			if(res.getFunctionType()==1) {
				return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除菜单失败!系统菜单不能删除");
			}
		}
		boolean isDel = resourceService.deleteIds(resourceId);
		if(isDel) {
			logService.insertLog(OperateLogModelEnum.RESOURCE.getModule(),"删除菜单",getRemoteAddr(),"菜单名称【"+resource.getResourceName()+"】",getLoginUser().getId());
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除菜单失败!");
		}
	}
	
	@ApiOperation("tree结构")
	@GetMapping(value = "/ridResource/{rid}", produces = "application/json;charset=UTF-8")
	public CommonResponse ridResource(@PathVariable("rid") String rid) {
		List<Resource> treeList= new ArrayList<Resource>();//第一级
		List<String> stringList= new ArrayList<String>();
		List<Resource> list=resourceService.ridResource(rid);
		for (Resource resource : list) {
			stringList.add(resource.getId());
		}
		
//		List<Resource> list1=new ArrayList<Resource>();//一级
//		List<Resource> list2=new ArrayList<Resource>();//二级
//		if (list.size() > 0) {
//			Iterator<Resource> it = list.iterator();// 去除一级二级留下三级
//			while (it.hasNext()) {
//				Resource resource = (Resource) it.next();
//				if (resource.getParentId().equals("0") && resource.getType() == 1) {
//					list1.add(resource);
//					it.remove();
//				}
//				if (!resource.getParentId().equals("0") && resource.getType() == 1) {
//					list2.add(resource);
//					it.remove();
//				}
//			}
//		}
//		if (list1.size() > 0) {
//			for (Resource resource : list1) {
//				List<Resource> treeList1 = new ArrayList<Resource>();// 第二级
//				List<Resource> treeList2 = new ArrayList<Resource>();// 第三级
//				if (list2.size() > 0) {
//					for (Resource resource1 : list2) {
//						for (Resource resource2 : list) {
//							if (resource1.getId().equals(resource2.getParentId())) {
//								treeList2.add(resource2);
//							}
//						}
//						if (resource.getId().equals(resource1.getParentId())) {
//							treeList1.add(resource1);
//						}
//						resource1.setChridendList(treeList2);
//					}
//				}
//				resource.setChridendList(treeList1);
//				treeList.add(resource);
//			}
//		}
		return CommonResponse.createCommonResponse(stringList);
	}
}