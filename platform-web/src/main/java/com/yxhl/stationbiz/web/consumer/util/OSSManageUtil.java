package com.yxhl.stationbiz.web.consumer.util;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSErrorCode;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.ServiceException;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.yxhl.stationbiz.web.consumer.config.OSSConfig;

/**
 * 对OSS服务器进行上传删除等的处理
 * @ClassName: OSSManageUtil 
 *
 */
@Component
public class OSSManageUtil {
	@Autowired
	private OSSConfig ossConfig;
	
	/***
	 * 阿里云图片上传
	 * @param file 文件流
	 * @param contentLength 文件大小
	 * @param fileName 文件上传后名称
	 * @return
	 * @throws Exception
	 */
	public String uploadFile(InputStream file,long contentLength,String fileName) throws Exception{
		OSSClient ossClient=new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
		String remoteFilePath = ossConfig.getTestBucket().replaceAll("\\\\","/")+"/";
		//创建上传Object的Metadata
		ObjectMetadata objectMetadata=new ObjectMetadata();
		objectMetadata.setContentLength(contentLength);
		objectMetadata.setCacheControl("no-cache");
		objectMetadata.setHeader("Pragma", "no-cache");
		if(StringUtils.isNotEmpty(fileName) && fileName.lastIndexOf(".") >= 0){
			objectMetadata.setContentType(contentType(fileName.substring(fileName.lastIndexOf(".")+1))); 
		}else{
			objectMetadata.setContentType(contentType("jpeg"));
		}
		objectMetadata.setContentDisposition("inline;filename=" + fileName);
		//上传文件
		ossClient.putObject(ossConfig.getBucketName(), remoteFilePath + fileName, file, objectMetadata);
		return ossConfig.getOsswebsite()+ossConfig.getTestBucket()+"/"+ fileName;
	}

	public String imageFileUpload(byte[] fileByte, String user_avatar) throws Exception {  
		String fileType = ".jpg";  
		String[] types = new String[] { ".bmp", ".png", ".gif", ".jpeg", ".pjpeg", ".jpg" };  
		for (int i = 0; i < types.length; i++) {  
			if (types[i].equalsIgnoreCase(user_avatar.substring(user_avatar.lastIndexOf(".")))) {  
				if (types[i].endsWith(".gif"))  
					fileType = ".gif";  
				if (types[i].endsWith(".png"))  
					fileType = ".png";  
			}  
		}  
		String fileName = (System.currentTimeMillis() + (new Random(999999).nextLong())) + fileType;  
		try {  
			InputStream input = new ByteArrayInputStream(fileByte);  
			String bucketName = ossConfig.getBucketName();  
			// 使用默认的OSS服务器地址创建OSSClient对象。  
			OSSClient client = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
			ensureBucket(client, bucketName);  
			ObjectMetadata objectMeta = new ObjectMetadata();  
			objectMeta.setContentLength(fileByte.length);  
			client.putObject(bucketName, fileName, input, objectMeta);  
			String saveUrl = ossConfig.getEndpoint()+"/"+ossConfig.getBucketName()+"/"+ fileName;  
			return saveUrl;  
		} catch (Exception e) {  
			e.printStackTrace();  
			return null;  
		}  
	}  

	public void ensureBucket(OSSClient client, String bucketName)throws OSSException, ClientException {  
		try {  
			// 创建bucket  
			client.createBucket(bucketName);  
			client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);  
		} catch (ServiceException e) {  
			if (!OSSErrorCode.BUCKET_ALREADY_EXISTS.equals(e.getErrorCode())) {  
				// 如果Bucket已经存在，则忽略  
				throw e;  
			}  
		}  
	}  

	/**
	 * 根据key删除OSS服务器上的文件
	 * @Title: deleteFile 
	 * @Description: 
	 * @param @param ossConfigure
	 * @param @param filePath    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void deleteFile(String endpoint,String accessKeyId,String accessKeySecret,String bucketName,String filePath){
		OSSClient ossClient = new OSSClient(endpoint,accessKeyId, accessKeySecret);
		ossClient.deleteObject(bucketName, filePath);

	}

	/**
	 * Description: 判断OSS服务文件上传时文件的contentType
	 * @Version1.0
	 * @param FilenameExtension 文件后缀
	 * @return String 
	 */
	public String contentType(String FilenameExtension){
		FilenameExtension = FilenameExtension.toUpperCase();
		if(FilenameExtension.equals("BMP")){return "image/bmp";}
		if(FilenameExtension.equals("GIF")){return "image/gif";}
		if(FilenameExtension.equals("JPEG")||
				FilenameExtension.equals("JPG")||	
				FilenameExtension.equals("PNG")){return "image/jpeg";}
		if(FilenameExtension.equals("HTML")){return "text/html";}
		if(FilenameExtension.equals("TXT")){return "text/plain";}
		if(FilenameExtension.equals("VSD")){return "application/vnd.visio";}
		if(FilenameExtension.equals("PPTX")||
				FilenameExtension.equals("PPT")){return "application/vnd.ms-powerpoint";}
		if(FilenameExtension.equals("DOCX")||
				FilenameExtension.equals("DOC")){return "application/msword";}
		if(FilenameExtension.equals("XML")){return "text/xml";}
		if(FilenameExtension.equals("ZIP")){return "application/zip";}
		if(FilenameExtension.equals("APK")){return "application/vnd.android.package-archive";}
		if(FilenameExtension.equals("IPA")){return "application/vnd.iphone";}
		return "text/html";
	}

	//创建Ducket
	/*public static void createDucket(){
		OSSClient ossClient = new OSSClient("oss-cn-shenzhen.aliyuncs.com", OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
		ossClient.createBucket("tsyly");
		ossClient.setBucketAcl("tsyly", CannedAccessControlList.PublicReadWrite);
	}*/
	
	public static void main(String[] args) {
		OSSClient ossClient = new OSSClient("http://oss-cn-beijing.aliyuncs.com", "LTAIVrykmcNc7Gxv", "fQkUARpDbq5DNVcxjKmY68KTsiba6R");
		//ossClient.createBucket("yxstation");
		ossClient.setBucketAcl("yxstation", CannedAccessControlList.PublicReadWrite);
	}
}

