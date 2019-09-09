package es.test.backup;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.google.inject.servlet.GuiceFilter;

//@WebFilter("/*")
public class MyGuiceFilter extends GuiceFilter {

    public MyGuiceFilter() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        System.out.println("Begin GuiceFilter");
        try {
            super.doFilter(servletRequest, servletResponse, filterChain);
        } finally {
            System.out.println("End GuiceFilter");
        }
    }
}
