package com.yxhl.platform.common.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据ELObjectDto类
 */
@Data
public class ELObjectDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("对象Id")
    private String id;

    @ApiModelProperty("创建者")
    private String createBy; // 创建者
    
    @ApiModelProperty("创建时间")
    private Date createTime; // 创建时间
    
    @ApiModelProperty("更新者")
    private String updateBy; // 更新者
    
    @ApiModelProperty("更新")
    private Date updateTime; // 更新
    
    @ApiModelProperty("删除标记（0：正常；1：删除；2：审核）")
    private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
}
