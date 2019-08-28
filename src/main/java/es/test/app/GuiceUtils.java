package es.test.app;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.internal.LinkedBindingImpl;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.MapBinderBinding;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.MultibinderBinding;
import com.google.inject.multibindings.MultibindingsTargetVisitor;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.multibindings.OptionalBinderBinding;
import com.google.inject.servlet.InstanceFilterBinding;
import com.google.inject.servlet.InstanceServletBinding;
import com.google.inject.servlet.LinkedFilterBinding;
import com.google.inject.servlet.LinkedServletBinding;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletModuleTargetVisitor;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.servlet.ServletModule.FilterKeyBindingBuilder;
import com.google.inject.servlet.ServletModule.ServletKeyBindingBuilder;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.BindingTargetVisitor;
import com.google.inject.spi.ConstructorBinding;
import com.google.inject.spi.ConvertedConstantBinding;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import com.google.inject.spi.ExposedBinding;
import com.google.inject.spi.InstanceBinding;
import com.google.inject.spi.LinkedKeyBinding;
import com.google.inject.spi.ProviderBinding;
import com.google.inject.spi.ProviderInstanceBinding;
import com.google.inject.spi.ProviderKeyBinding;
import com.google.inject.spi.ProvidesMethodBinding;
import com.google.inject.spi.ProvidesMethodTargetVisitor;
import com.google.inject.spi.UntargettedBinding;

public class GuiceUtils {

	public static List<Binding<?>> getBindings(Binding<?> binding) {
		List<Binding<?>> bindings = new ArrayList<>();

		bindings.add(binding);
		do {

			if (binding instanceof LinkedBindingImpl) { // LinkedKeyBinding
				LinkedBindingImpl<?> linkedBinding = (LinkedBindingImpl) binding;
				Key key = linkedBinding.getLinkedKey();
				Injector injector = linkedBinding.getInjector();

				if (injector != null) {
					binding = injector.getBinding(key);
					bindings.add(binding);
					continue;
				}
			} else if (binding instanceof ExposedBinding) {
				ExposedBinding<?> exposedBinding = (ExposedBinding) binding;
				Key key = exposedBinding.getKey();
				Injector injector = exposedBinding.getPrivateElements().getInjector();

				if (injector != null) {
					binding = injector.getBinding(key);
					bindings.add(binding);
					continue;
				}
			} else {
				break;
			}
		} while (true);

		return bindings;
	}

	public static boolean test(Binding<?> binding, BindingScopingVisitor<Boolean> visitor) {
		do {
			boolean singleton = binding.acceptScopingVisitor(visitor);
			if (singleton) {
				return true;
			}

			if (binding instanceof LinkedBindingImpl) { // LinkedKeyBinding
				LinkedBindingImpl<?> linkedBinding = (LinkedBindingImpl) binding;
				Key key = linkedBinding.getLinkedKey();
				Injector injector = linkedBinding.getInjector();

				if (injector != null) {
					binding = injector.getBinding(key);
					continue;
				}
			} else if (binding instanceof ExposedBinding) {
				ExposedBinding<?> exposedBinding = (ExposedBinding) binding;
				Key key = exposedBinding.getKey();
				Injector injector = exposedBinding.getPrivateElements().getInjector();

				if (injector != null) {
					binding = injector.getBinding(key);
					continue;
				}
			} else {
				System.out.println("¿Error?");
			}

			return false;
		} while (true);
	}

	public static <T> Scope getScopeInstanceOrNull(Injector injector, final Binding<T> binding) {

		return binding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Scope>() {

			@Override
			public Scope visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
				throw new RuntimeException(String.format("I don't know how to handle the scopeAnnotation: %s",
						scopeAnnotation.getCanonicalName()));
			}

			@Override
			public Scope visitEagerSingleton() {
				return Scopes.SINGLETON;
			}

			public Scope visitScope(Scope scope) {
				return scope;
			}

			@Override
			public Scope visitNoScoping() {
				if (binding instanceof LinkedKeyBinding<?>) {
					Binding<?> childBinding = injector.getBinding(((LinkedKeyBinding<T>) binding).getLinkedKey());
					return getScopeInstanceOrNull(injector, childBinding);
				}
				return null;
			}

		});
	}

	// BindingTargetVisitor, MultibindingsTargetVisitor,
	// ProvidesMethodTargetVisitor, ServletModuleTargetVisitor
	public static class MyBindingTargetVisitor<T, V> implements BindingTargetVisitor<T, V>,
			MultibindingsTargetVisitor<T, V>, ProvidesMethodTargetVisitor<T, V>, ServletModuleTargetVisitor<T, V> {

		private V delegateVisit(Binding<? extends T> binding) {
			if (binding instanceof InstanceBinding) {
				return visit((InstanceBinding<? extends T>) binding);
			}
			if (binding instanceof ProviderInstanceBinding) {
				return visit((ProviderInstanceBinding<? extends T>) binding);
			}
			if (binding instanceof ProviderKeyBinding) {
				return visit((ProviderKeyBinding<? extends T>) binding);
			}
			if (binding instanceof LinkedKeyBinding) {
				return visit((LinkedKeyBinding<? extends T>) binding);
			}
			if (binding instanceof ExposedBinding) {
				return visit((ExposedBinding<? extends T>) binding);
			}
			if (binding instanceof UntargettedBinding) {
				return visit((UntargettedBinding<? extends T>) binding);
			}
			if (binding instanceof ConstructorBinding) {
				return visit((ConstructorBinding<? extends T>) binding);
			}
			if (binding instanceof ConvertedConstantBinding) {
				return visit((ConvertedConstantBinding<? extends T>) binding);
			}
			if (binding instanceof ProviderBinding) {
				return visit((ProviderBinding<? extends T>) binding);
			}
			return visitDefault(binding);
		}

		protected V visitDefault(Binding<? extends T> binding) {
			System.out.println("    - " + binding.getClass());
			return null;
		}

		// BindingTargetVisitor

		@Override
		public V visit(InstanceBinding<? extends T> binding) {
			System.out.println("  - InstanceBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(ProviderInstanceBinding<? extends T> binding) {
			System.out.println("  - ProviderInstanceBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(ProviderKeyBinding<? extends T> binding) {
			System.out.println("  - ProviderKeyBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(LinkedKeyBinding<? extends T> binding) {
			System.out.println("  - LinkedKeyBinding");

			if (binding instanceof LinkedBindingImpl) {
				LinkedBindingImpl<? extends T> linkedBinding = (LinkedBindingImpl<? extends T>) binding;
				Key key = linkedBinding.getLinkedKey();
				Injector injector = linkedBinding.getInjector();

				if (injector != null) {
					return delegateVisit((Binding<? extends T>) injector.getBinding(key));
				}
			}
			return visitDefault(binding);
		}

		@Override
		public V visit(ExposedBinding<? extends T> binding) {
			System.out.println("  - ExposedBinding");

			ExposedBinding<? extends T> exposedBinding = (ExposedBinding<? extends T>) binding;
			Key key = exposedBinding.getKey();
			Injector injector = exposedBinding.getPrivateElements().getInjector();

			if (injector != null) {
				return delegateVisit((Binding<? extends T>) injector.getBinding(key));
			}
			return visitDefault(binding);
		}

		@Override
		public V visit(UntargettedBinding<? extends T> binding) {
			System.out.println("  - UntargettedBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(ConstructorBinding<? extends T> binding) {
			System.out.println("  - ConstructorBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(ConvertedConstantBinding<? extends T> binding) {
			System.out.println("  - ConvertedConstantBinding");
			return visitDefault(binding);
		}

		@Override
		public V visit(ProviderBinding<? extends T> binding) {
			System.out.println("  - ProviderBinding");
			return visitDefault(binding);
		}

		// MultibindingsTargetVisitor

		public V visit(MultibinderBinding<? extends T> multibinding) {
			System.out.println("  - MultibinderBinding");
			return null;
		}

		public V visit(MapBinderBinding<? extends T> mapbinding) {
			System.out.println("  - MapBinderBinding");
			return null;
		}

		public V visit(OptionalBinderBinding<? extends T> optionalbinding) {
			System.out.println("  - OptionalBinderBinding");
			return null;
		}

		// ProvidesMethodTargetVisitor

		public V visit(ProvidesMethodBinding<? extends T> providesMethodBinding) {
			System.out.println("  - ProvidesMethodBinding");
			return null;
		}

		// ServletModuleTargetVisitor

		public V visit(LinkedFilterBinding binding) {
			System.out.println("  - LinkedFilterBinding");
			return null;
		}

		public V visit(InstanceFilterBinding binding) {
			System.out.println("  - InstanceFilterBinding");
			return null;
		}

		public V visit(LinkedServletBinding binding) {
			System.out.println("  - LinkedServletBinding");
			return null;
		}

		public V visit(InstanceServletBinding binding) {
			System.out.println("  - InstanceServletBinding");
			return null;
		}
	}


	public static class MyBindingScopingVisitor<V> extends DefaultBindingScopingVisitor<V> implements BindingScopingVisitor<V> {


		public V visitEagerSingleton() {
			System.out.println("  - visitEagerSingleton");
			return super.visitEagerSingleton();
		}

		public V visitScope(Scope scope) {
			System.out.println("  - visitScope");
			return super.visitScope(scope);
		}

		public V visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
			System.out.println("  - visitScopeAnnotation");
			return super.visitScopeAnnotation(scopeAnnotation);
		}

		public V visitNoScoping() {
			System.out.println("  - visitNoScoping");
			return super.visitNoScoping();
		}
	}
}
