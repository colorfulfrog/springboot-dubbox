package com.yxhl.stationbiz.system.domain.service.basicinfo;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.yxhl.platform.common.service.IELService;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;

/**
 *  LineService
 *  注释:线路表Service
 *  创建人: xjh
 *  创建日期:2018-7-10 14:54:42
 */
public interface LineService extends IELService<Line>{
	
	/**
	 * 分页查询
	 * @param page 分页参数
	 * @param line 条件参数
	 * @return 当前页数据
	 */
	Page<Line> selPageList(Page<Line> page,Line line);

	/**
	 * 根据 编号查询
	 * @param line
	 * @return
	 */
	Line selById(String id);

	/**
	 * 添加线路
	 * @param line
	 * @return
	 */
	boolean insertLine(Line line);

	/**
	 * 删除线路
	 * @param lineIds
	 * @return
	 */
	boolean delLine(List<String> lineIds);

	/**
	 * 修改线路
	 * @param line
	 * @return
	 */
	boolean updateLine(Line line);  
}
