package es.test.app.old;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;

import es.test.app.Getter;

public class LifeCycleAwareModule extends AbstractModule {
	private final LifeCycleObjectRepository repo;
	private final Module module;
	private final Getter<Injector> injector;

	protected LifeCycleAwareModule(LifeCycleObjectRepository repo, Module module, Getter<Injector> injector) {
		this.repo = repo;
		this.module = module;
		this.injector = injector;
	}

	@Override
	protected void configure() {
		bindListener(Matchers.any(), new CloseableListener(repo, this.injector));
		install(module);
	}
}
