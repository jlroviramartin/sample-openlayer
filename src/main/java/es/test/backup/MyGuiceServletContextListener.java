package es.test.backup;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ApplicationHandler;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

//@WebListener
public class MyGuiceServletContextListener extends GuiceServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(/*new HK2IntoGuiceBridge(MyApplication.getServiceLocator()),*/ new ServletModule() {

            @Override
            protected void configureServlets() {
                // serve("*.html").with(MyServlet.class);
                // filter("/*").through(MyFilter.class);
                super.configureServlets();
            }
        }, new MyModule());
    }
}
