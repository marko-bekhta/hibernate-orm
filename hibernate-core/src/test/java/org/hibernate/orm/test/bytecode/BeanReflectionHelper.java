/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.bytecode;
import java.util.Date;

/**
 * @author Steve Ebersole
 */
public class BeanReflectionHelper {

	public static final Object[] TEST_VALUES = new Object[] {
			"hello", new Long(1), new Integer(1), new Date(), new Long(1), new Integer(1), new Object()
	};

	private static final String[] getterNames = {
			"getSomeString", "getSomeLong", "getSomeInteger", "getSomeDate",
			"getSomelong", "getSomeint", "getSomeObject"
	};

	private static final String[] setterNames = {
			"setSomeString", "setSomeLong", "setSomeInteger", "setSomeDate",
			"setSomelong", "setSomeint", "setSomeObject"
	};

	private static final Class<?>[] types = {
			String.class, Long.class, Integer.class, Date.class,
			long.class, int.class, Object.class
	};

	public static String[] getGetterNames() {
		return getterNames;
	}

	public static String[] getSetterNames() {
		return setterNames;
	}

	public static Class<?>[] getTypes() {
		return types;
	}
}
