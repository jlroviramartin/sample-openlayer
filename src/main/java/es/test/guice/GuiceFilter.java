package es.test.guice;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

@WebFilter(value = "/*")
public class GuiceFilter extends com.google.inject.servlet.GuiceFilter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		// https://epsg.io/3857

		super.doFilter(servletRequest, servletResponse, filterChain);
	}
}
