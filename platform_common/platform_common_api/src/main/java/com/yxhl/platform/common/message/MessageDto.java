package com.yxhl.platform.common.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息实体传输对象
 * @author liutao
 *
 */
public class MessageDto implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	private String id;
	/**
	 * 项目
	 */
	//private String projectName;
	/**
	 * 项目ID
	 */
	private String projectId;
	/**
	 * 目标名字
	 */
	private String targetName;
	/**
	 * 目标ID
	 */
	private String targetId;
	/**
	 * 动作目标类型：任务，需求，风险等
	 */
	private String targetType;
	/**
	 * 创建人
	 */
	private Date createTime;
	/**
	 * 动作
	 */
	private String verb;//动作

	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 用户名字
	 */
	private String uname;
	/**
	 * 用户头像地址
	 */
	private String avatar;
	/**
	 * 用户实际操作信息
	 */
	private Map<String, MessageEntity> data;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/*public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}*/
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getTargetType() {
		return targetType;
	}
	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getVerb() {
		return verb;
	}
	public void setVerb(String verb) {
		this.verb = verb;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Map<String, MessageEntity> getData() {
		return data;
	}
	public void setData(Map<String, MessageEntity> data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
