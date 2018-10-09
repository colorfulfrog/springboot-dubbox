package com.yxhl.stationbiz.system.provider.util;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
 
public class MailUtil {
 
	//smtp发送
    static int port = 25;
    static String mailHost = "mail.e-lead.cn";
    static String user = "hr@e-lead.cn";
    static String password = "Elead2016";
    //smtps发送
    static int port_smtps = 465;
    static String mailHost_smtps = "smtp.aliyun.com";
    static String user_smtps = "eleadrd@aliyun.com";
    static String password_smtps = "Elead2017";
    
    /**
     * SMTP协议发送
     * @param subject
     * @param body
     * @param tos
     */
    public static void sendEmail(String subject, String body, String [] tos) {
    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SimpleMailMessage mm = new SimpleMailMessage();

	    mailSender.setHost(mailHost);
	    mailSender.setPort(port);
	    mailSender.setUsername(user);
	    mailSender.setPassword(password);
	    mm.setFrom(user);
	    mm.setTo(tos);
	    mm.setSubject(subject);
	    mm.setText(body);
	    Properties prop = new Properties();  
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");  
        mailSender.setJavaMailProperties(prop);  
        mailSender.send(mm);
    }
    
    /**
     * SMTPS协议发送
     * @param subject
     * @param body
     * @param tos
     */
    public static void sendEmail_smtp(String subject, String body, String [] tos) {
    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SimpleMailMessage mm = new SimpleMailMessage();

	    mailSender.setHost(mailHost_smtps);
	    mailSender.setPort(port_smtps);
	    mailSender.setUsername(user_smtps);
	    mailSender.setPassword(password_smtps);
	    mm.setFrom(user_smtps);
	    mm.setTo(tos);
	    mm.setSubject(subject);
	    mm.setText(body);
	    Properties prop = new Properties();  
	    prop.put("mail.transport.protocol", "smtps");
	    prop.put("mail.smtps.host", mailHost_smtps);
	    prop.put("mail.smtps.auth", "true"); 
        mailSender.setJavaMailProperties(prop);  
        mailSender.send(mm);
    }
    public static void main(String[] args) {
    	System.out.println("start send");
		MailUtil.sendEmail_smtp("testmail", "this is a test", new String[]{"dingl@e-lead.cn"});
		System.out.println("end send");
	}
}