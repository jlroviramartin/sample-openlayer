package es.test.guice.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.servlet.annotation.WebInitParam;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GuiceWebFilter {

	/**
	 * The URL patterns of the servlet
	 */
	String[] value() default {};

	/**
	 * The URL patterns of the servlet
	 */
	String[] urlPatterns() default {};

	/**
	 * The init parameters of the servlet
	 */
	WebInitParam[] initParams() default {};
}
