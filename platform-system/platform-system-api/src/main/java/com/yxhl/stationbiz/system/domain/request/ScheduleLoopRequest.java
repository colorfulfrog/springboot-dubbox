package com.yxhl.stationbiz.system.domain.request;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_loop
 *  注释:班次调度循环配置
 *  创建人: lw
 *  创建日期:2018-7-11 9:27:23
 */
@Data
public class ScheduleLoopRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @TableField(exist=false)
	@ApiModelProperty(value = "所属单位")
	private String companyName;
    
	@ApiModelProperty(value = "关联线路 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "开始日期 起始")
	private Date startDateBegin;
	@ApiModelProperty(value = "开始日期 至")
	private Date startDateEnd;
	
	@ApiModelProperty(value = "结束日期 起始")
	private Date endDateBegin;
	@ApiModelProperty(value = "开始日期 至")
	private Date endDateEnd;
}
