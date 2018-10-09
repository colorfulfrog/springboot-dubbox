package com.yxhl.stationbiz.system.domain.request;



import java.io.Serializable;
import java.util.List;

import com.yxhl.stationbiz.system.domain.response.HolidayPriceResponse;
import com.yxhl.stationbiz.system.domain.response.SeatCateResp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加票价列表请求对象
 * @author Administrator
 *
 */
@Data
public class BusPriceRequest implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "所属机构 长度(32)")
	private java.lang.String orgId;
	
	@ApiModelProperty(value = "所属单位 长度(32)")
	private java.lang.String compId;
	
	@ApiModelProperty(value = "班次模板ID 长度(32)")
	private java.lang.String scheduleTplId;
	
	@ApiModelProperty(value = "票价ID 长度(32)")
	private java.lang.String priceId;
	
	@ApiModelProperty(value = "上车站 长度(32)")
	private java.lang.String onStaId;
	
	@ApiModelProperty(value = "下车站 长度(32)")
	private java.lang.String offStaId;
	
	@ApiModelProperty(value = "座位类型 0普通 1商务 2卧铺 长度(2)")
	private java.lang.String seatCate;
	
	@ApiModelProperty(value = "座位数 ")
	private java.lang.Integer seats;
	
	@ApiModelProperty(value = "座位号 0全部 其他数字表示具体座位号 ")
	private java.lang.Integer seatNo;
	
	@ApiModelProperty(value = "票价取值表 ")
	List<HolidayPriceResponse> tvList;
	
	private String createBy; // 创建者
	
	private String updateBy; // 更新者
	
	@ApiModelProperty(value = "线路id")
	private String lineId;
	
	@ApiModelProperty(value = "班次模板idList ")
	List<String> busIdList;
	
	@ApiModelProperty(value = "座位类型，座位数List ")
	List<SeatCateResp> seatList;
	
	
    
}
