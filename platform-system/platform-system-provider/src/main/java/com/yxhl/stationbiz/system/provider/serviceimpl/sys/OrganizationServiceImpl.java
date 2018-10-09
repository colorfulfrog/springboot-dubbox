package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.sys.OrganizationDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Organization;
import com.yxhl.stationbiz.system.domain.service.sys.OrganizationService;
/**
 * @ClassName: OrganizationServiceImpl
 * @Description: 机构表 serviceImpl
 * @author xjh
 * @date 2018-7-12 15:58:15
 */
@Transactional(readOnly = true)
@Service("organizationService")
public class OrganizationServiceImpl extends CrudService<OrganizationDao, Organization> implements OrganizationService {
	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public Page<Organization> selPageList(Page<Organization> page, Organization organization) {
		List<Organization> list = organizationDao.selPageList(page, organization);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<Organization> exportData(Organization organization) {
		List<Organization> exportData = organizationDao.exportData(organization);
		return exportData;
	}
}
