package es.test.backup;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import es.test.DataSourceLocator;
import es.test.JndiDataSourceLocator;
import es.test.JndiDataSourceLocator.FirstJndi;

public class MyBinder extends AbstractBinder {
    public MyBinder() {

    }

    @Override
    protected void configure() {
        bindAsContract(JndiDataSourceLocator.FirstJndi.class)
                .to(DataSourceLocator.class)
                .in(RequestScope.class);

        bindFactory(RequestContextFactory.class, RequestScope.class)
                .to(RequestContext.class)
                .in(RequestScope.class);
    }

    private static class RequestContextFactory implements Factory<RequestContext> {

        @Override
        public RequestContext provide() {
            return new RequestContext();
        }

        @Override
        public void dispose(RequestContext instance) {
            //instance.close();
        }
    }
}
