package com.yxhl.stationbiz.system.domain.entity.schedule;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_exec_price
 *  注释:执行票价表
 *  创建人: lw
 *  创建日期:2018-8-21 10:23:17
 */
@Data
@TableName(value="bs_exec_price")
public class ExecPrice extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "班次ID,关联班次表(bs_schedule_bus) 长度(32)")
	private java.lang.String scheduleBusId;
	
	@ApiModelProperty(value = "上车站 长度(32)")
	private java.lang.String onStaId;
	
	@ApiModelProperty(value = "下车站 长度(32)")
	private java.lang.String offStaId;
	
	@ApiModelProperty(value = "座位类型 0普通 1商务 2卧铺 长度(2)")
	private java.lang.String seatCate;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@ApiModelProperty(value = "座位号 0全部 其他数字表示具体座位号 ")
	private java.lang.Integer seatNo;
	
	
	
	
	/**
	 *  以下为扩展字段 
	 * 
	 */
	
	@TableField(exist=false)
	@ApiModelProperty(value = "上车站")
	private String onStaName;

	@TableField(exist=false)
	@ApiModelProperty(value = "到达站")
	private String offStaName;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "是否允许售票0否  1是 ")
	private java.lang.Integer allowTicketFlag;
	

	@TableField(exist=false)
	@ApiModelProperty(value = "运行时间(小时） ")
	private java.lang.Integer runTime;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "距始发站里程（公里） ")
	private java.lang.Integer startStationDistance;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "距始发站结算里程（公里） ")
	private java.lang.Integer settlementDistance;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "预留座位数")
	private Integer reservedSeats;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "不售座位数")
	private Integer notSaleSeats;
	
	public ExecPrice() {
		super();
	}

	public ExecPrice(BasicPrice basicPrice) {
		super();
		this.setOrgId(basicPrice.getOrgId());
		this.setCompId(basicPrice.getCompId());
		this.setOnStaId(basicPrice.getOnStaId());
		this.setOffStaId(basicPrice.getOffStaId());
		this.setSeatCate(basicPrice.getSeatCate());
		this.setSeats(basicPrice.getSeats());
		this.setSeatNo(basicPrice.getSeatNo());
		this.setCreateBy(basicPrice.getCreateBy());
		this.setUpdateBy(basicPrice.getUpdateBy());
		this.setCreateTime(basicPrice.getCreateTime());
		this.setUpdateTime(basicPrice.getUpdateTime());
	}
	
	public ExecPrice(HolidayPrice holidayPrice) {
		super();
		this.setOrgId(holidayPrice.getOrgId());
		this.setCompId(holidayPrice.getCompId());
		this.setOnStaId(holidayPrice.getOnStaId());
		this.setOffStaId(holidayPrice.getOffStaId());
		this.setSeatCate(holidayPrice.getSeatCate());
		this.setSeats(holidayPrice.getSeats());
		this.setSeatNo(holidayPrice.getSeatNo());
		this.setCreateBy(holidayPrice.getCreateBy());
		this.setUpdateBy(holidayPrice.getUpdateBy());
		this.setCreateTime(holidayPrice.getCreateTime());
		this.setUpdateTime(holidayPrice.getUpdateTime());
	}
	
	
	
}
