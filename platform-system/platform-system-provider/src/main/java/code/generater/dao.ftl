package ${daoPackage};

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import ${entityPackage}.${entityName};

/**
 *	
 *  ${tableName}Dao
 *  注释:${tableComment}
 *  创建人: ${auth}
 *  创建日期:${date}
 */
@Mapper
@Repository
public interface ${daoName} extends CrudDao<${entityName}>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param ${entityReName} 条件参数
	 * @return 当前页数据
	 */
	List<${entityName}> selPageList(Pagination page,${entityName} ${entityReName});
}