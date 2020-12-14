package wjt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import wjt.config.BizConfig;
import wjt.config.WebConfig;
import wjt.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

@Slf4j
public class MyAnnotationConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{BizConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"*.json"};
    }

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{new CorsFilter()};
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("corsFilter", filter);

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.allOf(DispatcherType.class);

        filterRegistration.addMappingForUrlPatterns(dispatcherTypes, false, "/*");

        log.info("filter={};registration!", filter);
        return filterRegistration;
    }

}
