package es.test.guice;

import es.test.servlets.TestServlet;

public class MainServletModule extends CustomServletModule {

    @Override
    protected void configureServlets() {
        System.out.println("***** Configure servlets");

        //defaultServeInPackage("es.test");
        defaultServe(TestServlet.class);
    }
}
