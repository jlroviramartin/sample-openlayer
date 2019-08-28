package es.test.guice;

import javax.servlet.annotation.WebListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

@WebListener
public class GuiceServletConfig extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {
		Injector injector = buildInjector();
		GuiceUtils.setInjector(injector);
		return injector;
	}

	private Injector buildInjector() {
		System.out.println("***** Building the Guice's Injector");

		Injector injector = Guice.createInjector( //
				new MainServletModule(), //
				new MainModule());
		return injector;
	}
}
