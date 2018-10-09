package com.yxhl.platform.common.exception;

/**
 * 系统业务异常.
 *  
 */
public class YxBizException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String code;

	public YxBizException() {
        super();
    }

    public YxBizException(String message) {
        super(message);
    }
    
    public YxBizException(String code,String message) {
    	super(message);
    	this.code = code;
    }

    public YxBizException(Throwable cause) {
        super(cause);
    }

    public YxBizException(String message, Throwable cause) {
        super(message, cause);
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
    
}