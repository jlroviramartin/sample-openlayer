package es.test.app;

import java.util.ArrayList;
import java.util.List;

public class RequestLifecycleImpl implements RequestLifecycle {

    private final List<Disposable> trackDisposables = new ArrayList<>();
    private final List<RequestLifecycleListener> lifecycleListener = new ArrayList<RequestLifecycleListener>();

    @Override
    public void track(Disposable disposable) {
        trackDisposables.add(disposable);
    }

    @Override
    public void beginLifecycle() {
        System.out.println("beginLifecycle");
    }

    @Override
    public void endLifecycle() {
        System.out.println("endLifecycle");

        for (RequestLifecycleListener listener : lifecycleListener) {
            try {
                listener.endLifecycle();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        lifecycleListener.clear();

        for (Disposable disposable : trackDisposables) {
            try {
                disposable.dispose();
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
        trackDisposables.clear();
    }

    @Override
    public void addLifecycleListener(RequestLifecycleListener listener) {
        lifecycleListener.add(listener);
    }

    @Override
    public void removeLifecycleListener(RequestLifecycleListener listener) {
        lifecycleListener.remove(listener);
    }
}
