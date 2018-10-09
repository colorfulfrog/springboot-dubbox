package com.yxhl.stationbiz.system.domain.entity.schedule;


import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleBusTpl;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.ScheduleLoop;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusCheckStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusReportStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusRunStatusEnum;
import com.yxhl.stationbiz.system.domain.enums.ScheduleBusStatusEnum;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_schedule_bus
 *  注释:班次
 *  创建人: lw
 *  创建日期:2018-7-10 16:42:40
 */
@Data
@TableName(value="bs_schedule_bus")
public class ScheduleBus extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位（即营运单位） 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "所属线路 长度(32)")
	private java.lang.String lineId;
	
	@ApiModelProperty(value = "线路名称 长度(100) 必填")
	private java.lang.String lineName;
	
	@ApiModelProperty(value = "所属班次模板(bs_schedule_bus) 长度(32)")
	private java.lang.String scheduleTplId;
	
	@ApiModelProperty(value = "发车日期 ")
	private Date runDate;
	
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "发车时间 ")
	private Date runTime;
	
	@ApiModelProperty(value = "报班车站 长度(32)")
	private java.lang.String reportStaId;
	
	@JsonFormat(timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "报班时间 ")
	private java.util.Date reportTime;
	
	@ApiModelProperty(value = "报班车车牌号 长度(20)")
	private java.lang.String reportCarNo;
	
	@ApiModelProperty(value = "计划车车牌号 长度(20)")
	private java.lang.String planCarNo;
	
	@ApiModelProperty(value = "车辆类型：0普通、1豪华、2卧铺、3快车 长度(50)")
	private java.lang.String vehicleType;
	
	@ApiModelProperty(value = "班次号 长度(20)")
	private java.lang.String busCode;
	
	@ApiModelProperty(value = "班次状态1正常 2停班 3被并/停班 4并班 5配载 6被配载 ")
	private java.lang.Integer busStatus;
	
	@ApiModelProperty(value = "检票状态 1开检 2未检 ")
	private java.lang.Integer checkStatus;
	
	@ApiModelProperty(value = "报班状态 1已报班 2未报班 3已取消 ")
	private java.lang.Integer reportStatus;
	
	@ApiModelProperty(value = "售票状态 ")
	private java.lang.Integer saleStatus;
	
	@ApiModelProperty(value = "班次类型 1固定班 2流水班 ")
	private java.lang.Integer busType;
	
	@ApiModelProperty(value = "主驾驶员ID 长度(32)")
	private java.lang.String mainDriverId;
	
	@ApiModelProperty(value = "副驾驶员ID 长度(32)")
	private java.lang.String backupDriverId;
	
	@ApiModelProperty(value = "乘务员 长度(20)")
	private java.lang.String attendant;
	
	@ApiModelProperty(value = "发班状态 1已发班 0未发班2班次完成")
	private java.lang.Integer runStatus;
	
	@ApiModelProperty(value = "发班时间（司机发车时间）")
	private Date realRunTime;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@ApiModelProperty(value = "已售车票 ")
	private java.lang.Integer saleTickets;
	
	@ApiModelProperty(value = "检票口 长度(32)")
	private java.lang.String ticketGateId;
	
	@ApiModelProperty(value = "乘车库 长度(32)")
	private java.lang.String busEntranceId;
	
	@ApiModelProperty(value = "运行时间(小时) ")
	private java.lang.Float runDuration;
	
	@ApiModelProperty(value = "运营类别 0单营 1共营 长度(2)")
	private java.lang.String oprCategory;
	
	@ApiModelProperty(value = "营运方式：0直达 1城乡公交 2普通 长度(2)")
	private java.lang.String oprMode;
	
	@ApiModelProperty(value = "运行区域:0县内、1县际、2市际、3省际 长度(2)")
	private java.lang.String runArea;
	
	@ApiModelProperty(value = "过路班次0否 1是 ")
	private java.lang.Integer passBusFlag;
	
	@ApiModelProperty(value = "报到售票0否 1是 ")
	private java.lang.Integer reportTicketFlag;
	
	@ApiModelProperty(value = "双程班次0否 1是 ")
	private java.lang.Integer doubleBusFlag;
	
	@ApiModelProperty(value = "允许混检0否 1是 ")
	private java.lang.Integer allowMixedCheckFlag;
	
	@ApiModelProperty(value = "本站专营0否 1是 ")
	private java.lang.Integer specializeFlag;
	
	@ApiModelProperty(value = "票面打印信息 长度(100)")
	private java.lang.String ticketPrintInfo;
	
	@ApiModelProperty(value = "流水班间隔发班时间(分钟) ")
	private java.lang.Integer flowIntervalTime;
	
	@ApiModelProperty(value = "末班发车时间(时:分) ")
	private java.util.Date lastDepartureTime;
	
	@ApiModelProperty(value = "加班班次0否 1是 ")
	private java.lang.Integer overtimeBusFlag;
	
	@ApiModelProperty(value = "打印座位号0否 1是 ")
	private java.lang.Integer printSeatFlag;
	
	@ApiModelProperty(value = "需要驾驶人员才能报班0否 1是 ")
	private java.lang.Integer needDriverReportFlag;
	
	@ApiModelProperty(value = "驾驶人数 ")
	private java.lang.Integer driverNum;
	
	@ApiModelProperty(value = "打印发车时间0否 1是 ")
	private java.lang.Integer printStartTimeFlag;
	
	@ApiModelProperty(value = "是否自动报班0否 1是 ")
	private java.lang.Integer autoReportFlag;
	
	@ApiModelProperty(value = "携童数")
	private Integer carryChildrenNum;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "被并班次ID(本条记录是并入班次)")
	private String mergedBusId;
	@ApiModelProperty(value = "并班原因(数据字典) 1班次调整 2班次取消")
	private String mergeReason;
	@ApiModelProperty(value = "三联单号")
	private String triplicateBillNum;
	@ApiModelProperty(value = "三联单开单人")
	private String triplicateBiller;
	@ApiModelProperty(value = "始发站名称")
	private String startStation;
	@ApiModelProperty(value = "终点站名称")
	private String endStation;

	
	//扩展属性
	@TableField(exist=false)
	@ApiModelProperty(value = "营运单位")
	private String companyName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "始发站ID（发车站）")
	private String startStationId;

	
	@TableField(exist=false)
	@ApiModelProperty(value = "终点站")
	private String endStationId;

	
	@TableField(exist=false)
	@ApiModelProperty(value = "报班站点")
	private String reportStation;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "当前车站")
	private String currentStation;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "班次类型参数")
	private String busTypeParam;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "车辆ID")
	private String vehicleId;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "司机ID")
	private String driverId;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "主驾驶员名称")
	private java.lang.String mainDriverName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "副驾驶员名称")
	private java.lang.String backupDriverName;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "线路里程 ")
	private java.lang.Long lineMileage;
	
	//排班页面扩展字段
	@TableField(exist=false)
	@ApiModelProperty(value = "当前站已售")
	private Integer curStaSales;
	@TableField(exist=false)
	@ApiModelProperty(value = "其他站已售")
	private Integer otherStaSales;
	@TableField(exist=false)
	@ApiModelProperty(value = "已售合计")
	private Integer saleTotal;
	@TableField(exist=false)
	@ApiModelProperty(value = "余座 ")
	private java.lang.Integer remainSeats;
	@TableField(exist=false)
	@ApiModelProperty(value = "预留座位数")
	private Integer reservedSeats;
	@TableField(exist=false)
	@ApiModelProperty(value = "不售座位数")
	private Integer notSaleSeats;
	@TableField(exist=false)
	@ApiModelProperty(value = "检票口")
	private String ticketGateName;
	@TableField(exist=false)
	@ApiModelProperty(value = "乘车库")
	private String busEntranceName;
	@TableField(exist=false)
	@ApiModelProperty(value = "计划车型")
	private String planCarType;
	@TableField(exist=false)
	@ApiModelProperty(value = "途径站点")
	private String passStations;
	
	@ApiModelProperty(value = "开始日期 ")
	@TableField(exist=false)
	private Date startDate;
	
	@ApiModelProperty(value = "结束日期 ")
	@TableField(exist=false)
	private Date endDate;
	
	

	
	//检票列表新增字段 begin
	@TableField(exist=false)
	@ApiModelProperty(value = "售票数")
	private java.lang.Integer saleCount;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "检票数 ")
	private java.lang.Integer checkCount;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "未检票数 ")
	private java.lang.Integer notCheckCount;
	
	

	//检票列表新增字段 end

	@TableField(exist=false)
	@ApiModelProperty(value = "始发站票价")
	private String firstStationPrice;
	
	@TableField(exist=false)
	@JsonFormat(timezone="GMT+8",pattern="HH:mm")
	@ApiModelProperty(value = "始发时间(时:分) ")
	private Date startTime;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "可售")
	private Integer availableSeats;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "已售")
	private Integer alreadySeat;
	

	@TableField(exist=false)
	@ApiModelProperty(value = "从属班次编号")
	private String belongBusId;
	
	
	@TableField(exist=false)
	@ApiModelProperty(value = "待选班次编号")
	private String candidateBusId;
	

	@TableField(exist=false)
	@ApiModelProperty(value = "票价")
	private List<TicketCateValue> ticketCateValues;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "发车时间")
	private String runDateStr;
	
	@TableField(exist=false)
	@ApiModelProperty(value = "查询条件（班次ID）")
	private String scheduleBusId;
	
	public ScheduleBus() {
		
	}
	
	public ScheduleBus (ScheduleLoop loop) {
		this.setId(IdWorker.get32UUID());
		this.setOrgId(loop.getOrgId());
		this.setCompId(loop.getCompId());
		this.setLineId(loop.getLineId());
		this.setLineName(loop.getLineName());
		this.setScheduleTplId(loop.getTplId());
		this.setRunDate(new Date());
		this.setRunTime(loop.getStartTime());
		this.setBusCode(loop.getBusCode());
		this.setBusStatus(ScheduleBusStatusEnum.NORMAL.getType());
		this.setCheckStatus(ScheduleBusCheckStatusEnum.NOT_CHECK.getType());
		this.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
		this.setBusType(loop.getRunFlowFlag()==0?1:2);
		this.setRunStatus(ScheduleBusRunStatusEnum.NOT_RUN.getType());
	}
	
	/**
	 * 根据班次模板生成班次
	 * @param tpl 班次模板
	 * @param runDate 发车日期
	 */
	public ScheduleBus (ScheduleBusTpl tpl, Date runDate) {
		this.setId(IdWorker.get32UUID());
		this.setOrgId(tpl.getOrgId());
		this.setCompId(tpl.getCompId());
		this.setLineId(tpl.getLineId());
		this.setLineName(tpl.getLineName());
		this.setScheduleTplId(tpl.getId());
		this.setRunDate(runDate);
		this.setRunTime(tpl.getStartTime());
		this.setBusCode(tpl.getBusCode());
		this.setBusStatus(ScheduleBusStatusEnum.NORMAL.getType());
		this.setCheckStatus(ScheduleBusCheckStatusEnum.NOT_CHECK.getType());
		this.setReportStatus(ScheduleBusReportStatusEnum.NOT_REPORT.getType());
		this.setBusType(tpl.getRunFlowFlag()==0?1:2);//RunFlowFlag 表示是否为流水班  0否   1是
		this.setRunStatus(ScheduleBusRunStatusEnum.NOT_RUN.getType());
		this.setSeats(tpl.getPersons());
		this.setTicketGateId(tpl.getTicketGateId());
		this.setBusEntranceId(tpl.getBusEntranceId());
		this.setRunDuration(tpl.getRunTime());
		this.setOprCategory(tpl.getOprCategory());
		this.setOprMode(tpl.getOprMode());
		this.setRunArea(tpl.getRunArea());
		this.setPassBusFlag(tpl.getPassBusFlag());
		this.setReportTicketFlag(tpl.getReportTicketFlag());
		this.setDoubleBusFlag(tpl.getDoubleBusFlag());
		this.setAllowMixedCheckFlag(tpl.getAllowMixedCheckFlag());
		this.setSpecializeFlag(tpl.getSpecializeFlag());
		this.setTicketPrintInfo(tpl.getTicketPrintInfo());
		this.setFlowIntervalTime(tpl.getFlowIntervalTime());
		this.setLastDepartureTime(tpl.getLastDepartureTime());
		this.setOvertimeBusFlag(tpl.getOvertimeBusFlag());
		this.setPrintSeatFlag(tpl.getPrintSeatFlag());
		this.setNeedDriverReportFlag(tpl.getNeedDriverReportFlag());
		this.setDriverNum(tpl.getDriverNum());
		this.setPrintStartTimeFlag(tpl.getPrintStartTimeFlag());
		this.setAutoReportFlag(tpl.getAutoReportFlag());
		this.setVehicleType(tpl.getVehicleType());
	}
}
