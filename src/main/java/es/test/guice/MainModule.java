package es.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.ProvisionListener;

import es.test.DataSourceLocator;
import es.test.JndiDataSourceLocator;
import es.test.app.GuiceUtils;
import es.test.services.GisServices;
import es.test.services.GisServicesImp;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        System.out.println("***** Configuring MainModule module");

        bind(DataSourceLocator.class) //
                .to(JndiDataSourceLocator.FirstJndi.class) //
                .in(ServletScopes.REQUEST);

        bind(GisServices.class)//
                .to(GisServicesImp.class) //
                .in(ServletScopes.REQUEST);

        bindListener(Matchers.any(), new ProvisionListener() {

            @Override
            public <T> void onProvision(ProvisionInvocation<T> provision) {

                System.out.println("-> BEGIN");

                Binding<?> binding = provision.getBinding();
                boolean requestScoped = ServletScopes.isRequestScoped(binding);

                // Scope scope = getScopeInstanceOrNull(binding.get, binding);
                Object ret1 = binding.acceptScopingVisitor(new GuiceUtils.MyBindingScopingVisitor<>());
                Object ret2 = binding.acceptTargetVisitor(new GuiceUtils.MyBindingTargetVisitor<>());

                System.out.println("<- END");
            }
        });

    }
}
