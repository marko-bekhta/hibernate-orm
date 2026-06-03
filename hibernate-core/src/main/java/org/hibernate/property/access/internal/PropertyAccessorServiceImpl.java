/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.models.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.PropertyAccessorService;

/**
 * @author Marko Bekhta
 */
public class PropertyAccessorServiceImpl implements PropertyAccessorService {

	public static final PropertyAccessorService INSTANCE = new PropertyAccessorServiceImpl();

	private PropertyAccessorServiceImpl() {
	}

	@Override
	public HibernateAccessorFactory hibernateAccessorFactory() {
		return HibernateAccessorFactory.reflection();
	}
}
