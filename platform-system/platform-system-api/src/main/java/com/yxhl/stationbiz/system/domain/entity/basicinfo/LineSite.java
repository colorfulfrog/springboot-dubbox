package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.yxhl.platform.common.entity.ELItem;
import com.yxhl.platform.common.entity.ELUser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_line_site
 *  注释:线路停靠点
 *  创建人: lw
 *  创建日期:2018-7-11 9:56:16
 */
@Data
@TableName(value="bs_line_site")
public class LineSite extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "线路关联 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "站点编号 长度(32)")
	private java.lang.String stationId;
	
	
	@ApiModelProperty(value = "是否为停靠点0否 1是 ")
	private java.lang.Integer siteFlag;
	
	@ApiModelProperty(value = "距始发站里程（公里） ")
	private java.lang.String startStationDistance;
	
	@ApiModelProperty(value = "距始发站结算里程（公里） ")
	private java.lang.String settlementDistance;
	
	@ApiModelProperty(value = "0正常、1关闭 ")
	private java.lang.Integer status;
	
	@ApiModelProperty(value = "排序")
	private java.lang.Integer sort;
	
	@ApiModelProperty(value = "联系人")
	private java.lang.String contacts;
	
	@ApiModelProperty(value = "联系电话")
	private java.lang.String telephone;
	
	@ApiModelProperty(value = "简拼")
	private java.lang.String spell;
	
	
	
	
	/**
	 * 扩展属性
	 */
	@ApiModelProperty(value = "站点名称 长度(100)")
	@TableField(exist=false)
	private java.lang.String siteName;




	public LineSite() {
		super();
	}




	public LineSite(Line line,Station station) {
		super();
		this.setLineId(line.getId()); 
		this.setCreateBy(line.getCreateBy());
		this.setUpdateBy(line.getUpdateBy()); 
		this.setOrgId(line.getOrgId());
		this.setCompId(line.getCompId()); 
		this.setStatus(0); //正常
		this.setStationId(station.getId()); 
		this.setContacts(station.getContacts());
		this.setTelephone(station.getTelephone());
		this.setSpell(station.getSpell()); 
		this.setSiteFlag(1);//是否为停靠点0否 1是
		this.setStartStationDistance("0");
		this.setSettlementDistance("0"); 
	}
	
	
	
}
