package es.test.app.old;

import javax.inject.Inject;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.BindingImpl;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.ProvisionListener.ProvisionInvocation;

import es.test.app.GuiceUtils;
import es.test.app.GuiceUtils.MyBindingScopingVisitor;
import es.test.app.GuiceUtils.MyBindingTargetVisitor;

public class MyProvisionListener implements ProvisionListener {

	@Override
	public <T> void onProvision(ProvisionInvocation<T> provision) {

		System.out.println("-> BEGIN");

		Binding<T> binding = provision.getBinding();
		Key<T> key = binding.getKey();
		System.out.println("*** key " + key);

		TypeLiteral<T> typeLiteral = key.getTypeLiteral();
		System.out.println("*** typeLiteral " + typeLiteral + " ***");

		if (key.hasAttributes()) {
			System.out.println("*** hasAttributes ***");
		}

		T t = provision.provision();
		System.out.println("*** provision " + t);

		BindingImpl<T> bindingImpl = (BindingImpl<T>)binding;
		Scope scope = bindingImpl.getScoping().getScopeInstance();
		System.out.println("*** scope " + scope);

		//ConstructorBinding<T> cbinding = (ConstructorBinding<T>) binding;

		boolean requestScoped = ServletScopes.isRequestScoped(binding);

		// Scope scope = getScopeInstanceOrNull(binding.get, binding);
		Object ret1 = binding.acceptScopingVisitor(new GuiceUtils.MyBindingScopingVisitor<>());
		Object ret2 = binding.acceptTargetVisitor(new GuiceUtils.MyBindingTargetVisitor<>());

		System.out.println("<- END");
	}
}
