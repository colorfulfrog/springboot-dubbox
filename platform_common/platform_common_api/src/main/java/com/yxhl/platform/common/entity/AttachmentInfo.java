package com.yxhl.platform.common.entity;

import java.io.Serializable;

import lombok.Data;


@Data
public class AttachmentInfo implements Serializable {
	
	private static final long serialVersionUID = 2841713888137800731L;
	
	private String name;	        //附件名称
	private String realName;        //真实名称
	private String contentType;     //文件类型
	private String fileUrl;		    //文件路径
	private long   fileSize;		//文件大小
	private int    imageWidth;      //图片宽度  jpg,png,gif等图片类型才保存宽高 
	private int    imageHeight;     //图片高度
}
