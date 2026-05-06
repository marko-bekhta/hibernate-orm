/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterFieldImpl;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessBuildingException;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.hibernate.internal.util.ReflectHelper.getterMethodOrNull;
import static org.hibernate.property.access.internal.AccessStrategyHelper.fieldOrNull;
import static org.hibernate.property.access.internal.AccessStrategyHelper.getAccessType;

/**
 * A {@link PropertyAccess} based on mix of getter method or field.
 *
 * @author Gavin King
 */
public class PropertyAccessGetterImpl implements PropertyAccess {
	private final PropertyAccessStrategy strategy;

	private final Getter getter;

	public PropertyAccessGetterImpl(PropertyAccessStrategy strategy, HibernateAccessorFactory accessorFactory, Class<?> containerJavaType, String propertyName) {
		this.strategy = strategy;

		final var propertyAccessType = getAccessType( containerJavaType, propertyName );
		switch ( propertyAccessType ) {
			case FIELD: {
				final var field = fieldOrNull( containerJavaType, propertyName );
				if ( field == null ) {
					throw new PropertyAccessBuildingException(
							"Could not locate field for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
					);
				}
				getter = fieldGetter( accessorFactory, containerJavaType, propertyName, field );
				break;
			}
			case PROPERTY: {
				final var getterMethod = getterMethodOrNull( containerJavaType, propertyName );
				if ( getterMethod == null ) {
					throw new PropertyAccessBuildingException(
							"Could not locate getter for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
					);
				}
				getter = propertyGetter( accessorFactory, containerJavaType, propertyName, getterMethod );
				break;
			}
			default: {
				throw new PropertyAccessBuildingException(
						"Invalid access type " + propertyAccessType + " for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
				);
			}
		}
	}

	// --- //

	private static Getter fieldGetter(HibernateAccessorFactory accessorFactory, Class<?> containerJavaType, String propertyName, Field field) {
		return new GetterFieldImpl( containerJavaType, propertyName, field, accessorFactory.valueReader( field ) );
	}

	private static Getter propertyGetter(HibernateAccessorFactory accessorFactory, Class<?> containerJavaType, String propertyName, Method method) {
		return new GetterMethodImpl( containerJavaType, propertyName, method, accessorFactory.valueReader( method ) );
	}

	@Override
	public PropertyAccessStrategy getPropertyAccessStrategy() {
		return strategy;
	}

	@Override
	public Getter getGetter() {
		return getter;
	}

	@Override
	public @Nullable Setter getSetter() {
		return null;
	}
}
