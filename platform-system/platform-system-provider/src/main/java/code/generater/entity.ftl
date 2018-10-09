package ${entityPackage};


import com.baomidou.mybatisplus.annotations.TableName;
<#if isTree == "1">
import com.yxhl.platform.common.entity.ELTreeItem;
<#else>
import com.yxhl.platform.common.entity.ELItem;
</#if>
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:${tableName}
 *  注释:${tableComment}
 *  创建人: ${auth}
 *  创建日期:${date}
 */
@Data
@TableName(value="${tableName}")
<#if isTree == "1">
public class ${entityName} extends ELTreeItem<${entityName}>{
<#else>
public class ${entityName} extends ELItem{
</#if>
    private static final long serialVersionUID = 1L;
    
<#if fields ?exists >
	<#list fields as tem>
	<#if tem[0] =="java.lang.String">
	<#if tem[3] !="">
	@ApiModelProperty(value = "${tem[2]} 长度(${tem[4]}) 必填")
	</#if>
	<#if tem[3] =="">
	@ApiModelProperty(value = "${tem[2]} 长度(${tem[4]})")
	</#if>
	</#if>
	<#if tem[0] !="java.lang.String">
	@ApiModelProperty(value = "${tem[2]} ")
	</#if>
	private ${tem[0]} ${tem[1]};
	
	</#list>
</#if>
}
