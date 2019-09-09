package es.test.guice;

import javax.servlet.annotation.WebFilter;

@WebFilter(value = "/*")
public class GuiceFilter extends com.google.inject.servlet.GuiceFilter {
}
