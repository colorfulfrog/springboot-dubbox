package com.yxhl.platform.common.message;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class MailDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String subject; // 邮件主题
	private String from; // 发件人
	private String[] to; // 收件人
	private String[] cc; // 抄送
	private String textContent; // 邮件内容
	private String type = "Text"; // 邮件类型：Text(简单文本)、Html(简单html)、Html_Inline(嵌入静态资源)、Attachment(带附件)
	private File[] images; // 附加图片内容
	private File[] attachments; // 附件
	
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
	
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public File[] getImages() {
		return images;
	}
	public void setImages(File[] images) {
		this.images = images;
	}
	public File[] getAttachments() {
		return attachments;
	}
	public void setAttachments(File[] attachments) {
		this.attachments = attachments;
	}
	public boolean isMimeMail(){
		return ((null!=images&&images.length>0)&&(null!=attachments||attachments.length>0));
	}
	@Override
	public String toString() {
		return "MailDto [subject=" + subject + ", from=" + from + ", to=" + Arrays.toString(to) + ", cc="
				+ Arrays.toString(cc) + ", textContent=" + textContent + ", type=" + type + ", images="
				+ Arrays.toString(images) + ", attachments=" + Arrays.toString(attachments) + "]";
	}
}
