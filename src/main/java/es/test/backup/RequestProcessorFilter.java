package es.test.backup;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

//@WebFilter("/*")
public class RequestProcessorFilter implements Filter {

    @Inject
    private RequestContext requestContext;

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        System.out.println("Begin doFilter");
        //requestContext.startRequest();
        try {
            filterChain.doFilter(request, response);
        } finally {
            //requestContext.stopRequest();
            System.out.println("End doFilter");
        }
    }

    @Override
    public void destroy() {
    }
}
