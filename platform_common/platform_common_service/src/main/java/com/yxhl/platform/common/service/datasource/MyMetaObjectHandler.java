package com.yxhl.platform.common.service.datasource;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import java.util.Date;

/**
 * @class_name MyMetaObjectHandler
 * @description 自动填充字段
 */
public class MyMetaObjectHandler extends MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //createTime
        if (hasFieldName(metaObject, "createTime")) {
            Object createTime = getFieldValByName("createTime", metaObject);
            if (createTime == null) {
                setFieldValByName("createTime", new Date(), metaObject);
            }
        }

        //updateTime
        if (hasFieldName(metaObject, "updateTime")) {
            Object updateTime = getFieldValByName("updateTime", metaObject);
            if (updateTime == null) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }
        }
        //delFlag
        if (hasFieldName(metaObject, "delFlag")) {
            Object delFlag = getFieldValByName("delFlag", metaObject);
            if (delFlag == null) {
                setFieldValByName("delFlag", "0", metaObject);
            }
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //updateTime
        if (hasFieldName(metaObject, "updateTime")) {
            /*Object updateTime = getFieldValByName("updateTime", metaObject);
            if (updateTime == null) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }*/
            setFieldValByName("updateTime", new Date(), metaObject);
        }
    }

    private boolean hasFieldName(MetaObject metaObject, String fieldName) {
        if (metaObject.hasGetter(fieldName) || metaObject.hasGetter("et." + fieldName)) {
            return true;
        }
        return false;
    }
}
