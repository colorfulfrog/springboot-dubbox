package com.yxhl.stationbiz.system.domain.exception;

import com.google.common.base.Joiner;
import com.yxhl.platform.common.exception.BaseException;

/**
* @Description: 自定义Exception
*/
public class ConcurrentException extends BaseException{
	private static final long serialVersionUID = -1644359954949978011L;
	
	private String tableName;
	private String db;
	private String operate;
	
	public ConcurrentException(String tableName, String db, String operate,String errorCode) {
		super();
		this.tableName = tableName;
		this.db = db;
		this.operate = operate;
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return Joiner.on(":").join(db,tableName,operate,"current exception");
	}
	
}	
