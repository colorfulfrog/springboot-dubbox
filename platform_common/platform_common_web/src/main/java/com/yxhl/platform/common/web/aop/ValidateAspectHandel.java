package com.yxhl.platform.common.web.aop;



import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.utils.JsonMapper;

/**
 * @class_name ValidateAspectHandel
 * @description 验证注解类
 */
@Component
@Aspect
@Order(1)
public class ValidateAspectHandel {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public LocalValidatorFactoryBean validator;

    /**
     * 使用AOP对使用了ValidateGroup的方法进行代理校验
     *
     * @throws Throwable
     */
    @SuppressWarnings("finally")
    @Around(value = "execution(* com.yxhl.stationbiz..*.*(..))&&@annotation(javax.validation.Valid)")//
    private Object validateAround(ProceedingJoinPoint joinPoint) throws Throwable {

        long date = System.currentTimeMillis();
        Map<String, Object> result = Maps.newConcurrentMap();
        /**
         * 默认校验正确
         */
        result.put("validate", true);
        boolean flag = true;
        /**
         * 所有的请求参数
         */
        Object[] args = null;
        Method method = null;
        Object target = null;
        String methodName = null;
        try {
            /**
             * 获取被切面方法名字
             */
            methodName = joinPoint.getSignature().getName();
            target = joinPoint.getTarget();
            method = getMethodByClassAndName(target.getClass(), methodName);    //得到拦截的方法
            args = joinPoint.getArgs();        //方法的参数
            Class<?>[] groups = new Class<?>[0];
            LocalValidatorFactoryBean validatorFactoryBean = validator;// (LocalValidatorFactoryBean)validator;
            ValidatorImpl validatorImpl = (ValidatorImpl) validatorFactoryBean.getValidator();
            /**
             * 通过javax定义的校验规则，进行注解校验
             */
            Set<ConstraintViolation<Object>> violations = validatorImpl.validateParameters(target, method, args, groups);
            /**
             * 存在校验不合法的情况
             */
            if (!violations.isEmpty()) {
                for (Iterator<ConstraintViolation<Object>> iterator = violations.iterator(); iterator.hasNext(); ) {
                    ConstraintViolation<Object> constraintViolation = iterator.next();
                    String errorMessage = constraintViolation.getMessage();
                    String field = constraintViolation.getPropertyPath().toString().split("\\.")[1];
                    try {

                        String _errorMessage = CodeUtils.toErrorMessage(errorMessage, field);
                        result.put("validate", false);
                        result.put("result", _errorMessage);
                        break;
                    } catch (Exception e) {
                        String _errorMessage = this.exception(e, methodName);
                        result.put("validate", false);
                        result.put("result", _errorMessage);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            String _errorMessage = this.exception(e, methodName);
            result.put("validate", false);
            result.put("result", _errorMessage);
        } finally {
            flag = (boolean) result.get("validate");
            if (flag) {
                //校验正确,执行controller
                Object val = joinPoint.proceed();
                //记录函数执行的日志
                this.log(methodName, date, val);
                return val;
            } else {
                return result.get("result");
            }
        }
    }

    /**
     * @param c
     * @param methodName 根据类和方法名得到方法
     * @return
     * @Title: getMethodByClassAndName
     * @Description:
     */
    public Method getMethodByClassAndName(@SuppressWarnings("rawtypes") Class c, String methodName) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    /**
     * @param methodName 被切面的方法名字
     * @param startTime  方法开始时间
     * @Title: log
     * @Description: 记录日志
     */
    private void log(String methodName, long startTime, Object result) {

        if (logger.isInfoEnabled()) {
            long endTime = System.currentTimeMillis();
            Map<String, Object> loggerMap = Maps.newHashMap();
            loggerMap.put("cost", (endTime - startTime) + "");
            loggerMap.put("result", result);

            Map<String, Object> loggerResult = Maps.newHashMap();
            loggerResult.put(methodName, loggerMap);
            logger.info(JsonMapper.toJsonString(loggerResult));
        }
    }

    /**
     * @param e          传入的异常
     * @param methodName 被切面的方法名称
     * @return
     * @Title: exception
     * @Description: 异常发生时，记录日志，并声称返回的数据
     */
    private String exception(Exception e, String methodName) {
        logger.error(Joiner.on(":").join("method={}", methodName, e));
        Map<String, String> map = Maps.newConcurrentMap();
        map.put("code", CodeUtils.EXCEPTION_CODE);
        map.put("message", CodeUtils.EXCEPTION_MSG + e);
        String _errorMessage = JsonMapper.toJsonString(map);
        return _errorMessage;
    }

    /**
     * @param request
     * @return
     * @throws IOException
     * @Title: getRealRemoteAddr
     * @Description: 获取真实的远程调用IP
     */
    @SuppressWarnings("unused")
    private String getRealRemoteAddr(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        String ip = request.getHeader("x-forwarded-for");
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
