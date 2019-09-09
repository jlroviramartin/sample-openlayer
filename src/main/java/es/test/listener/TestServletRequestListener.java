package es.test.listener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

//@WebListener
public class TestServletRequestListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        System.out.println("ServletRequest requestInitialized, request by: " + event.getServletRequest().getRemoteAddr());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        System.out.println("ServletRequest requestDestroyed, request by: " + event.getServletRequest().getRemoteAddr());
    }
}
