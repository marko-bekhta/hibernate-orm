/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.HibernateAccessorFactoryResolver;


public class HibernateAccessorFactoryResolverImpl implements HibernateAccessorFactoryResolver {

	public static final HibernateAccessorFactoryResolver INSTANCE = new HibernateAccessorFactoryResolverImpl();

	private final HibernateAccessorFactory accessorFactory;

	private HibernateAccessorFactoryResolverImpl() {
		// TODO: Switch to HibernateAccessorFactory.lambda(lookup)
		// TODO: If we go with lambda ... where do we get our hands on lookup ???
		//  Ideally we want that lookup to be passed to us by a user...
		this.accessorFactory = HibernateAccessorFactory.reflection();
	}

	@Override
	public HibernateAccessorFactory resolveHibernateAccessorFactoryResolver() {
		return accessorFactory;
	}
}
