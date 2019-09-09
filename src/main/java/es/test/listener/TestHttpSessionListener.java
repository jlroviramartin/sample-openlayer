package es.test.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

//@WebListener
public class TestHttpSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(final HttpSessionEvent event) {
        System.out.println("HttpSession sessionCreated " + event);
    }

    @Override
    public void sessionDestroyed(final HttpSessionEvent event) {
        System.out.println("HttpSession sessionDestroyed " + event);
    }
}