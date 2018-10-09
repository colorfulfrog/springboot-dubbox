package com.yxhl.stationbiz.system.provider.serviceimpl.sys;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.service.CrudService;
import org.springframework.transaction.annotation.Transactional;
import com.yxhl.stationbiz.system.provider.dao.sys.ConfigDao;
import com.yxhl.stationbiz.system.domain.entity.sys.Config;
import com.yxhl.stationbiz.system.domain.service.sys.ConfigService;
/**
 * @ClassName: ConfigServiceImpl
 * @Description: 参数配置表 serviceImpl
 * @author xjh
 * @date 2018-7-12 16:14:50
 */
@Transactional(readOnly = true)
@Service("configService")
public class ConfigServiceImpl extends CrudService<ConfigDao, Config> implements ConfigService {
	@Autowired
	private ConfigDao configDao;

	@Override
	public Page<Config> selPageList(Page<Config> page, Config config) {
		List<Config> list = configDao.selPageList(page, config);
		page.setRecords(list);
		return page;
	}

	@Override
	public List<Config> exportData(Config config) {
		List<Config> exportData = configDao.exportData(config);
		return exportData;
	}

	@Override
	public String selByCode(String orgId, String code) {
		Config config = configDao.selByCode(orgId,code);
		return config!=null?config.getValue():null;
	}
}
