package com.yxhl.stationbiz.system.domain.dto;

import java.util.Date;

import com.yxhl.platform.common.entity.ELObject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ELProjectDto extends ELObject {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("父项目id")
	private String parentId;

	@ApiModelProperty("项目编号")
	private String code;

	@ApiModelProperty("项目名称")
	private String name;

	@ApiModelProperty("启动时间")
	private Date startTime;

	@ApiModelProperty("完成时间")
	private Date finishTime;
}
