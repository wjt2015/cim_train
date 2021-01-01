package wjt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.MyDelegatingWebMvcConfiguration;
import wjt.controller.HomeController;
import wjt.filter.CorsFilter;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@Import(MyDelegatingWebMvcConfiguration.class)
@ComponentScan(basePackageClasses = {HomeController.class})
public class WebConfig {

    /**
     * 支持文件上传;
     * (https://docs.spring.io/spring-framework/docs/4.3.8.RELEASE/spring-framework-reference/htmlsingle/#mvc-multipart);
     */
    @Bean
    public MultipartResolver multipartResolver() {
        final String charsetName = StandardCharsets.UTF_8.name();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSizePerFile(-1);
        multipartResolver.setDefaultEncoding(charsetName);
        log.info("multipartResolver={};charsetName={};", multipartResolver, charsetName);
        return multipartResolver;
    }

}
