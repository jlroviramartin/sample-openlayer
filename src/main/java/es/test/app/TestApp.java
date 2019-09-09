package es.test.app;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;

public class TestApp {

    public static void main(String[] args) {

        runInjection.runInRequest((injector) -> {

            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
        });

        runInjection.runInRequest((injector) -> {

            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
            System.out.println(injector.getInstance(Counter.class));
        });
    }

    private static RunInjection runInjection = new RunInjection() {

        private final Provider<RequestLifecycle> requestLifecycleProvider = //
                () -> getInjector().getInstance(RequestLifecycle.class);

        @Override
        protected Module[] getModules() {
            return new Module[] { //
                    new ServletModule(), //
                    new AbstractModule() {
                        @Override
                        protected void configure() {
                            bindListener(Matchers.any(), new DisposableTypeListener(requestLifecycleProvider));

                            bind(RequestLifecycle.class).to(RequestLifecycleImpl.class).in(ServletScopes.REQUEST);
                            bind(Counter.class).to(SimpleCounter.class).in(ServletScopes.REQUEST);
                        }
                    } };
        }
    };

    public static interface Counter {
        int getIndex();
    }

    public static class SimpleCounter implements Counter, RequestLifecycleListener {
        private static int count = 0;
        private int index = count++;

        private boolean isDisposed = false;

        @Inject
        public SimpleCounter(RequestLifecycle requestLifecycle) {
            System.out.println("SimpleCounter created: " + this);
            requestLifecycle.addLifecycleListener(this);
        }

        @Override
        public int getIndex() {
            checkDisposed();
            return index;
        }

        @Override
        public String toString() {
            checkDisposed();
            return Integer.toString(index);
        }

        public void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                System.out.println("SimpleCounter disposed: " + this);
            }
        }

        private void checkDisposed() {
            assert !isDisposed;
        }

        @Override
        public void endLifecycle() {
            System.out.println("SimpleCounter endLifecycle");
            dispose();
        }
    }

    public static class SimpleCounter2 implements Counter, Disposable {
        private static int count = 0;
        private int index = count++;

        private boolean isDisposed = false;

        public SimpleCounter2() {
            System.out.println("SimpleCounter created: " + this);
        }

        @Override
        public int getIndex() {
            checkDisposed();
            return index;
        }

        @Override
        public String toString() {
            checkDisposed();
            return Integer.toString(index);
        }

        @Override
        public void dispose() {
            if (!isDisposed) {
                isDisposed = true;
                System.out.println("SimpleCounter disposed: " + this);
            }
        }

        private void checkDisposed() {
            assert !isDisposed;
        }
    }

}
