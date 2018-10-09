package com.yxhl.platform.common.web.config;

import java.util.List;
import java.util.Map;

import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.yxhl.platform.common.web.aop.LoginVerifyAspectHandler;

import ch.qos.logback.access.servlet.TeeFilter;

/**
 * WEB配置类
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Settings settings;


    /**
     * Remote ip filter remote ip filter.
     *
     * @return the remote ip filter
     */
    @Bean
    public RemoteIpFilter remoteIpFilter() {
        return new RemoteIpFilter();
    }

    /**
     * Tee filter tee filter.
     *
     * @return the tee filter
     */
    @Bean
    @ConditionalOnProperty(prefix = "server.tomcat.accesslog", name = "debug", havingValue = "true")
    public TeeFilter teeFilter() {
        //复制请求响应流，用于打印调试日志
        return new TeeFilter();
    }

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Object mapper object mapper.
     *
     * @return the object mapper
     */
//    @Bean
//    public ObjectMapper objectMapper() {
//        // JsonMapper jsonMapper = new JsonMapper();
//        // return new JsonMapper();
//        return new ObjectMapper();
////        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
////        ObjectMapper mapper = new ObjectMapper();
////        mapper.setDateFormat(dateFormat);
////        logger.info("&&&&&&&&&&&&&&&&-----objectMapper");
////        return mapper;
//    }

    /**
     * Http message converter http message converter.
     *
     * @return the http message converter
     */
//    @Bean
//    public HttpMessageConverter<Object> httpMessageConverter() {
//        logger.info("&&&&&&&&&&&&&&&&-----httpMessageConverter");
////        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
////        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
////        builder.indentOutput(true).dateFormat(dateFormat);
////
////        MappingJackson2HttpMessageConverter httpMessageConverter
////                = new MappingJackson2HttpMessageConverter(builder.build());
////        httpMessageConverter.setObjectMapper(this.objectMapper());
////
////        return httpMessageConverter;
//        return new MappingJackson2HttpMessageConverter(this.objectMapper());
//    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(httpMessageConverter());
////        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
////        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
////        builder.indentOutput(true).dateFormat(dateFormat);
////        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
////        logger.info("&&&&&&&&&&&&&&&&-----configureMessageConverters");
////        super.configureMessageConverters(converters);
//    }
//
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer.setUseSuffixPatternMatch(false) // 系统对外暴露的 URL 不会识别和匹配 .* 后缀
//            .setUseTrailingSlashMatch(true); // 系统不区分 URL 的最后一个字符是否是斜杠 /
//    }
//
//    @Override
//    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
//        // 等价于<mvc:default-servlet-handler />, 对静态资源文件的访问, 将无法 mapping 到 Controller 的 path 交给 default servlet handler 处理
//        configurer.enable();
//    }
//
//    /**
//     * Validator local validator factory bean.
//     *
//     * @return the local validator factory bean
//     */
//    @Bean
//    public LocalValidatorFactoryBean validator() {
//        return new LocalValidatorFactoryBean();
//    }
//
//    /**
//     * Gets method validation post processor.
//     *
//     * @return the method validation post processor
//     */
//    @Bean
//    public MethodValidationPostProcessor getMethodValidationPostProcessor() {
//        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
//        processor.setValidator(validator());
//        return processor;
//    }

    /**
     * Container customizer embedded servlet container customizer.
     *
     * @return the embedded servlet container customizer
     */
    @Bean
    @ConditionalOnProperty(prefix = "server.tomcat.accesslog", name = "debug", havingValue = "true")
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new ContainerAccessLogCustomizer("logback-access.xml");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Map<String, List<String>> mappings = this.settings.getStaticMappings();
        if (mappings != null) {
            for (Map.Entry<String, List<String>> e : mappings.entrySet()) {
                List<String> list = e.getValue();
                if (list != null && !list.isEmpty()) {
                    String[] temp = new String[list.size()];
                    list.toArray(temp);
                    registry.addResourceHandler(e.getKey()).addResourceLocations(temp);
                    super.addResourceHandlers(registry);
                }
            }
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HandlerInterceptor interceptor = new LoginVerifyAspectHandler();
        registry.addInterceptor(interceptor);
        super.addInterceptors(registry);
    }
}
