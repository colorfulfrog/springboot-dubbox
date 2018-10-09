package com.yxhl.platform.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员Entity类
 */
@Data
public class ELUser extends ELItem {

    private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户名称")
	private String userName;
	
	@ApiModelProperty("昵称")
	private String nickName;
	
	@ApiModelProperty("头像")
	private String avatar;
	
	@ApiModelProperty("所属机构")
	private String orgId;
	
	@ApiModelProperty("所属单位")
	private String companyId;
	
	@ApiModelProperty("所属站点")
	private String stationId;
	
}
