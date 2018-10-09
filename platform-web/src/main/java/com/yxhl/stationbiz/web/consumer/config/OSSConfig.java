package com.yxhl.stationbiz.web.consumer.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * OSS 参数配置类
 * @author lw
 *
 */
@Component
@ConfigurationProperties(prefix="aliyun.oss")
@Data
public class OSSConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	private String endpoint;
	private String osswebsite;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;
	private String testBucket;
}
