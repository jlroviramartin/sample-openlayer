package es.test.app.old;

import java.lang.annotation.Annotation;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProvisionListener;

import es.test.app.Getter;
import es.test.app.GuiceUtils;

public class CloseableListener implements ProvisionListener {

	private final LifeCycleObjectRepository repo;
	private final Getter<Injector> injector;

	public CloseableListener(LifeCycleObjectRepository repo, Getter<Injector> injector) {
		this.repo = repo;
		this.injector = injector;
	}

	@Override
	public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {

		System.out.print("Inicio onProvision");

		Binding<?> binding = provisionInvocation.getBinding();

		Binding<?> xxx = injector.get().getBinding(binding.getKey());

		//boolean requestScoped = ServletScopes.isRequestScoped(binding);
		//boolean re = test(binding, myVisitor);

		Scope scope = GuiceUtils.getScopeInstanceOrNull(injector.get(), binding);

		System.out.print("Fin onProvision");
		/*
		 * if (provision instanceof Closeable && shouldManage(provisionInvocation)) {
		 * repo.register((Closeable) provision); }
		 */
	}

	private BindingScopingVisitor<Boolean> myVisitor = new BindingScopingVisitor<Boolean>() {
		@Override
		public Boolean visitEagerSingleton() {
			System.out.println("  - visitEagerSingleton");
			return true;
		}

		@Override
		public Boolean visitScope(Scope scope) {
			System.out.println("  - visitScope");
			return scope == Scopes.SINGLETON || scope == ServletScopes.REQUEST;
		}

		@Override
		public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
			System.out.println("  - visitScopeAnnotation");
			return scopeAnnotation.isAssignableFrom(javax.inject.Singleton.class)
					|| scopeAnnotation.isAssignableFrom(com.google.inject.Singleton.class);
		}

		@Override
		public Boolean visitNoScoping() {
			System.out.println("  - visitNoScoping");
			return false;
		}
	};

	private boolean shouldManage(ProvisionInvocation<?> provisionInvocation) {

		Binding<?> binding = provisionInvocation.getBinding();
		Key<?> key = binding.getKey();

		return binding.acceptScopingVisitor(myVisitor);
	}

}
