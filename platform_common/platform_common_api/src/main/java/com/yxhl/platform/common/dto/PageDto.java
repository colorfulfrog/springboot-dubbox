package com.yxhl.platform.common.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据PageDto类
 */
@Data
public class PageDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("页数，默认1")
    private int pageIndex = 1;
    
    @ApiModelProperty("每页条数，默认10")
    private int pageSize = 10;
    
    @ApiModelProperty("排序字段，默认create_time")
    private String orderBy = "create_time";
    
    @ApiModelProperty("排序顺序,倒序")
    private String sortBy = "DESC";
}
