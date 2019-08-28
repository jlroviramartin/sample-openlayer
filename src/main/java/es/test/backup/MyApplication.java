package es.test.backup;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.HK2IntoGuiceBridge;

import com.google.inject.Guice;
import com.google.inject.Injector;

//@ApplicationPath("/resources")
public class MyApplication extends ResourceConfig {

	public MyApplication() {
		initResourceLocator();
		registerServices();
	}

	private void initResourceLocator() {
		register(new ContainerLifecycleListener() {
			public void onStartup(Container container) {

				// access the ServiceLocator here
				InjectionManager injectionManager = container.getApplicationHandler().getInjectionManager();
				ServiceLocator serviceLocator = injectionManager.getInstance(ServiceLocator.class);

				GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

				// injector = Guice.createInjector(new HK2IntoGuiceBridge(serviceLocator), new
				// MyModule());
			}

			public void onReload(Container container) {
			}

			public void onShutdown(Container container) {
			}
		});
	}

	private void registerServices() {
		register(HelloWorldResource.class);
		register(EmployeeResource.class);
		register(MyRequestFilter.class);
		register(MyResponseFilter.class);
		register(RequestProcessorFilter.class);

		// register(new MyBinder());
	}
}
