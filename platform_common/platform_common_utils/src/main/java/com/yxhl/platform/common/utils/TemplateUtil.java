package com.yxhl.platform.common.utils;

import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class TemplateUtil {
	private static Properties props = new Properties();
	static {
		props.setProperty(Velocity.INPUT_ENCODING, "UTF-8");  
        props.setProperty(Velocity.RESOURCE_LOADER, "classpath");  
        props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	}
	
	/**
	 * 替换字符串模板(Velocity模板)参数
	 * @param templateStr 字符串模板
	 * @param model 参数
	 */
	public static String getTemplateByStr(String templateStr,Map<String,Object> model) {
        VelocityEngine engine = new VelocityEngine(props);  
        VelocityContext context = new VelocityContext();
        for (Entry<String, Object> entry : model.entrySet()) {
        	context.put(entry.getKey(), entry.getValue()); 
		}
        StringWriter writer = new StringWriter();  
        engine.evaluate(context, writer, "", templateStr);  
        return writer.toString();
    }
	
	/**
	 * 替换字符串模板(Velocity模板)参数
	 * @param templatePath classpath路径下的Velocity模板名称
	 * @param model 参数
	 */
	public static String getTemplate(String templatePath,Map<String,Object> model) {
        VelocityEngine engine = new VelocityEngine(props);  
        VelocityContext context = new VelocityContext();
        for (Entry<String, Object> entry : model.entrySet()) {
        	context.put(entry.getKey(), entry.getValue()); 
		}
        Template template = engine.getTemplate(templatePath,"UTF-8");
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }
	
	/*public static void main(String[] args) {
		// 字符串模版  
        String template = "${owner}：您的${type} : ${bill} 在  ${date} 日已支付成功";  
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("owner", "nassir");  
        model.put("bill", "201203221000029763");  
        model.put("type", "订单");  
        model.put("date",  
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
		String templateStr = TemplateUtil.getTemplateByStr(template, model);
		System.out.println(templateStr);
		
		String templatePath = "mail.vm";
		Map<String,Object> model2 = new HashMap<String,Object>();
		model2.put("username", "Velocity2");
		model2.put("activation_url", "http://www.baidu.com");
        String template2 = TemplateUtil.getTemplate(templatePath, model2);
        System.out.println(template2);
	}*/
}
