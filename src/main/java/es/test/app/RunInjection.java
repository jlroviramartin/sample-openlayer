package es.test.app;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.servlet.RequestScoper;
import com.google.inject.servlet.RequestScoper.CloseableScope;
import com.google.inject.servlet.ServletScopes;

public abstract class RunInjection {

    private Injector injector;

    public Injector getInjector() {
        if (injector == null) {
            injector = buildInjector();
        }
        return injector;
    }

    protected abstract Module[] getModules();

    protected Injector buildInjector() {
        return Guice.createInjector(getModules());
    }

    protected void beforeScope() {
    }

    protected void afterScope() {
    }

    public void runInRequest(Consumer<Injector> consumer) {
        getInjector();

        Map<Key<?>, Object> seedMap = new HashMap<>();
        RequestScoper requestScoper = ServletScopes.scopeRequest(seedMap);
        try (CloseableScope scope = requestScoper.open()) {
            RequestLifecycle requestLifecycle = getInjector().getInstance(RequestLifecycle.class);

            requestLifecycle.beginLifecycle();
            try {
                beforeScope();
                consumer.accept(getInjector());
                afterScope();
            } finally {
                requestLifecycle.endLifecycle();
            }
        }
    }
}
