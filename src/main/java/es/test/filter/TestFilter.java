package es.test.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

//@WebFilter("/*")
public class TestFilter implements Filter {

	@Override
	public void init(FilterConfig arg0) throws ServletException {
        System.out.println("Filter init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
        System.out.println("Filter doFilter");
        filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
        System.out.println("Filter destroy");
	}
}
