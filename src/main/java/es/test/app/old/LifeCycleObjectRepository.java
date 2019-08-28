package es.test.app.old;

import java.io.Closeable;
import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;

public class LifeCycleObjectRepository {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(LifeCycleObjectRepository.class);

	private final Set<Closeable> closeableObjects = Sets.newConcurrentHashSet();

	void register(Closeable closeable) {
		if (closeableObjects.add(closeable)) {
			// LOGGER.info("Register {} for close at shutdown", closeable);
			System.out.println("Register {} for close at shutdown");
		}
	}

	public synchronized void closeAll() {
		closeableObjects.forEach(c -> {
			try {
				// LOGGER.info("Close {}", c);
				System.out.println("Close {}");
				c.close();
			} catch (IOException e) {
				// LOGGER.error("Error closing object", e);
				System.out.println("Error closing object: " + e);
			}
		});
		closeableObjects.clear();
	}
}
