package com.yxhl.stationbiz.system.provider.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.TreeService;
import com.yxhl.stationbiz.system.domain.entity.ELProject;
import com.yxhl.stationbiz.system.domain.service.ELProjectService;
import com.yxhl.stationbiz.system.provider.dao.ELProjectDao;

@Service(value = "projectService")
public class ELProjectServiceImpl extends TreeService<ELProjectDao, ELProject> implements ELProjectService {

	@Autowired
	private ELProjectDao projectDao;

	@Override
	public Page<ELProject> selPageList(Page<ELProject> page, ELProject project) {
		List<ELProject> list = projectDao.selPageList(page, project);
		page.setRecords(list);
		return page;
	}
}