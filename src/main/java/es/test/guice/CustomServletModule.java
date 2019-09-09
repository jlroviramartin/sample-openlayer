package es.test.guice;

import static java.util.Arrays.asList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServlet;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import com.google.inject.servlet.ServletModule;

import es.test.guice.annotations.GuiceWebFilter;
import es.test.guice.annotations.GuiceWebServlet;

public class CustomServletModule extends ServletModule {

    @SuppressWarnings("unchecked")
    protected void defaultServeInPackage(String _package) {
        Reflections reflections = new Reflections( //
                new ConfigurationBuilder() //
                        .setUrls(ClasspathHelper.forPackage(_package)) //
                        .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner()));

        for (Class<?> _class : reflections.getTypesAnnotatedWith(GuiceWebServlet.class)) {
            if (HttpServlet.class.isAssignableFrom(_class)) {
                defaultServe((Class<? extends HttpServlet>) _class);
            }
        }

        for (Class<?> _class : reflections.getTypesAnnotatedWith(GuiceWebFilter.class)) {
            if (Filter.class.isAssignableFrom(_class)) {
                defaultFilter((Class<? extends Filter>) _class);
            }
        }
    }

    protected void defaultServe(Class<? extends HttpServlet> servletKey) {
        GuiceWebServlet webServlet = servletKey.getAnnotation(GuiceWebServlet.class);
        if (webServlet != null) {
            String[] urlPatterns = webServlet.value();
            if (urlPatterns == null || urlPatterns.length == 0) {
                urlPatterns = webServlet.urlPatterns();
            }

            System.out.println("***** Serve " + servletKey + " as " + Arrays.toString(urlPatterns));

            serve(asList(urlPatterns)).with(servletKey, getInitParams(webServlet.initParams()));
        } else {
            System.out.println("***** Cannot serve " + servletKey);
        }
    }

    protected void defaultFilter(Class<? extends Filter> filterKey) {
        GuiceWebFilter webFilter = filterKey.getAnnotation(GuiceWebFilter.class);
        if (webFilter != null) {
            String[] urlPatterns = webFilter.value();
            if (urlPatterns == null || urlPatterns.length == 0) {
                urlPatterns = webFilter.urlPatterns();
            }

            System.out.println("***** Filter " + filterKey + " as " + Arrays.toString(urlPatterns));

            filter(asList(urlPatterns)).through(filterKey, getInitParams(webFilter.initParams()));
        } else {
            System.out.println("***** Cannot filter " + filterKey);
        }
    }

    private Map<String, String> getInitParams(WebInitParam[] webInitParams) {
        Map<String, String> initParams = new HashMap<String, String>();
        if (webInitParams != null && webInitParams.length > 0) {
            for (WebInitParam webInitParam : asList(webInitParams)) {
                String name = webInitParam.name();
                String value = webInitParam.value();
                initParams.put(name, value);
            }
        }
        return initParams;
    }
}