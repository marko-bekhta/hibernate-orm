/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import org.hibernate.accessor.HibernateAccessorFactory;

public class HibernateAccessorFactoryResolverRegistry {
	private static volatile HibernateAccessorFactory factory;

	public static void register(HibernateAccessorFactory accessorFactory) {
		factory = accessorFactory;
	}

	public static HibernateAccessorFactory resolveHibernateAccessorFactory() {
		return factory;
	}
}
