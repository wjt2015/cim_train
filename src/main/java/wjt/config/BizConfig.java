package wjt.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import wjt.filter.CorsFilter;

@Configuration
@ComponentScan(basePackageClasses = {CorsFilter.class})
public class BizConfig {

}
