package ${requestManagerPackage};


import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import ${entityPackage}.${entityName};
import com.yxcoach.common.request.BaseRequest;

/**
 *	
 *  注释:${tableComment} rquest对象
 *  创建人: ${auth}
 *  创建日期:${date}
 */
@ApiModel(value = "${requestManagerEntityName}", description = "${tableComment} rquest对象")
public class ${requestManagerEntityName} extends BaseRequest{
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "${tableComment}对象")
	private ${entityName} ${entityReName};
	
	public void set${entityReName?cap_first}(${entityName} ${entityReName}){
		this.${entityReName}=${entityReName};
	}
	public ${entityName} get${entityReName?cap_first}(){
		return this.${entityReName};
	}	
}
