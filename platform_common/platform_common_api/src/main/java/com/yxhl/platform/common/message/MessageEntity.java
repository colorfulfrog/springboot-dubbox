package com.yxhl.platform.common.message;

import java.io.Serializable;

/**
 * 消息实体信息
 * @author liutao
 *
 */
public class MessageEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String eid;
	private String etype;
	private String name;
	
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
