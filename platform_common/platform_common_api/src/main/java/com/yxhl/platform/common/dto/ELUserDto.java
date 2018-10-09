package com.yxhl.platform.common.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @class_name ELSimpleUser
 * @description 用户简单对象
 */
@Data
public class ELUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String orgId;

    private String orgName;

    // 用户编码（工号）
    private String code;

    // 用户名(登陆名)
    private String name;

    // 真实名称(显示名)
    private String displayName;

    // 手机号码
    private String mobile;

    // 电子邮箱
    private String email;

    // 头像
    private String avatar;

    // 用户类型
    private String type;

    // 用户状态
    private String status;
}
