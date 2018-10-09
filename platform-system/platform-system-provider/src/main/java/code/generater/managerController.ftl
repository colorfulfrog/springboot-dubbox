package ${controlPackage};

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
import ${entityPackage}.${entityName};
import ${servicePackage}.${serviceName};
import com.yxhl.platform.common.entity.ELUser;
import com.yxhl.stationbiz.system.domain.service.sys.DictionaryService;
import com.yxhl.stationbiz.system.domain.service.sys.OperateLogService;


/**
 *	
 *  ${tableComment}控制器
 *  创建人: ${auth}
 *  创建日期:${date}
 */
@RestController
@Api(tags = "${tableComment}")
@RequestMapping(value = "/${controlType}")
public class ${controllerName} extends BaseController{
	
    @Autowired
    private ${serviceName} ${serviceReName};
    @Autowired
	private OperateLogService logService;
    @Autowired
    private DictionaryService dictionaryService;
    
	@ApiOperation("分页查询")
	@PostMapping(value = "/${entityReName}/page", produces = "application/json;charset=UTF-8")
	public CommonResponse page(@RequestParam("currentPage")String currentPage,@RequestParam("pageSize")String pageSize, @RequestBody ${entityName} ${entityReName}) {
		CommonResponse resp = CommonResponse.createCommonResponse();
		if(!StringUtils.isNotBlank(currentPage) & !StringUtils.isNotBlank(pageSize)){
			currentPage="1";
            pageSize="10";
        }
		Page<${entityName}> page = new Page<${entityName}>(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
		Page<${entityName}> result = ${serviceReName}.selPageList(page, ${entityReName});
		resp.setData(result);
		/*List<Dictionary> loopTypeList = dictionaryService.getRediesByKey("loop_typeint"); //查询循环类型字典
		resp.extendsRes("loopType", loopTypeList);*/
		return resp;
	}
	
	@ApiOperation(value = "根据条件查询${tableComment}", notes = "根据条件查询${tableComment}信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/${entityReName}s", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("name") String name) {
		Wrapper<${entityName}> wrapper = new EntityWrapper<${entityName}>();
		wrapper.like("name", name);
		//wrapper.where("id={0} and create_by={1}", 9,1);
		List<${entityName}> list = ${serviceReName}.selectList(wrapper);
		return CommonResponse.createCommonResponse(list);
	}

	@ApiOperation(value = "新增${tableComment}", notes = "新增${tableComment}信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(value = "/${entityReName}", produces = "application/json;charset=UTF-8")
	public CommonResponse add${entityName}(@RequestBody ${entityName} ${entityReName}) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			${entityReName}.setOrgId(loginUser.getOrgId());
			${entityReName}.setCompId(loginUser.getCompanyId());
			${entityReName}.setCreateBy(loginUser.getId());
			${entityReName}.setUpdateBy(loginUser.getId());
		}
		${entityReName}.setId(IdWorker.get32UUID());
		boolean isAdded = ${serviceReName}.insert(${entityReName});
		if(isAdded) {
			return CommonResponse.createCommonResponse(${entityReName});
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "新增失败!");
		}
	}

	@ApiOperation(value = "修改${tableComment}", notes = "修改${tableComment}信息", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(value = "/${entityReName}/{${entityReName}Id}", produces = "application/json;charset=UTF-8")
	public CommonResponse update${entityName}(@PathVariable("${entityReName}Id") String ${entityReName}Id,@RequestBody ${entityName} ${entityReName}) {
		ELUser loginUser = getLoginUser();
		if(loginUser != null) {
			${entityReName}.setUpdateBy(loginUser.getId());
		}
		${entityReName}.setId(${entityReName}Id);
		boolean isUpdated = ${serviceReName}.updateById(${entityReName});
		if(isUpdated) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "修改失败!");
		}
	}
	
	@ApiOperation(value = "删除${tableComment}", notes = "删除${tableComment}信息", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/${entityReName}/{${entityReName}Ids}", produces = "application/json;charset=UTF-8")
	public CommonResponse delById(@PathVariable("${entityReName}Ids") List<String> ${entityReName}Ids) {
		boolean isDel = ${serviceReName}.deleteBatchIds(${entityReName}Ids);
		if(isDel) {
			return CommonResponse.createCommonResponse();
		}else {
			return CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "删除失败!");
		}
	}

	@ApiOperation(value = "查看${tableComment}详情", notes = "查看${tableComment}详情信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(value = "/${entityReName}/{${entityReName}Id}", produces = "application/json;charset=UTF-8")
	public CommonResponse getInfoById(@PathVariable("${entityReName}Id") String ${entityReName}Id) {
		${entityName} ${entityReName} = ${serviceReName}.selectById(${entityReName}Id);
		return CommonResponse.createCommonResponse(${entityReName});
	}
}
