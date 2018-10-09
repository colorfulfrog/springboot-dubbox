package com.yxhl.stationbiz.system.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELTreeItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName(value="proj_elproject")
public class ELProject  extends ELTreeItem<ELProject>{

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("项目名称")
	private String name;
	
	@ApiModelProperty("启动时间")
	private Date startTime;

	@ApiModelProperty("完成时间")
	private Date finishTime;
}
