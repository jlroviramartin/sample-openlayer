package es.test.resources;

import java.util.*;

public class IterableEnumeration<T> implements Iterable<T> {
	private final Enumeration<T> enumerator;

	public IterableEnumeration(Enumeration<T> enumerator) {
		this.enumerator = enumerator;
	}

	// return an adaptor for the Enumeration
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return enumerator.hasMoreElements();
			}

			public T next() {
				return enumerator.nextElement();
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public static <T> Iterable<T> make(Enumeration<T> en) {
		return new IterableEnumeration<T>(en);
	}
}