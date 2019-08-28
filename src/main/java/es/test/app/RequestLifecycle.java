package es.test.app;

public interface RequestLifecycle {

	void beginLifecycle();

	void endLifecycle();

	void track(Disposable disposable);

	void addLifecycleListener(RequestLifecycleListener listener);

	void removeLifecycleListener(RequestLifecycleListener listener);
}
