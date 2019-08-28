package es.test.backup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// https://stackoverflow.com/questions/10549241/how-to-use-dependency-injection-in-servlet
//@WebListener
public class MyServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.addServlet("MyServlet", new HelloWorldServlet("Everyone")).addMapping("/MyServlet");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}