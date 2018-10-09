package com.yxhl.stationbiz.system.domain.request;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	运营计划请求实体类
 *  创建人: lw
 */
@Data
public class CreateScheduleBusRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "所属单位")
	private java.lang.String compId;
    
    @ApiModelProperty(value = "线路ID")
    private java.lang.String lineId;
    
    @ApiModelProperty(value = "班次模板ID列表")
	private List<String> scheduleTplList;
    
    @ApiModelProperty(value = "操作；currentSel:当前选中；line:本线路所有班次；company:本单位所有班次")
    private String operation;
    
    @ApiModelProperty(value = "操作类型；create:制作计划；delete:删除计划")
    private String operateType;
	
	@ApiModelProperty(value = "制作日期 起始")
	private Date startDate;
	@ApiModelProperty(value = "制作日期 至")
	private Date endDate;
}
