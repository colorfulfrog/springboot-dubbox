package com.yxhl.stationbiz.system.domain.entity.schedule;


import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_holiday
 *  注释:节日设置表
 *  创建人: ypf
 *  创建日期:2018-8-13 17:41:22
 */
@Data
@TableName(value="bs_holiday")
public class Holiday extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "节日名称 长度(32)")
	private java.lang.String holidayName;
	
	@ApiModelProperty(value = "别称 长度(32)")
	private java.lang.String nickName;
	
	@ApiModelProperty(value = "开始日期 ")
	private java.util.Date beginDate;
	
	@ApiModelProperty(value = "结束日期 ")
	private java.util.Date endDate;
	
}
