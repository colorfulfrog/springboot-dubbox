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
 *  表名:bs_driver
 *  注释:驾驶员表
 *  创建人: xjh
 *  创建日期:2018-7-17 15:34:09
 */
@Data
@TableName(value="bs_driver")
public class Driver extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "姓名 长度(20) 必填")
	private java.lang.String driverName;
	
	@ApiModelProperty(value = "简拼 长度(10)")
	private java.lang.String shortSpell;
	
	@ApiModelProperty(value = "工号 长度(20)")
	private java.lang.String employeeId;
	
	@ApiModelProperty(value = "IC卡号 长度(20)")
	private java.lang.String icCardNo;
	
	@ApiModelProperty(value = "档案号 长度(20)")
	private java.lang.String archiveNo;
	
	@ApiModelProperty(value = "性别: 1 男；2 女 ")
	private java.lang.Integer sex;
	
	@ApiModelProperty(value = "相片 长度(200)")
	private java.lang.String photo;
	
	@ApiModelProperty(value = "工资 ")
	private java.lang.Float salary;
	
	@ApiModelProperty(value = "身份证 长度(20) 必填")
	private java.lang.String identityCard;
	
	@ApiModelProperty(value = "出生日期 ")
	private java.util.Date birthDate;
	
	@ApiModelProperty(value = "政治面貌：1群众、2共青团员，3共产党员，4其他党派 ")
	private java.lang.Integer politicalStatus;
	
	@ApiModelProperty(value = "文化程度: 1小学，2初中，3高中，4中专，5大专，6本科，7硕士，8博士 ")
	private java.lang.Integer educationalLevel;
	
	@ApiModelProperty(value = "籍贯 长度(32)")
	private java.lang.String birthPlace;
	
	@ApiModelProperty(value = "招考日期 ")
	private java.util.Date recruitDate;
	
	@ApiModelProperty(value = "移动电话 长度(20)")
	private java.lang.String mobile;
	
	@ApiModelProperty(value = "联系电话 长度(20)")
	private java.lang.String telephone;
	
	@ApiModelProperty(value = "家庭住址 长度(64)")
	private java.lang.String address;
	
	@ApiModelProperty(value = "隶属车队 长度(20)")
	private java.lang.String carTeam;
	
	@ApiModelProperty(value = "隶属分队 长度(20)")
	private java.lang.String department;
	
	@ApiModelProperty(value = "建制车号 长度(20)")
	private java.lang.String buildCarNum;
	
	@ApiModelProperty(value = "驾驶证类别: 1 A1,2 A2, 3 A3,4 B1, 5 B2,6 C1,7 C2,8 C3, 9 C4, 10 其他 ")
	private java.lang.Integer driverLicenseCategory;
	
	@ApiModelProperty(value = "驾驶员状态：1在岗，2停驾，3辞退，4退休，5待班，6转出，7试用 ")
	private java.lang.Integer driverLicenseStatus;
	
	@ApiModelProperty(value = "初始里程 ")
	private java.lang.Float initialMileage;
	
	@ApiModelProperty(value = "是否临时工 0:临时工 1 正式工 ")
	private java.lang.Integer temporaryWorkerFlag;
	
	@ApiModelProperty(value = "签订责任书 0 未签订；1已签订 ")
	private java.lang.Integer signResponsibility;
	
	@ApiModelProperty(value = "准驾类型 ")
	private java.lang.Integer allowDriveType;
	
	@ApiModelProperty(value = "运营线路 长度(100)")
	private java.lang.String line;
	
	@ApiModelProperty(value = "准驾证号 长度(20)")
	private java.lang.String driveCertNo;
	
	@ApiModelProperty(value = "准驾证发证日期 ")
	private java.util.Date driveCertDate;
	
	@ApiModelProperty(value = "准驾证截止日期 ")
	private java.util.Date driveCertEndDate;
	
	@ApiModelProperty(value = "从业资格证号 长度(20)")
	private java.lang.String qualificationCertNo;
	
	@ApiModelProperty(value = "从业资格证发证日期 ")
	private java.util.Date qualificationCertDate;
	
	@ApiModelProperty(value = "从业资格证截止日期 ")
	private java.util.Date qualificationCertEndDate;
	
	@ApiModelProperty(value = "驾驶证号 长度(32)")
	private java.lang.String driveLicense;
	
	@ApiModelProperty(value = "准驾车型 长度(10)")
	private java.lang.String driveCarType;
	
	@ApiModelProperty(value = "驾驶证有效起始日期 ")
	private java.util.Date licenseStartDate;
	
	@ApiModelProperty(value = "驾驶证有效截止日期 ")
	private java.util.Date licenseEndDate;
	
	@ApiModelProperty(value = "驾驶证初次领证日期 ")
	private java.util.Date licenseDate;
	
	@ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "所属单位 名称")
	private String fullName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "创建人 名称")
	private String userName;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "修改人 名称")
	private String xgrName;
	
	@ApiModelProperty(value = "初次绑定车辆时间")
	private java.util.Date bindTime;
	
	@ApiModelProperty(value = "修改绑定车辆时间")
	private java.util.Date updateBindTime;
	
	/**
	 * 验证司机各种证件有效期
	 * @return
	 */
	public boolean verifyCert() {
		return (this.getDriveCertDate()==null||this.getDriveCertEndDate()==null?true:DateHelper.containDate(new Date(), this.getDriveCertDate(), this.getDriveCertEndDate())) //准驾证
			&& (this.getQualificationCertDate()==null||this.getQualificationCertEndDate()==null?true:DateHelper.containDate(new Date(), this.getQualificationCertDate(), this.getQualificationCertEndDate())) //从业资格证
			&& (this.getLicenseDate()==null||this.getLicenseEndDate()==null?true:DateHelper.containDate(new Date(), this.getLicenseDate(), this.getLicenseEndDate())); //驾驶证
	}
}
