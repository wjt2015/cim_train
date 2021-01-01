package wjt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import wjt.config.BizConfig;
import wjt.config.WebConfig;
import wjt.filter.CorsFilter;
import wjt.websocket.MyWebSocketConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import java.util.EnumSet;

@Slf4j
public class MyAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{BizConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        log.info("getServletConfigClasses finish!");
        return new Class<?>[]{WebConfig.class, MyWebSocketConfigurer.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.json", "*.ws"};
    }

    @Override
    protected Filter[] getServletFilters() {
        DelegatingFilterProxy corsFilter = new DelegatingFilterProxy("corsFilter");
        corsFilter.setContextAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        return new Filter[]{corsFilter};
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {

/*
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("corsFilter", filter);
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);
        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
*/

        FilterRegistration.Dynamic filterRegistration = super.registerServletFilter(servletContext, filter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        log.info("filter={};registration!", filter);
        return filterRegistration;
    }

}
