package com.yxhl.platform.common.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * BaseEntity
 *
 * @author lw
 */
@Data
public abstract class BaseObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    
}

