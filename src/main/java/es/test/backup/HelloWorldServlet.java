package es.test.backup;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "MyServlet", urlPatterns = {"/MyServlet"})
public class HelloWorldServlet extends HttpServlet {
    private final String name;

    public HelloWorldServlet() {
        this("DEFAULT");
    }

    public HelloWorldServlet(String name) {
        this.name = name;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hello, " + name + "!");
    }
}
