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
		// TODO: Switch to HibernateAccessorFactory.lambda(lookup)
		// TODO: If we go with lambda ... where do we get our hands on lookup ???
		//  Ideally we want that lookup to be passed to us by a user...
		return HibernateAccessorFactory.reflection();
	}
}
