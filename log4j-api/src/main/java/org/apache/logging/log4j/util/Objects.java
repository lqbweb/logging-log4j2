package org.apache.logging.log4j.util;

/**
 * Created by Yo on 17/08/2016.
 */
public class Objects {
	public static <T> T requireNonNull(T obj) {
		if (obj == null)
			throw new NullPointerException();
		return obj;
	}

	public static <T> T requireNonNull(T obj, String message) {
		if (obj == null)
			throw new NullPointerException(message);
		return obj;
	}

	/**
	 * Returns {@code true} if the arguments are equal to each other
	 * and {@code false} otherwise.
	 * Consequently, if both arguments are {@code null}, {@code true}
	 * is returned and if exactly one argument is {@code null}, {@code
	 * false} is returned.  Otherwise, equality is determined by using
	 * the {@link Object#equals equals} method of the first
	 * argument.
	 *
	 * @param a an object
	 * @param b an object to be compared with {@code a} for equality
	 * @return {@code true} if the arguments are equal to each other
	 * and {@code false} otherwise
	 * @see Object#equals(Object)
	 */
	public static boolean equals(Object a, Object b) {
		return (a == b) || (a != null && a.equals(b));
	}

	public static int hashCode(Object o) {
		return o != null ? o.hashCode() : 0;
	}
}
