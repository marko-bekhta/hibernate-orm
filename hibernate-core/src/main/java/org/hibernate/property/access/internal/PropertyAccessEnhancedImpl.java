/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import jakarta.persistence.AccessType;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.EnhancedGetterFieldImpl;
import org.hibernate.property.access.spi.EnhancedSetterImpl;
import org.hibernate.property.access.spi.EnhancedSetterMethodImpl;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterFieldImpl;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessBuildingException;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;

import java.lang.reflect.Method;

import static org.hibernate.internal.util.ReflectHelper.findField;
import static org.hibernate.internal.util.ReflectHelper.findSetterMethod;
import static org.hibernate.internal.util.ReflectHelper.getterMethodOrNull;
import static org.hibernate.property.access.internal.AccessStrategyHelper.fieldOrNull;
import static org.hibernate.property.access.internal.AccessStrategyHelper.getAccessType;

/**
 * A {@link PropertyAccess} for byte code enhanced entities. Enhanced setter methods ( if available ) are used for
 * property writes. Regular getter methods/fields are used for property access. Based upon PropertyAccessMixedImpl.
 *
 * @author Steve Ebersole
 * @author Luis Barreiro
 */
public class PropertyAccessEnhancedImpl implements PropertyAccess {
	private final PropertyAccessStrategy strategy;

	private final Getter getter;
	private final Setter setter;

	public PropertyAccessEnhancedImpl(
			PropertyAccessStrategy strategy,
			HibernateAccessorFactory accessorFactory,
			Class<?> containerJavaType,
			String propertyName,
			@Nullable AccessType classAccessType) {
		this.strategy = strategy;

		final var propertyAccessType =
				classAccessType == null
						? getAccessType( containerJavaType, propertyName )
						: classAccessType;

		switch ( propertyAccessType ) {
			case FIELD: {
				final var field = fieldOrNull( containerJavaType, propertyName );
				if ( field == null ) {
					throw new PropertyAccessBuildingException(
							"Could not locate field for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
					);
				}
				getter = new GetterFieldImpl( containerJavaType, propertyName, field, accessorFactory.valueReader( field ) );
				setter = new EnhancedSetterImpl( containerJavaType, propertyName, field, accessorFactory.valueWriter( field ) );
				break;
			}
			case PROPERTY: {
				final var getterMethod = getterMethodOrNull( containerJavaType, propertyName );
				if ( getterMethod == null ) {
					throw new PropertyAccessBuildingException(
							"Could not locate getter for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
					);
				}
				getter = propertyGetter( accessorFactory, classAccessType, containerJavaType, propertyName, getterMethod );
				setter = propertySetter( accessorFactory, classAccessType, containerJavaType, propertyName, getterMethod.getReturnType() );
				break;
			}
			default: {
				throw new PropertyAccessBuildingException(
						"Invalid access type " + propertyAccessType + " for property named [" + containerJavaType.getName() + "#" + propertyName + "]"
				);
			}
		}
	}

	private static Getter propertyGetter(HibernateAccessorFactory accessorFactory, @Nullable AccessType classAccessType, Class<?> containerJavaType, String propertyName, Method getterMethod) {
		if ( classAccessType != null ) {
			final var explicitAccessType = getAccessType( containerJavaType, propertyName );
			if ( explicitAccessType == AccessType.FIELD ) {
				// We need to default to FIELD unless we have an explicit AccessType
				// to avoid unnecessary initializations
				final var field = findField( containerJavaType, propertyName );
				return new EnhancedGetterFieldImpl( containerJavaType, propertyName,
						field, getterMethod, accessorFactory.valueReader( field ) );
			}
		}
		// when classAccessType is null, know PROPERTY is the explicit access type
		return new GetterMethodImpl( containerJavaType, propertyName, getterMethod, accessorFactory.valueReader( getterMethod ) );
	}

	private static Setter propertySetter(HibernateAccessorFactory accessorFactory, @Nullable AccessType classAccessType, Class<?> containerJavaType, String propertyName, Class<?> fieldType) {
		if ( classAccessType != null ) {
			final var explicitAccessType = getAccessType( containerJavaType, propertyName );
			if ( explicitAccessType == AccessType.FIELD ) {
				// We need to default to FIELD unless we have an explicit AccessType
				// to avoid unnecessary initializations
				final var field = findField( containerJavaType, propertyName );
				return new EnhancedSetterImpl( containerJavaType, propertyName,
						field, accessorFactory.valueWriter( field ) );
			}
		}
		// when classAccessType is null, know PROPERTY is the explicit access type
		final var setterMethod = findSetterMethod( containerJavaType, propertyName, fieldType );
		return new EnhancedSetterMethodImpl( containerJavaType, propertyName,
				setterMethod, accessorFactory.valueWriter( setterMethod ) );
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
	public Setter getSetter() {
		return setter;
	}
}
