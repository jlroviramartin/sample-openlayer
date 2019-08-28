package es.test.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Injector;
import es.test.DataSourceLocator;
import es.test.guice.annotations.GuiceWebServlet;

@Singleton
@GuiceWebServlet("/testservlet")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 317217932668830396L;

	// @Inject
	private Injector injector;

	// @Inject
	private Provider<DataSourceLocator> dataSourceLocatorProvider;

	@Inject
	public TestServlet(Injector injector, Provider<DataSourceLocator> dataSourceLocatorProvider) {
		this.injector = injector;
		this.dataSourceLocatorProvider = dataSourceLocatorProvider;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		DataSourceLocator locator = dataSourceLocatorProvider.get();
		DataSourceLocator locator2 = dataSourceLocatorProvider.get();

		resp.setContentType("text/html;charset=UTF-8");

		PrintWriter out = resp.getWriter();
		try {
			out.println("<!DOCTYPE html>");
			out.println("<html><head>");
			out.println("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
			out.println("<title>Hello, World</title></head>");
			out.println("<body>");

			out.println("<h1>Hello, world!</h1>");
			out.println("<h2>Locator: " + locator + "</h>");
			out.println("<h2>Locator2: " + locator2 + "</h>");

			out.println("<p>Request URI: " + req.getRequestURI() + "</p>");
			out.println("<p>Protocol: " + req.getProtocol() + "</p>");
			out.println("<p>PathInfo: " + req.getPathInfo() + "</p>");
			out.println("<p>Remote Address: " + req.getRemoteAddr() + "</p>");

			out.println("</body>");
			out.println("</html>");
		} finally {
			out.close(); // Always close the output writer
		}
	}
}
