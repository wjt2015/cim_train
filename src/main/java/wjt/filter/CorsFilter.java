package wjt.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域详解 been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource
 * (https://www.cnblogs.com/cxygg/p/12419502.html);
 * <p>
 * cors,
 * (https://juejin.cn/post/6844903873400799240);
 * (https://juejin.cn/post/6844903568852385806);
 * (https://juejin.cn/post/6844903838621630477);
 * (https://juejin.cn/post/6887744164079878151);
 * (https://juejin.cn/search?query=cors&type=all);
 * <p>
 * <p>
 * //---
 * DelegatingFilterProxy;
 */
@Slf4j
@Order(value = 1)
@Service
//@WebFilter(urlPatterns = {"/*"})
public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("corsFilter init finish!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        final long start = System.currentTimeMillis();

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (CorsUtils.isCorsRequest(httpServletRequest)) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*"); //  这里最好明确的写允许的域名
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization");
        }

        chain.doFilter(request, response);
        log.info("cors allow!requestURL={};method={};protocol={};elapsed={}ms;", httpServletRequest.getRequestURL(), httpServletRequest.getMethod(), httpServletRequest.getProtocol(), (System.currentTimeMillis() - start));

    }

    @Override
    public void destroy() {

    }
}
