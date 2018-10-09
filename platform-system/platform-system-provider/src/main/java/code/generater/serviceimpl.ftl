package ${serviceImplPackage};

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
<#if isTree == "1">
import com.yxhl.platform.common.service.service.TreeService;
<#else>
import com.yxhl.platform.common.service.service.CrudService;
</#if>
import org.springframework.transaction.annotation.Transactional;
import ${daoPackage}.${daoName};
import ${entityPackage}.${entityName};
import ${servicePackage}.${serviceName};
/**
 * @ClassName: ${serviceNameImpl}
 * @Description: ${tableComment} serviceImpl
 * @author ${auth}
 * @date ${date}
 */
@Transactional(readOnly = true)
@Service("${serviceReName}")
<#if isTree == "1">
public class ${serviceNameImpl} extends TreeService<${daoName}, ${entityName}> implements ${serviceName} {
<#else>
public class ${serviceNameImpl} extends CrudService<${daoName}, ${entityName}> implements ${serviceName} {
</#if>
	@Autowired
	private ${daoName} ${daoReName};

	@Override
	public Page<${entityName}> selPageList(Page<${entityName}> page, ${entityName} ${entityReName}) {
		List<${entityName}> list = ${daoReName}.selPageList(page, ${entityReName});
		page.setRecords(list);
		return page;
	}
}
