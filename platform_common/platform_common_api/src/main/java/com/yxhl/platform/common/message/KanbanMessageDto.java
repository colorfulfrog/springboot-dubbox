package com.yxhl.platform.common.message;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息实体传输对象
 * @author liutao
 *
 */
public class KanbanMessageDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String cardId;

	private String ownerCode;

	private String cardType;

	private String optType;

	private String optField;

	private String content;

	//项目id
	private String projectId;

	//1：个人；2：项目
	private String ownerType;

	//责任人id
	private String createBy;

	private String toWhere;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public String getToWhere() {
		return toWhere;
	}

	public void setToWhere(String toWhere) {
		this.toWhere = toWhere;
	}


	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
