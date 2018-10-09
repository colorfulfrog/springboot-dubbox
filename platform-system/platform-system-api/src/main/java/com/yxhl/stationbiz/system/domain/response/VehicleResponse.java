package com.yxhl.stationbiz.system.domain.response;

import java.io.Serializable;
import java.util.List;

import com.yxhl.stationbiz.system.domain.entity.basicinfo.Driver;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Line;
import com.yxhl.stationbiz.system.domain.entity.basicinfo.Vehicle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author lw
 */
@Data
public class VehicleResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "车辆信息")
	private Vehicle vehicle;
	
	@ApiModelProperty(value = "线路")
	private List<Line> lines;
	
	@ApiModelProperty(value = "司机")
	private List<Driver> drivers;
}
