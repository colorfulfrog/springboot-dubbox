package com.yxhl.platform.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.dto.ELUserDto;

import lombok.Data;

/**
 * rest 接口返回对象
*/
@Data
public class CommonResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean success;
    private String code;
    private String message;

    private Map<String, Object> res;

    /**
     * 默认构造方法
     */
    public CommonResponse() {
        this.success = true;
    }

    /**
     * 默认失败构造方法
     */
    public CommonResponse(boolean status) {
        this.success = status;
    }

    /**
     * 设置为成功应答
     */
    public void success() {
        this.code = CodeUtils.SUCCESS_CODE;
        this.message = CodeUtils.SUCCESS_MSG;
    }

    /**
     * 向通用应答内设置一项数据对象
     *
     * @param data
     */
    public void setData(Object data) {
        if (res == null) {
            res = new HashMap<>();
        }
        res.put("data", data);
    }

    /**
     * 向通用应答内设置一个集合对象
     *
     * @param collection
     */
    public void setData(Collection collection) {
        if (res == null) {
            res = new HashMap<>();
        }
        res.put("data", collection);
    }

    public void setUsers(Collection<ELUserDto> collection) {
        if (res == null) {
            res = new HashMap<>();
        }
        res.put("users", collection);
    }

    /**
     * 快速创建一个成功应答对象
     *
     * @return
     */
    public static CommonResponse createCommonResponse() {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        return commonResponse;
    }

    /**
     * 快速创建一个应答对象, 可传入一个数据对象, 并置为success
     *
     * @param data
     * @return
     */
    public static CommonResponse createCommonResponse(Object data) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        if (commonResponse.res == null) {
            commonResponse.res = new HashMap<>();
        }
        commonResponse.res.put("data", data);
        return commonResponse;
    }

    /**
    * @param
    * @return
    * @description 扩展res
    * @version v1.0
    */
    public void extendsRes(String key,Object value) {
        if (res == null) {
            res = new HashMap<>();
        }
        res.put(key, value);
    }

    /**
     * @param id
     * @return
     * @Title: createCommonResponseForId
     * @Description: 快速创建一个应答对象, 并将ID返回回去
     */
    public static CommonResponse createCommonResponseForId(String id) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        commonResponse.setCode(CodeUtils.SUCCESS_CODE);
        commonResponse.setData(id);
        return commonResponse;
    }

    /**
     * @param timestamp
     * @return
     * @Title: updateCommonResponseForTimestamp
     * @Description: 批量修改一个应答对象, 并将修改时间返回回去
     */
    public static CommonResponse updateCommonResponseForTimestamp(String timestamp) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.success();
        if (commonResponse.res == null) {
            commonResponse.res = new HashMap<>();
        }
        commonResponse.res.put("data", timestamp);
        return commonResponse;
    }

    /**
     * @return
     * @Title: createExceptionCommonResponse
     * @Description: 异常信息
     */
    public static CommonResponse createExceptionCommonResponse(Exception e) {
        CommonResponse commonResponse = new CommonResponse(false);
        commonResponse.setCode(CodeUtils.EXCEPTION_CODE);
        commonResponse.setMessage(CodeUtils.EXCEPTION_MSG + e);
        return commonResponse;
    }

    /**
     * @param message
     * @return
     * @Title: createWrongParamCommonResponse
     * @Description: 数据不合法
     */
    public static CommonResponse createWrongParamCommonResponse(String message) {
        CommonResponse commonResponse = new CommonResponse(false);
        commonResponse.setCode(CodeUtils.DATA_WRONGFULNESS_CODE);
        commonResponse.setMessage(CodeUtils.DATA_WRONGFULNESS_MSG + "-" + message);
        return commonResponse;
    }

    /**
     * @return
     * @Title: createEmptyListCommonResponse
     * @Description: 数据集合为空
     */
    public static CommonResponse createEmptyListCommonResponse() {
        CommonResponse commonResponse = new CommonResponse(true);
        commonResponse.setCode(CodeUtils.SUCCESS_CODE);
        commonResponse.setMessage(CodeUtils.EMPTY_LIST_MSG);
        commonResponse.setRes(Maps.newHashMap());
        return commonResponse;
    }

    /**
     * @return
     * @Title: createEmptyDataCommonResponse
     * @Description: 数据为空
     */
    public static CommonResponse createEmptyDataCommonResponse() {
        CommonResponse commonResponse = new CommonResponse(true);
        commonResponse.setCode(CodeUtils.SUCCESS_CODE);
        commonResponse.setMessage(CodeUtils.EMPTY_DAT_MSG);
        commonResponse.setRes(Maps.newHashMap());
        return commonResponse;
    }

    /**
     * @return
     * @Title: createNotExistCommonResponse
     * @Description: 数据为null
     */
    public static CommonResponse createNotExistCommonResponse() {
        CommonResponse commonResponse = new CommonResponse(false);

        commonResponse.setCode(CodeUtils.DATA_NOT_EXIST_CODE);
        commonResponse.setMessage(CodeUtils.DATA_NOT_EXIST_MSG);
        return commonResponse;
    }

    /**
     * @return
     * @Title: createNotExistCommonResponse
     * @Description: 数据为null
     */
    public static CommonResponse createNotExistCommonResponse(String column) {
        CommonResponse commonResponse = new CommonResponse(false);
        commonResponse.setCode(CodeUtils.DATA_NOT_EXIST_CODE);
        commonResponse.setMessage(column + " " + CodeUtils.DATA_NOT_EXIST_MSG);
        return commonResponse;
    }

    /**
     * @param @return
     * @return CommonResponse    返回类型
     * @throws
     * @Title: createExistCommonResponse
     * @Description: 数据重复
     */
    public static CommonResponse createExistCommonResponse() {
        CommonResponse commonResponse = new CommonResponse(false);
        commonResponse.setCode(CodeUtils.DATA_DUPLICATION_CODE);
        commonResponse.setMessage(CodeUtils.DATA_DUPLICATION_MSG);
        return commonResponse;
    }

    /**
     * @param code
     * @param msg
     * @return
     * @Title: createCustomCommonResponse
     * @Description: 自定义返回对象   code、message 均为自定义
     */
    public static CommonResponse createCustomCommonResponse(String code, String msg) {
        CommonResponse commonResponse = new CommonResponse(true);
        commonResponse.setCode(code);
        commonResponse.setMessage(msg);
        return commonResponse;
    }

    /**
    * @param
    * @return
    * @description 系统异常
    * @version v1.0
    */
    public static CommonResponse createExceptionsCommonResponse() {
        CommonResponse commonResponse = new CommonResponse(false);
        commonResponse.setCode(CodeUtils.EXCEPTION_CODE);
        commonResponse.setMessage(CodeUtils.EXCEPTION_MSG);
        return commonResponse;
    }
}