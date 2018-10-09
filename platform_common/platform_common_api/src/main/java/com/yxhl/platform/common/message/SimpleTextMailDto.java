package com.yxhl.platform.common.message;

import com.alibaba.fastjson.JSONObject;

public class SimpleTextMailDto extends BaseMailDto{
	private static final long serialVersionUID = 1L;
	private String textContent; // 普通文本邮件内容
	
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
