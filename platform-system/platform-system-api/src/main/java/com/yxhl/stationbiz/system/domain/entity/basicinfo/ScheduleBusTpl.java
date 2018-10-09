package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_bus_tpl
 *  注释:班次模板
 *  创建人: lw
 *  创建日期:2018-7-11 9:58:54
 */
@Data
@TableName(value="bs_schedule_bus_tpl")
public class ScheduleBusTpl extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "运营线路 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "班次号 长度(50)")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "运行时间(小时） ")
	private float runTime;
	
	@ApiModelProperty(value = "检票口 ")
	private java.lang.String ticketGateId;
	
	@ApiModelProperty(value = "乘车库 ")
	private java.lang.String busEntranceId;
	
	@ApiModelProperty(value = "运营类别 0单营 1共营 长度(50)")
	private java.lang.String oprCategory;
	
	@ApiModelProperty(value = "定员 ")
	private java.lang.Integer persons;
	
	@ApiModelProperty(value = "运行区域:0县内、1县际、2市际、3省际 长度(50)")
	private java.lang.String runArea;
	
	@ApiModelProperty(value = "营运方式：0直达  1城乡公交  2普通 长度(50)")
	private java.lang.String oprMode;
	
	@ApiModelProperty(value = "车辆类型：0普通、1豪华、2卧铺、3快车 长度(50)")
	private java.lang.String vehicleType;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "始发时间(时:分) ")
	private Date startTime;
	
	@ApiModelProperty(value = "票面打印信息 长度(100)")
	private java.lang.String ticketPrintInfo;
	
	@ApiModelProperty(value = "途经站点 长度(300)")
	private java.lang.String passSite;
	
	@ApiModelProperty(value = "终到车站 长度(100)")
	private java.lang.String endStation;
	
	@ApiModelProperty(value = "流水班次 0否  1是 ")
	private java.lang.Integer runFlowFlag;
	
	@ApiModelProperty(value = "流水班间隔发班时间(分钟) ")
	private java.lang.Integer flowIntervalTime;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "末班发车时间(时:分) ")
	private Date lastDepartureTime;
	
	@ApiModelProperty(value = "过路班次0否  1是 ")
	private java.lang.Integer passBusFlag;
	
	@ApiModelProperty(value = "加班班次0否  1是 ")
	private java.lang.Integer overtimeBusFlag;
	
	@ApiModelProperty(value = "双程班次0否  1是 ")
	private java.lang.Integer doubleBusFlag;
	
	@ApiModelProperty(value = "报到售票0否  1是 ")
	private java.lang.Integer reportTicketFlag;
	
	@ApiModelProperty(value = "允许混检0否  1是 ")
	private java.lang.Integer allowMixedCheckFlag;
	
	@ApiModelProperty(value = "本站专营0否  1是 ")
	private java.lang.Integer specializeFlag;
	
	@ApiModelProperty(value = "打印座位号0否  1是 ")
	private java.lang.Integer printSeatFlag;
	
	@ApiModelProperty(value = "同时修改其他班次0否  1是 ")
	private java.lang.Integer upSameTimeFlag;
	
	@ApiModelProperty(value = "需要驾驶人员才能报班0否  1是 ")
	private java.lang.Integer needDriverReportFlag;
	
	@ApiModelProperty(value = "驾驶人数 ")
	private java.lang.Integer driverNum;
	
	@ApiModelProperty(value = "打印发车时间0否  1是 ")
	private java.lang.Integer printStartTimeFlag;
	
	@ApiModelProperty(value = "是否自动报班0否  1是 ")
	private java.lang.Integer autoReportFlag;
	
	@ApiModelProperty(value = "始发站票价 ")
	private java.math.BigDecimal firstStationPrice;
	
	@ApiModelProperty(value = "备注 长度(500)")
	private java.lang.String remark;
	
	@ApiModelProperty(value = "是否立即生成当天班次 0 否 1是 ")
	private java.lang.Integer immediateGenerFlag;
	
	@ApiModelProperty(value = "0正常、1长停 ")
	private java.lang.Integer status;
	
	
	/**
	 * 扩展属性
	 */
	@ApiModelProperty(value = "所属单位名称")
	@TableField(exist=false)
	private java.lang.String fullName;
	
	@ApiModelProperty(value = "线路名称")
	@TableField(exist=false)
	private java.lang.String lineName;
	
	@ApiModelProperty(value = "运营类别 0单营 1共营 长度(50)")
	@TableField(exist=false)
	private java.lang.String oprCategoryName;
	
	@ApiModelProperty(value = "运行区域:0县内、1县际、2市际、3省际 长度(50)")
	@TableField(exist=false)
	private java.lang.String runAreaName;
	
	@ApiModelProperty(value = "营运方式：0直达  1城乡公交  2普通 长度(50)")
	@TableField(exist=false)
	private java.lang.String oprModeName;	
	
	
	@ApiModelProperty(value = "车辆类型：0直达  1城乡公交  2普通 长度(50)")
	@TableField(exist=false)
	private java.lang.String vehicleTypeName;	
	

	@ApiModelProperty(value = "乘车库名称")
	@TableField(exist=false)
	private java.lang.String entranceName;
	
	@ApiModelProperty(value = "检票口位置")
	@TableField(exist=false)
	private java.lang.String gateName;
	
	@ApiModelProperty(value = "正班班次0否  1是 ")
	@TableField(exist=false)
	private java.lang.Integer overtimeBusFlag2;
	
}
