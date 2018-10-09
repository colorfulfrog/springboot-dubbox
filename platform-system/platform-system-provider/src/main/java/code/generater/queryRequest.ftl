package ${requestManagerPackage};


import com.wordnik.swagger.annotations.ApiModel;
import com.yxcoach.common.request.BaseQueryRequest;

/**
 *	
 *  注释:${tableComment} rquest对象
 *  创建人: ${auth}
 *  创建日期:${date}
 */
@ApiModel(value = "${entityName}QueryRequest", description = "${tableComment} rquest分页查询对象")
public class ${entityName}QueryRequest extends BaseQueryRequest{
	private static final long serialVersionUID = 1L;
	
}
