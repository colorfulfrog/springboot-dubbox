package com.yxhl.platform.common.exception;

/**
 * 公共的错误码异常类
 */
public class BaseException extends Exception {
	private static final long serialVersionUID = 1L;

	protected String errorCode;
	protected BaseError error;

	public BaseException() {
		super();
	}

	public BaseException(BaseError error) {
		super();
		this.error = error;
	}

	public BaseException(BaseError error, String errorCode) {
		super();
		this.error = error;
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public BaseError getError() {
		return error;
	}

}
