package ${servicePackage};

import com.baomidou.mybatisplus.plugins.Page;
<#if isTree == "1">
import com.yxhl.platform.common.service.IELTreeService;
<#else>
import com.yxhl.platform.common.service.IELService;
</#if>
import ${entityPackage}.${entityName};

/**
 *  ${serviceName}
 *  注释:${tableComment}Service
 *  创建人: ${auth}
 *  创建日期:${date}
 */
<#if isTree == "1">
public interface ${serviceName} extends IELTreeService<${entityName}>{
<#else>
public interface ${serviceName} extends IELService<${entityName}>{
</#if>
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ${entityReName} 条件参数
	 * @return 当前页数据
	 */
	Page<${entityName}> selPageList(Page<${entityName}> page,${entityName} ${entityReName});
}
