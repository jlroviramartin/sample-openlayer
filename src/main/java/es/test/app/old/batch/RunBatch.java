package es.test.app.old.batch;

import javax.inject.Named;

import com.google.inject.Inject;

public class RunBatch {

	private final SimpleScope scope;

	@Inject
	public RunBatch(@Named("batchScope") SimpleScope scope) {
		this.scope = scope;
	}

	/**
	 * Runs {@code runnable} in batch scope.
	 */
	public void scopeRunnable(Runnable runnable) {
		scope.enter();
		try {
			// explicitly seed some seed objects...
			//scope.seed(Key.get(SomeObject.class), someObject);

			// create and access scoped objects
			runnable.run();

		} finally {
			scope.exit();
		}
	}
}
