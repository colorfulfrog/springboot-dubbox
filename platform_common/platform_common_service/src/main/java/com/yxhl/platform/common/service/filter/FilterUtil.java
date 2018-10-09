package com.yxhl.platform.common.service.filter;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yxhl.platform.common.utils.StringHelper;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @class_name FilterUtil
 * @description 过滤条件查询
 */
public class FilterUtil {

    /**
    * @param
    * @return
    * @description 匹配类型
    */
    public enum MatchType {
        IN, EQ, LIKE, LT, GT, ISNULL, LE, GE;
    }

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 解析过滤条件列表字符串 字段名称_值类型_值_比较方式_联合方式_关联实体<br>
     * 值类型分为：
     *
     * <pre>
     * S(String.class), I(Integer.class), L(Long.class), B(Boolean.class), C(Collection.class);
     * </pre>
     *
     * 比较方式分为：
     *
     * <pre>
     * IN, EQ, LIKE, LT, GT, LE, GE;
     * </pre>
     *
     * 联合方式分为：
     *
     * <pre>
     * AND,OR;
     * 若是AND可以留空
     * </pre>
     *
     * 联合方式分为： 若关联实体不是集合而是自身属性，则可以留空
     *
     * <br>
     * 例如：name_S_ch_LIKE__<br>
     * 注意：若参数类型为Boolean时，True必须传入True或true，False可以不设置。例：isSet_B__EQ or
     * isSet_B_True_EQ
     *
     * @param filterStr
     *            name_S_ch_LIKE__
     * @return 若传入参数为空则返回null
     */
    public static Wrapper parsePropertyFilterExp(String filterStr, String orderByStr) throws Exception {

        if (StringUtils.isEmpty(filterStr)) {
            return Condition.create();
        }

        Wrapper wrapper = Condition.create();
        if (filterStr.length()-1 > filterStr.lastIndexOf(",")) {
            filterStr = filterStr + ",";
        }
        String expReg = "([^\\_]*)_([^\\_]*)_([^\\_]*)_(.*?)_(.*?),";

        if(!filterStr.matches(expReg)) {
            throw new Exception("错误的格式，请调整如下例：id_L_3_EQ_OR");
        }
        List<String[]> exps = StringHelper.matchAll(filterStr, expReg);
        for (String[] singleExp : exps) {
            String property =StringHelper.camelToUnderline(singleExp[1]); //实体属性转换为数据库属性
            String typeName = singleExp[2].toUpperCase();
            String value = singleExp[3];
            if (typeName.equals("B")) {
                if (StringUtils.isEmpty(value)) {
                    value = "false";
                }
            }
            String mt = singleExp[4].toUpperCase();
            String andOr = singleExp[5];

            if (null != andOr && "OR".equals(andOr.toUpperCase().trim())) {
                wrapper = wrapper.or();
            }

            Collection<? extends Serializable> values = Collections.emptyList();
            if (mt.equals("IN")) {
                values = com.google.common.base.Splitter.on(",").splitToList(value);
            }

            if (mt.equals("EQ")) {
                wrapper.eq(property, value);
            } else if (mt.equals("GE")) {
                wrapper.ge(property, value);
            } else if (mt.equals("GT")) {
                wrapper.gt(property, value);
            } else if (mt.equals("IN")) {
                wrapper.in(property, values);
            } else if (mt.equals("LE")) {
                wrapper.le(property, value);
            } else if (mt.equals("LT")) {
                wrapper.lt(property, value);
            } else if (mt.equals("LIKE")) {
                wrapper.like(property, value);
            } else if (mt.equals("ISNULL")) {
                wrapper.isNotNull(property);
            }
        }

        if (null == orderByStr || "".equals(orderByStr.trim())) {
            return wrapper;
        }

        if (orderByStr.length()-1 > orderByStr.lastIndexOf(",")) {
            orderByStr = orderByStr + ",";
        }
        expReg = "([^\\_]*)_([^\\_]*),";
//        if(!orderByStr.matches(expReg)) {
//            throw new Exception("排序，错误的格式，请调整如下例：name_desc,id_asc");
//        }
//        AssertUtils.isTrue(orderByStr.matches(expReg), "排序，错误的格式，请调整如下例：name_desc,id_asc");

        exps = StringHelper.matchAll(orderByStr, expReg);
        for (String[] singleExp : exps) {
            String property = singleExp[1];
            String sortOrder = singleExp[2];
            if ("".equals(sortOrder.trim()) || "ASC".equals(sortOrder.toUpperCase().trim())) {
                wrapper.orderBy(property, true);
            } else {
                wrapper.orderBy(property, false);
            }
        }
        return wrapper;
    }
}
