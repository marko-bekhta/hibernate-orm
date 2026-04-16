/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.HibernateAccessorFactoryResolver;

import java.lang.invoke.MethodHandles;

public class HibernateAccessorFactoryResolverImpl implements HibernateAccessorFactoryResolver {

	public static final HibernateAccessorFactoryResolver INSTANCE = new HibernateAccessorFactoryResolverImpl();

	private HibernateAccessorFactoryResolverImpl() {
	}

	@Override
	public HibernateAccessorFactory resolveHibernateAccessorFactoryResolver(MethodHandles.Lookup lookup) {
		// TODO: switch to HibernateAccessorFactory.lambda(lookup)
		return HibernateAccessorFactory.reflection();
	}
}
