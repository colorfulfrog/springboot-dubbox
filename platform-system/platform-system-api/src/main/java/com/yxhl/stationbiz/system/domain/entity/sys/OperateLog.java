package com.yxhl.stationbiz.system.domain.entity.sys;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.yxhl.platform.common.entity.ELItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *	
 *  表名:sys_operate_log
 *  注释:操作日志表
 *  创建人: xjh
 *  创建日期:2018-7-9 17:20:55
 */
@Data
@TableName(value="sys_operate_log")
public class OperateLog extends ELItem{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "操作模块 长度(10) 必填")
	private java.lang.String module;
	
	@ApiModelProperty(value = "操作类型 长度(20) 必填")
	private java.lang.String type;
	
	@ApiModelProperty(value = "操作IP 长度(20) 必填")
	private java.lang.String ip;
	
	@ApiModelProperty(value = "操作内容")
	private String content;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "开始时间")
	private String startTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "结束时间")
	private String endTime;
	
	@TableField(exist = false)
	@ApiModelProperty(value = "操作人 名称")
	private String userName;
}
