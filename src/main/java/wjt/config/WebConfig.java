package wjt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.MyDelegatingWebMvcConfiguration;

@Slf4j
@Configuration
@Import(MyDelegatingWebMvcConfiguration.class)
public class WebConfig {
}
