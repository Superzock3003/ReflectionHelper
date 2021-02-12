package org.inventivetalent.reflection.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Helper class to set fields, methods &amp; constructors accessible
 */
public abstract class AccessUtil {

	/**
	 * Sets the field accessible and removes final modifiers
	 *
	 * @param field Field to set accessible
	 * @return the Field
	 * @throws ReflectiveOperationException (usually never)
	 */

	public static Field setAccessible(Field field) throws ReflectiveOperationException {
		return setAccessible(field, false);
	}

	public static Field setAccessible(Field field, boolean readOnly) throws ReflectiveOperationException {
		field.setAccessible(true);
		if (readOnly) {
			return field;
		}
		int modifiers = field.getModifiers();
		try {
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, modifiers & ~Modifier.FINAL);
		} catch (NoSuchFieldException e) {
			if ("modifiers".equals(e.getMessage()) || (e.getCause() != null && e.getCause().getMessage() != null &&  e.getCause().getMessage().equals("modifiers"))) {
				// https://github.com/ViaVersion/ViaVersion/blob/e07c994ddc50e00b53b728d08ab044e66c35c30f/bungee/src/main/java/us/myles/ViaVersion/bungee/platform/BungeeViaInjector.java
				// Java 12 compatibility *this is fine*
				Method getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
				getDeclaredFields0.setAccessible(true);
				Field[] fields = (Field[]) getDeclaredFields0.invoke(Field.class, false);
				for (Field classField : fields) {
					if ("modifiers".equals(classField.getName())) {
						classField.setAccessible(true);
						classField.set(field, modifiers & ~Modifier.FINAL);
						break;
					}
				}
			} else {
				throw e;
			}
		}
		return field;
	}

	/**
	 * Sets the method accessible
	 *
	 * @param method Method to set accessible
	 * @return the Method
	 * @throws ReflectiveOperationException (usually never)
	 */
	public static Method setAccessible(Method method) throws ReflectiveOperationException {
		method.setAccessible(true);
		return method;
	}

	/**
	 * Sets the constructor accessible
	 *
	 * @param constructor Constructor to set accessible
	 * @return the Constructor
	 * @throws ReflectiveOperationException (usually never)
	 */
	public static Constructor setAccessible(Constructor constructor) throws ReflectiveOperationException {
		constructor.setAccessible(true);
		return constructor;
	}

}
