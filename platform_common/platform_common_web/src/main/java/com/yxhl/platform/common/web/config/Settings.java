package com.yxhl.platform.common.web.config;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "el.conf")
public class Settings {

	public Map<String, List<String>> staticMappings = new LinkedHashMap<String, List<String>>();

	public Map<String, List<String>> getStaticMappings() {
		return staticMappings;
	}

	public void setStaticMappings(Map<String, List<String>> staticMappings) {
		this.staticMappings = staticMappings;
	}
}
