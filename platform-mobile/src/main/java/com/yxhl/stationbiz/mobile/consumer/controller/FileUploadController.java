package com.yxhl.stationbiz.mobile.consumer.controller;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.yxhl.platform.common.CommonResponse;
import com.yxhl.platform.common.constants.CodeUtils;
import com.yxhl.platform.common.entity.AttachmentInfo;
import com.yxhl.platform.common.web.BaseController;
import com.yxhl.stationbiz.mobile.consumer.util.OSSManageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "文件上传")
@RequestMapping(value = "/file")
public class FileUploadController extends BaseController {
	@Autowired
	private OSSManageUtil ossUtil;
	
	@ApiOperation("文件上传")
	//@PostMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	@RequestMapping(value = "/upload", produces = "application/json;charset=UTF-8")
	public CommonResponse list(@RequestParam("file") MultipartFile file) throws IOException, Exception {
		if (file.isEmpty()) {
			CommonResponse.createCustomCommonResponse(CodeUtils.FAIL_CODE, "无法获取文件");
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        //获取文件扩展名
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1); 
		//文件重命名
		String reFileName = UUID.randomUUID().toString()+"."+suffix;
        logger.info("上传的文件名为：" + fileName + ",重命名后：" + reFileName);
        String filePath=ossUtil.uploadFile(file.getInputStream(),
				file.getSize(), reFileName);
        AttachmentInfo atta=new AttachmentInfo();
		atta.setName(fileName);
		atta.setFileSize(file.getSize()); 
		atta.setFileUrl(filePath);
		atta.setContentType(suffix);
		return CommonResponse.createCommonResponse(atta);
	}
}