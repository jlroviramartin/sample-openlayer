package es.test.app.old.batch;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class BatchScopeModule extends AbstractModule {

	@Override
	public void configure() {
		SimpleScope batchScope = new SimpleScope();

		// tell Guice about the scope
		bindScope(BatchScoped.class, batchScope);

		// make our scope instance injectable
		bind(SimpleScope.class).annotatedWith(Names.named("batchScope")).toInstance(batchScope);

		bind(RunBatch.class).in(Singleton.class);
	}
}
