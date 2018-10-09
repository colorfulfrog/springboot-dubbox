package com.yxhl.stationbiz.web.consumer;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yxhl.platform.common.web.config.Settings;
import com.yxhl.platform.common.web.config.WebConfig;

/**
 * 启动类
 */
@RestController
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
//@ImportResource(value= {"classpath:dubbo-consumer.xml","classpath:dubbo-schedule-consumer.xml"})
@ImportResource("classpath:dubbo-*consumer.xml")
@Import({Settings.class, WebConfig.class})
@ComponentScan(basePackages= {"com.yxhl"})
@ServletComponentScan
public class WebConsumerApplication {
    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebConsumerApplication.class);

    /**
     * Hello string.
     *
     * @return the string
     */
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
    
    @PostConstruct
    void started() {
      TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(WebConsumerApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        LOGGER.info("platform-web started!!!");
    }
}
