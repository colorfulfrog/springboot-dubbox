package com.yxhl.stationbiz.system.provider.dao.finance;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.yxhl.platform.common.service.dao.CrudDao;
import com.yxhl.stationbiz.system.domain.entity.finance.BillEntry;

/**
 *	
 *  bs_bill_entryDao
 *  注释:票据录入表
 *  创建人: ypf
 *  创建日期:2018-9-12 11:00:03
 */
@Mapper
@Repository
public interface BillEntryDao extends CrudDao<BillEntry>{
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param billEntry 条件参数
	 * @return 当前页数据
	 */
	List<BillEntry> selPageList(Pagination page,BillEntry billEntry);
}