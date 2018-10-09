package com.yxhl.stationbiz.system.domain.exception;

/**
 * 业务异常.
 *
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

}