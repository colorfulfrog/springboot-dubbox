package com.yxhl.stationbiz.system.domain.response;


import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yxhl.platform.common.entity.ELItem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 座位类型,座位数
 * @author Administrator
 *
 */
@Data
public class SeatCateResp implements Serializable{
    private static final long serialVersionUID = 1L;
    
	@ApiModelProperty(value = "座位类型 0普通座，1商务座，2卧铺")
	private java.lang.String seatCate;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
}
