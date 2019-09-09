package es.test.app;

import javax.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class DisposableTypeListener implements TypeListener {

    private final Provider<RequestLifecycle> requestLifecycleProvider;

    public DisposableTypeListener(Provider<RequestLifecycle> requestLifecycleProvider) {
        this.requestLifecycleProvider = requestLifecycleProvider;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {

        typeEncounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(I injectee) {
                System.out.println("*** afterInjection ***");

                if (injectee instanceof Disposable) {
                    RequestLifecycle requestLifecycle = requestLifecycleProvider.get();
                    requestLifecycle.track((Disposable) injectee);
                }
            }
        });
    }
}
