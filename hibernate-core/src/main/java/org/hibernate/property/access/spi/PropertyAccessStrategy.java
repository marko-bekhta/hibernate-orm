/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import org.hibernate.accessor.HibernateAccessorFactory;

/**
 * Describes a strategy for accessing a persistent attribute,
 * for example: field, JavaBean-style property, or whatever.
 * <p>
 * Acts as a factory for {@link PropertyAccess} instances.
 */
public interface PropertyAccessStrategy {
	/**
	 * Build a {@link PropertyAccess} for the indicated property
	 *
	 * @param containerJavaType The Java type that contains the property; may be {@code null} for non-pojo cases.
	 * @param propertyName The property name
	 * @param setterRequired Whether it is an error if we are unable to find a corresponding setter
	 *
	 * @return The appropriate PropertyAccess
	 */
	PropertyAccess buildPropertyAccess(Class<?> containerJavaType, String propertyName, boolean setterRequired);

	/**
	 * Build a {@link PropertyAccess} for the indicated property using the given {@link HibernateAccessorFactory}
	 * for field/method access.
	 *
	 * @param containerJavaType The Java type that contains the property; may be {@code null} for non-pojo cases.
	 * @param propertyName The property name
	 * @param setterRequired Whether it is an error if we are unable to find a corresponding setter
	 * @param accessorFactory The factory to use for creating field/method accessors
	 *
	 * @return The appropriate PropertyAccess
	 */
	default PropertyAccess buildPropertyAccess(Class<?> containerJavaType, String propertyName, boolean setterRequired,
			HibernateAccessorFactory accessorFactory) {
		return buildPropertyAccess( containerJavaType, propertyName, setterRequired );
	}
}
