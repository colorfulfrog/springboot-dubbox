package com.yxhl.stationbiz.system.provider.serviceimpl.finance;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.TreeService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.finance.BillEntryDao;
import com.yxhl.stationbiz.system.domain.entity.finance.BillEntry;
import com.yxhl.stationbiz.system.domain.service.finance.BillEntryService;
/**
 * @ClassName: BillEntryServiceImpl
 * @Description: 票据录入表 serviceImpl
 * @author ypf
 * @date 2018-9-12 11:16:57
 */
@Transactional(readOnly = true)
@Service("billEntryService")
public class BillEntryServiceImpl extends TreeService<BillEntryDao, BillEntry> implements BillEntryService {
	@Autowired
	private BillEntryDao billEntryDao;

	@Override
	public Page<BillEntry> selPageList(Page<BillEntry> page, BillEntry billEntry) {
		List<BillEntry> list = billEntryDao.selPageList(page, billEntry);
		page.setRecords(list);
		return page;
	}
}
