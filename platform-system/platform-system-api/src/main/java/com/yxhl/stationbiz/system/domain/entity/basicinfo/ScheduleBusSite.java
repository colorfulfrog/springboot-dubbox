package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_bus_site
 *  注释:班次停靠点
 *  创建人: lw
 *  创建日期:2018-7-12 19:15:10
 */
@Data
@TableName(value="bs_schedule_bus_site")
public class ScheduleBusSite extends ELItem{
   

	private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "关联班次调度表 长度(32)")
	private java.lang.String scheduleBusTplId;
	
	@ApiModelProperty(value = "停靠站点 （bs_station.id） 长度(32)")
	private java.lang.String stationId;
	
	@ApiModelProperty(value = "运行时间(小时） ")
	private java.lang.String runTime;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间(时:分) ")
	private Date departureTime;
	
	@ApiModelProperty(value = "检票口 ")
	private java.lang.String ticketGateId;
	
	@ApiModelProperty(value = "乘车库 ")
	private java.lang.String busEntranceId;
	
	@ApiModelProperty(value = "是否始发站0否  1是 ")
	private java.lang.Integer firstSiteFlag;
	
	@ApiModelProperty(value = "是否允许售票0否  1是 ")
	private java.lang.Integer allowTicketFlag;
	
	@ApiModelProperty(value = "备注 长度(500)")
	private java.lang.String remark;
	
	@ApiModelProperty(value = "0正常、1关闭 ")
	private java.lang.Integer status;
	
	@ApiModelProperty(value = "排序")
	private java.lang.Integer sort;
	
	
	@ApiModelProperty(value = "停靠站点名称")
	@TableField(exist=false)
	private java.lang.String stationName;
	
	@ApiModelProperty(value = "停靠站点简拼")
	@TableField(exist=false)
	private java.lang.String spell;
	
	@ApiModelProperty(value = "乘车库名称")
	@TableField(exist=false)
	private java.lang.String entranceName;
	
	@ApiModelProperty(value = "检票口位置")
	@TableField(exist=false)
	private java.lang.String gateName;
	
	
	
	public ScheduleBusSite(LineSite lineite) {
		 this.setId(IdWorker.get32UUID());
		 this.setOrgId(lineite.getOrgId());
		 this.setCompId(lineite.getCompId());
		 this.setStationId(lineite.getStationId());
		 this.setStatus(0); //0启用、1不启用
		 this.setSort(lineite.getSort()); 
	}



	public ScheduleBusSite() {
		super();
	}
	
}
