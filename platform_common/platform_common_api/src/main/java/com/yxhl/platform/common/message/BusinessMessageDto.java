package com.yxhl.platform.common.message;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息实体传输对象
 * @author liutao
 *
 */
public class BusinessMessageDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String ownerCode;

	private String ownerType;

	private String cardType;

	private String optType;

	private String optField;

	private String cardId;

	private String content;

	private String code;

	public String getToWhere() {
		return toWhere;
	}

	public void setToWhere(String toWhere) {
		this.toWhere = toWhere;
	}

	private String toWhere;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getOptField() {
		return optField;
	}

	public void setOptField(String optField) {
		this.optField = optField;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
