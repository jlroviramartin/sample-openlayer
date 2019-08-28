package es.test.backup;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletScopes;

import es.test.DataSourceLocator;
import es.test.JndiDataSourceLocator;
import es.test.JndiDataSourceLocator.FirstJndi;

public class MyModule extends AbstractModule {

	@Override
	public void configure() {
		bind(DataSourceLocator.class).to(JndiDataSourceLocator.FirstJndi.class).in(ServletScopes.REQUEST);

		// bindFactory(RequestContextFactory.class,
		// RequestScope.class).to(RequestContext.class).in(RequestScope.class);

	}
}
