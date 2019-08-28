package es.test.guice;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.google.inject.Injector;

public class GuiceUtils {

	private static Injector injector;
	private static InjectionManager injectionManager;

	public static Injector getInjector() {
		return injector;
	}

	static void setInjector(Injector value) {
		System.out.println("Setup Guice's Injector");
		if (injector != null && value != null) {
			System.out.println("***¿ERROR?*** Already exists an Injector");
		}

		injector = value;
		tryBuildBridge();
	}

	static InjectionManager getInjectionManager() {
		return injectionManager;
	}

	static void setInjectionManager(InjectionManager value) {
		System.out.println("Setup Hk2's  InjectionManager");
		if (injectionManager != null && value != null) {
			System.out.println("***¿ERROR?*** Already exists an InjectionManager");
		}

		injectionManager = value;
		tryBuildBridge();
	}

	static boolean tryBuildBridge() {
		if (injector != null && injectionManager != null) {
			System.out.println("***** 1) Preparing the bridge between Hk2 and Guice: GuiceBridge");

			ServiceLocator serviceLocator = injectionManager.getInstance(ServiceLocator.class);
			GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);

			System.out.println("***** 2) Building the bridge between Hk2 and Guice: GuiceBridge");

			GuiceIntoHK2Bridge guiceBridge = injectionManager.getInstance(GuiceIntoHK2Bridge.class);
			guiceBridge.bridgeGuiceInjector(injector);
			return true;
		}
		return false;
	}
}