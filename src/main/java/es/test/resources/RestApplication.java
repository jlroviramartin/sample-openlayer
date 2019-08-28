package es.test.resources;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import es.test.guice.GuiceBridgeLifecycleListener;
import es.test.json.ObjectMapperProvider;

@ApplicationPath("/rest")
public class RestApplication extends ResourceConfig {

	public RestApplication() {
		configResources();
	}

	private void configResources() {

		System.out.println("***** RestApplication: configResources");

		// json mapping
		register(ObjectMapperProvider.class);
		register(JacksonFeature.class);

		// Resources REST
		register(TestResource.class);
		register(MapResource.class);

		// Providers
		register(CORSFilter.class);

		// Listeners
		register(GuiceBridgeLifecycleListener.class);
	}
}
