/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;

import jakarta.persistence.AccessType;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Defines a strategy for accessing property values via a get/set pair, which may be nonpublic.  This
 * is the default (and recommended) strategy.
 *
 * @author Steve Ebersole
 * @author Gavin King
 */
public class PropertyAccessStrategyEnhancedImpl implements PropertyAccessStrategy {

	private final @Nullable AccessType classAccessType;

	public PropertyAccessStrategyEnhancedImpl(@Nullable AccessType classAccessType) {
		this.classAccessType = classAccessType;
	}

	@Override
	public PropertyAccess buildPropertyAccess(Class<?> containerJavaType, final String propertyName, boolean setterRequired) {
		return buildPropertyAccess( containerJavaType, propertyName, setterRequired, HibernateAccessorFactory.reflection() );
	}

	@Override
	public PropertyAccess buildPropertyAccess(Class<?> containerJavaType, String propertyName, boolean setterRequired,
			HibernateAccessorFactory accessorFactory) {
		return new PropertyAccessEnhancedImpl( this, accessorFactory, containerJavaType, propertyName, classAccessType );
	}
}
