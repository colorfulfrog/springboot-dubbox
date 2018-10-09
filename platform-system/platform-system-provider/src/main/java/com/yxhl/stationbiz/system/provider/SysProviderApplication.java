package com.yxhl.stationbiz.system.provider;

import java.io.IOException;
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
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring-boot 启动入口
 */
@RestController
@ServletComponentScan
@ComponentScan(basePackages= {"com.yxhl"})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class } )
@EnableTransactionManagement //启用事务
@ImportResource("classpath:dubbo-*provider.xml")
@EnableScheduling
public class SysProviderApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysProviderApplication.class);

    @RequestMapping
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
     * @throws IOException the io exception
     */
    public static void main(String[] args) throws IOException {
        SpringApplication application = new SpringApplication(SysProviderApplication.class);
        application.setRegisterShutdownHook(false);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        LOGGER.info("platform-system-provider started!!!");
    }
}
