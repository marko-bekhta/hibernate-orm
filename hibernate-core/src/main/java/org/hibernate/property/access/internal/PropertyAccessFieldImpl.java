/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;


import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterFieldImpl;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.property.access.spi.SetterFieldImpl;

import java.lang.reflect.Modifier;

import static org.hibernate.internal.util.ReflectHelper.findField;

/**
 * @author Steve Ebersole
 * @author Gavin King
 */
public class PropertyAccessFieldImpl implements PropertyAccess {
	private final PropertyAccessStrategyFieldImpl strategy;
	private final Getter getter;
	private final Setter setter;

	public PropertyAccessFieldImpl(
			PropertyAccessStrategyFieldImpl strategy,
			HibernateAccessorFactory accessorFactory,
			Class<?> containerJavaType,
			final String propertyName) {
		this.strategy = strategy;

		final var field = findField( containerJavaType, propertyName );
		getter = new GetterFieldImpl( accessorFactory, containerJavaType, propertyName, field );
		if ( !Modifier.isFinal( field.getModifiers() ) ) {
			setter = new SetterFieldImpl( accessorFactory, containerJavaType, propertyName, field );
		}
		else {
			setter = null;
		}
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
