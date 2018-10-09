package com.yxhl.stationbiz.system.domain.entity.sys;



import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户管理
 * @author ypf
 *
 */
@Data
@TableName(value="sys_user")
public class User extends ELItem{
	

	@ApiModelProperty("用户编码")
	private String userCode;
	
	@ApiModelProperty("用户名称")
	private String userName;
	
	@ApiModelProperty("用户密码")
	private String userPwd;
	
	@ApiModelProperty("昵称")
	private String nickName;
	
	@ApiModelProperty("头像")
	private String avatar;
	
	@ApiModelProperty("员工卡")
	private String employId;
	
	@ApiModelProperty("折扣率")
	private Integer discountRate;
	
	@ApiModelProperty("联系电话")
	private String telphone;
	
	@ApiModelProperty("身份证")
	private String identityCard;
	
	@ApiModelProperty("邮箱")
	private String email;
	
	@ApiModelProperty("多点登录 1 允许多点登录 0 不允许多点登录")
	private Integer multiLogin;
	
	@ApiModelProperty("用户类型 1、超级管理员 2、普通用户")
	private Integer userType;
	
	@ApiModelProperty("所属机构")
	private String orgId;
	
	@ApiModelProperty("所属单位")
	private String companyId;
	
	@ApiModelProperty("所属站点")
	private String stationId;
	
	@ApiModelProperty("版本号")
	private Long version;
	
	@ApiModelProperty("友盟设备号")
	private String deviceToken;
	
	@TableField(exist=false)
	private String token;
	
	@TableField(exist=false)
	private String roleName;
	
	@TableField(exist=false)
	private String orgName;
	
	@TableField(exist=false)
	private String companyName;
	
	@TableField(exist=false)
	private String stationName;
	
	@TableField(exist=false)
	private String createName;
	
	@TableField(exist=false)
	private List<String> ids;
	
	@ApiModelProperty("最后登录时间")
	private Date lastLoginTime; 
	
	@TableField(exist=false)
	private String fullName;
}
