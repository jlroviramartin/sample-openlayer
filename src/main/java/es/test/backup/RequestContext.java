package es.test.backup;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import org.glassfish.hk2.api.ActiveDescriptor;
import org.glassfish.hk2.api.ServiceHandle;
import org.glassfish.hk2.extras.operation.OperationContext;

//@Singleton
public class RequestContext extends OperationContext<RequestScope> {

	private final HashMap<ActiveDescriptor<?>, Object> requestScopedEntities = new HashMap<ActiveDescriptor<?>, Object>();

	private boolean inRequest = false;

	/**
	 * Starts a request
	 */
	public void startRequest() {
		inRequest = true;
	}

	/**
	 * Stops a request (including properly disposing all the previous request
	 * objects)
	 */
	@SuppressWarnings("unchecked")
	public void stopRequest() {
		inRequest = false;

		for (Map.Entry<ActiveDescriptor<?>, Object> entry : requestScopedEntities.entrySet()) {
			ActiveDescriptor<Object> ad = (ActiveDescriptor<Object>) entry.getKey();
			Object value = entry.getValue();

			ad.dispose(value);
		}

		requestScopedEntities.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glassfish.hk2.api.Context#getScope()
	 */
	@Override
	public Class<? extends Annotation> getScope() {
		return RequestScope.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glassfish.hk2.api.Context#findOrCreate(org.glassfish.hk2.api.
	 * ActiveDescriptor, org.glassfish.hk2.api.ServiceHandle)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <U> U findOrCreate(ActiveDescriptor<U> activeDescriptor, ServiceHandle<?> root) {
		U retVal = (U) requestScopedEntities.get(activeDescriptor);
		if (retVal != null) {
			return retVal;
		}

		retVal = activeDescriptor.create(root);
		requestScopedEntities.put(activeDescriptor, retVal);

		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.glassfish.hk2.api.Context#find(org.glassfish.hk2.api.ActiveDescriptor)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean containsKey(ActiveDescriptor<?> descriptor) {
		return requestScopedEntities.containsKey(descriptor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glassfish.hk2.api.Context#isActive()
	 */
	@Override
	public boolean isActive() {
		return inRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glassfish.hk2.api.Context#supportsNullCreation()
	 */
	@Override
	public boolean supportsNullCreation() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glassfish.hk2.api.Context#supportsNullCreation()
	 */
	@Override
	public void shutdown() {
	}

	@Override
	public void destroyOne(ActiveDescriptor<?> descriptor) {
	}
}
