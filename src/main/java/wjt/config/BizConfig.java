package wjt.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import wjt.filter.CorsFilter;
import wjt.service.impl.SdpServiceImpl;
import wjt.websocket.WebRTCTextWebSocketHandler;

@Configuration
@ComponentScan(basePackageClasses = {CorsFilter.class, SdpServiceImpl.class, WebRTCTextWebSocketHandler.class})
public class BizConfig {

}
