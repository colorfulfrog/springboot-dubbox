package com.yxhl.platform.common.service.dao;

import com.yxhl.platform.common.entity.BaseObject;
/**
 * dao基类继承BaseMapper
 *
 * @param <T> the type parameter
 * @author liwei
 */
public interface CrudDao<T extends BaseObject> extends com.baomidou.mybatisplus.mapper.BaseMapper<T> {
    // 这里可以放一些公共的方法
}