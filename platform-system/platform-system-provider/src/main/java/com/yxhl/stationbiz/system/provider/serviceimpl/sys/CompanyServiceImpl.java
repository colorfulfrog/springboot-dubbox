package com.yxhl.stationbiz.system.provider.serviceimpl.sys;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import com.yxhl.stationbiz.system.domain.entity.sys.Company;
import com.yxhl.stationbiz.system.domain.service.sys.CompanyService;
import com.yxhl.stationbiz.system.provider.dao.sys.CompanyDao;


@Service(value = "companyService")
public class CompanyServiceImpl extends CrudService<CompanyDao, Company> implements CompanyService {

	@Autowired
	private CompanyDao companyDao;
	
	@Override
	public Page<Company> selPageList(Page<Company> page, Company company) {
		List<Company> list= companyDao.selPageList(page, company);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<Company> exportData(Company company) {
		List<Company> list=companyDao.exportData(company);
		for (Company company2 : list) {
			if(company2.getType()==1) {
				company2.setTypeName("运输公司");
			}
			if(company2.getType()==2) {
				company2.setTypeName("站场公司");
			}
		}
		return list;
	}

}