package com.yxhl.platform.common.message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 邮件基础Dto
 * @author liwei
 *
 */
public class BaseMailDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String subject; // 邮件主题
	private String from; // 发件人
	private String[] to; // 收件人
	private String[] cc; // 抄送
	private String type = "Text"; // 邮件类型：Text(简单文本)、Html(简单html)、Html_Inline(嵌入静态资源)、Attachment(带附件)
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String[] getTo() {
		return to;
	}
	public void setTo(String[] to) {
		this.to = to;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "BaseMailDto [subject=" + subject + ", from=" + from + ", to=" + Arrays.toString(to) + ", cc="
				+ Arrays.toString(cc) + ", type=" + type + "]";
	}
}
