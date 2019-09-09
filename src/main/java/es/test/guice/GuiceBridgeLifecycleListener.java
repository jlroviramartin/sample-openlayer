package es.test.guice;

import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;

public class GuiceBridgeLifecycleListener implements ContainerLifecycleListener {

    @Override
    public void onStartup(Container container) {
        InjectionManager injectionManager = container.getApplicationHandler().getInjectionManager();
        GuiceUtils.setInjectionManager(injectionManager);
    }

    @Override
    public void onReload(Container container) {
    }

    @Override
    public void onShutdown(Container container) {
    }
}
