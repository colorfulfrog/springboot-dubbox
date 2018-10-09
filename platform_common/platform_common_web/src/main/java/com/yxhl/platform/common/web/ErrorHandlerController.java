package com.yxhl.platform.common.web;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yxhl.platform.common.CommonResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @class_name ErrorHandlerController
 * @description 异常处理
 */
@RestController
public class ErrorHandlerController implements ErrorController {

    private final static String ERROR_PATH = "/error";


    /**
     * Returns the path of the error page.
     *
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    /**
     * Supports the HTML Error View
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorHtml(HttpServletRequest request) {
        return "404";
    }

    /**
     * Supports other formats like JSON, XML
     *
     * @param request
     * @return
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object error(HttpServletRequest request) {
        return CommonResponse.createExceptionsCommonResponse();
    }
}
