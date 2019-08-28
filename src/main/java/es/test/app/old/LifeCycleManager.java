package es.test.app.old;

import java.util.stream.Collectors;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import es.test.app.Getter;

import static java.util.stream.StreamSupport.stream;

public class LifeCycleManager {

	private final LifeCycleObjectRepository repo = new LifeCycleObjectRepository();
	private final Injector injector;

	public LifeCycleManager(Module... modules) {
		this(ImmutableList.copyOf(modules));
	}

	public LifeCycleManager(Iterable<Module> modules) {
		this.injector = Guice.createInjector(enableLifeCycleManagement(repo, modules, () -> getInjector()));
		addShutdownHook();
	}

	public Injector getInjector() {
		return injector;
	}

	public <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}

	private void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(repo::closeAll));
	}

	private static Iterable<Module> enableLifeCycleManagement(LifeCycleObjectRepository repo,
			Iterable<Module> modules, Getter<Injector> injector) {
		return stream(modules.spliterator(), false) //
				.map(m -> new LifeCycleAwareModule(repo, m, injector)) //
				.collect(Collectors.toList());
	}
}