package es.test.app.old;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class MyTypeListener implements TypeListener {

	@Override
	public <I> void hear(final TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

		typeEncounter.register(new InjectionListener<I>() {
			@Override
			public void afterInjection(I i) {
				System.out.println("*** afterInjection ***");

				/*if (i instanceof Disposable) {
					disposables.add((Disposable) i);
				}*/
			}
		});
	}
}
