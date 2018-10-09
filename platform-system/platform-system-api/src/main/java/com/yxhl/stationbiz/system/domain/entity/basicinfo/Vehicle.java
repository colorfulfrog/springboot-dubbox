package com.yxhl.stationbiz.system.domain.entity.basicinfo;


import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import com.yxhl.platform.common.utils.DateHelper;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:bs_vehicle
 *  注释:车辆表
 *  创建人: xjh
 *  创建日期:2018-7-12 15:38:43
 */
@Data
@TableName(value="bs_vehicle")
public class Vehicle extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "车牌号 长度(20) 必填")
	private java.lang.String carNo;
	
	@ApiModelProperty(value = "上牌时间 ")
	private java.util.Date licenseApplyDate;
	
	@ApiModelProperty(value = "临时车 1 临时车；2非临时车 ")
	private java.lang.Integer tempCar;
	
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "结算单位 长度(32)")
	private java.lang.String balaCompId;
	
	@ApiModelProperty(value = "厂牌型号 长度(10)")
	private java.lang.String brandModel;
	
	@ApiModelProperty(value = "车辆状态 0营运、1停运、2报废")
	private java.lang.Integer vehicleStatus;
	
	@ApiModelProperty(value = "燃料类型 0汽油、1柴油、2天然气，3电动、4油电混合、5油气混合")
	private java.lang.Integer fuelType;
	
	@ApiModelProperty(value = "结算车站 长度(32)")
	private java.lang.String balanceStationId;
	
	@ApiModelProperty(value = "有效期 ")
	private java.util.Date expiryDate;
	
	@ApiModelProperty(value = "营运类型 0本站公营车，1外站公营车，2本站承包车，3外站承包车")
	private java.lang.Integer operationType;
	
	@ApiModelProperty(value = "IC卡号 长度(20)")
	private java.lang.String icCardNo;
	
	@ApiModelProperty("运营线路")
	private String lineId;
	
	@ApiModelProperty(value = "核定座位数 ")
	private java.lang.Integer approvedSeats;
	
	@ApiModelProperty(value = "乘客座位数 ")
	private java.lang.Integer passengerSeats;
	
	@ApiModelProperty(value = "车牌颜色 0蓝色、1黄色、2黑色，3白色，4绿色")
	private java.lang.String carBrandColor;
	
	@ApiModelProperty(value = "车身颜色 0蓝色、1黄色、2黑色，3白色，4绿色")
	private java.lang.String carBodyColor;
	
	@ApiModelProperty(value = "发动机号 长度(20)")
	private java.lang.String engineNo;
	
	@ApiModelProperty(value = "车架号 长度(20)")
	private java.lang.String frameNo;
	
	@ApiModelProperty(value = "载重 ")
	private java.lang.Float carLoad;
	
	@ApiModelProperty(value = "联系人 长度(10)")
	private java.lang.String contact;
	
	@ApiModelProperty(value = "联系电话 长度(20)")
	private java.lang.String telephone;
	
	@ApiModelProperty(value = "gprs码 长度(32)")
	private java.lang.String gprsCode;
	
	@ApiModelProperty(value = "座位类型 0普通座，1商务座，2卧铺")
	private java.lang.Integer seatCategory;
	
	@ApiModelProperty(value = "需证件审核才能报班: 1 需要审核；0 不需要审核 ")
	private java.lang.Integer needVerify;
	
	@ApiModelProperty(value = "行李舱最大载重量 ")
	private java.lang.Float luggageMaxLoad;
	
	@ApiModelProperty(value = "最大容量 ")
	private java.lang.Float maxCapacity;
	
	@ApiModelProperty(value = "是否支持快递 1支持；0不支持 ")
	private java.lang.Integer expressSupport;
	
	@ApiModelProperty(value = "第三者责任险编号 长度(20)")
	private java.lang.String thirdInsNo;
	
	@ApiModelProperty(value = "第三者责任险有效期起始日期 ")
	private java.util.Date thirdInsStartDate;
	
	@ApiModelProperty(value = "第三者责任险有效期截止日期 ")
	private java.util.Date thirdInsEndDate;
	
	@ApiModelProperty(value = "承运险编号 长度(20)")
	private java.lang.String carrierInsNo;
	
	@ApiModelProperty(value = "承运险有效期起始日期 ")
	private java.util.Date carrierInsStartDate;
	
	@ApiModelProperty(value = "承运险有效期截止日期 ")
	private java.util.Date carrierInsEndDate;
	
	@ApiModelProperty(value = "交强险编号 长度(20)")
	private java.lang.String jqxInsNo;
	
	@ApiModelProperty(value = "交强险有效期起始日期 ")
	private java.util.Date jqxInsStartDate;
	
	@ApiModelProperty(value = "交强险有效期截止日期 ")
	private java.util.Date jqxInsEndDate;
	
	@ApiModelProperty(value = "行驶证号 长度(32)")
	private java.lang.String driveLicNo;
	
	@ApiModelProperty(value = "行驶证有效起始日期 ")
	private java.util.Date driveLicStartDate;
	
	@ApiModelProperty(value = "行驶证有效截止日期 ")
	private java.util.Date driveLicEndDate;
	
	@ApiModelProperty(value = "营运证号 长度(32)")
	private java.lang.String opeCertNo;
	
	@ApiModelProperty(value = "营运证有效起始日期 ")
	private java.util.Date opeCertStartDate;
	
	@ApiModelProperty(value = "营运证有效截止日期 ")
	private java.util.Date opeCertEndDate;
	
	@ApiModelProperty(value = "进站协议号 长度(32)")
	private java.lang.String inStationNo;
	
	@ApiModelProperty(value = "进站有效起始日期 ")
	private java.util.Date inStationStartDate;
	
	@ApiModelProperty(value = "进站证有效截止日期 ")
	private java.util.Date inStationEndDate;
	
	@ApiModelProperty(value = "二维证有效截止日期 ")
	private java.util.Date twoDimeEndDate;
	
	@ApiModelProperty(value = "二维证件号 长度(32)")
	private java.lang.String twoDimeNo;
	
	@ApiModelProperty(value = "需驾驶员才能报班: 1 需要驾驶员；0 不需要驾驶员 ")
	private java.lang.Integer needDriver;
	
	@ApiModelProperty(value = "下次二级维护里程 ")
	private java.lang.Float nextMtMile;
	
	@ApiModelProperty(value = "线路标志牌 长度(20)")
	private java.lang.String lineMark;
	
	@ApiModelProperty(value = "线路标志牌有效起始日期 ")
	private java.util.Date lineMarkStartDate;
	
	@ApiModelProperty(value = "线路标志牌截止日期 ")
	private java.util.Date lineMarkEndDate;
	
	@ApiModelProperty(value = "保证金 ")
	private java.lang.Float cautionMoney;
	
	@ApiModelProperty(value = "停车费 ")
	private java.lang.Float parkFee;
	
	@ApiModelProperty(value = "进站卡号 长度(20)")
	private java.lang.String inStaCardNo;
	
	@ApiModelProperty(value = "例检费 ")
	private java.lang.Float inspectFee;
	
	@ApiModelProperty(value = "其他费用 ")
	private java.lang.Float otherFee;
	
	@ApiModelProperty(value = "出厂日期 ")
	private java.util.Date productDate;
	
	@ApiModelProperty(value = "核准线路 长度(100)")
	private java.lang.String allowLine;
	
	@ApiModelProperty(value = "备注 长度(64)")
	private java.lang.String remark;
	
	@ApiModelProperty(value = "车辆图片 长度(200)")
	private java.lang.String vehicleImg;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属单位 名称")
	private String fullName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "创建人 名称")
	private String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改人 名称")
	private String xgrName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "座位类型名称 ")   
	private String seatCategoryName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "线路 名称")
	private String lineName;
	 
	@ApiModelProperty(value = "车辆运营线路:1本单位所有线路 0:取line_id绑定的单条线路")
	private String allCompLines;
	
	/**
	 * 验证车辆证件有效期
	 * @return
	 */
	public boolean verifyCert() {
		return (this.getThirdInsStartDate()==null||this.getThirdInsEndDate()==null?true:DateHelper.containDate(new Date(), this.getThirdInsStartDate(), this.getThirdInsEndDate()))
			&& (this.getCarrierInsStartDate()==null||this.getCarrierInsEndDate()==null?true:DateHelper.containDate(new Date(), this.getCarrierInsStartDate(), this.getCarrierInsEndDate()))
			&& (this.getJqxInsStartDate()==null||this.getJqxInsEndDate()==null?true:DateHelper.containDate(new Date(), this.getJqxInsStartDate(), this.getJqxInsEndDate()))
			&& (this.getDriveLicStartDate()==null||this.getDriveLicEndDate()==null?true:DateHelper.containDate(new Date(), this.getDriveLicStartDate(), this.getDriveLicEndDate()))
			&& (this.getOpeCertStartDate()==null||this.getOpeCertEndDate()==null?true:DateHelper.containDate(new Date(), this.getOpeCertStartDate(), this.getOpeCertEndDate()))
			&& (this.getInStationStartDate()==null||this.getInStationEndDate()==null?true:DateHelper.containDate(new Date(), this.getInStationStartDate(), this.getInStationEndDate()));
	}
}
