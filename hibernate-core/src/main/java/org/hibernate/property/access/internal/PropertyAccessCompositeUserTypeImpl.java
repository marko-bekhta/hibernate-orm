/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;

import jakarta.annotation.Nullable;

/**
 * {@link PropertyAccess} for accessing the wrapped property via get/set pair, which may be nonpublic.
 *
 * @author Steve Ebersole
 *
 * @see PropertyAccessStrategyBasicImpl
 */
public class PropertyAccessCompositeUserTypeImpl implements PropertyAccess, Getter {

	private final PropertyAccessStrategyCompositeUserTypeImpl strategy;
	private final int propertyIndex;

	public PropertyAccessCompositeUserTypeImpl(PropertyAccessStrategyCompositeUserTypeImpl strategy, String property) {
		this.strategy = strategy;
		this.propertyIndex = strategy.sortedPropertyNames.indexOf( property );
	}

	@Override
	public PropertyAccessStrategy getPropertyAccessStrategy() {
		return strategy;
	}

	@Override
	public Getter getGetter() {
		return this;
	}

	@Override
	public @Nullable Setter getSetter() {
		return null;
	}

	@Override
	public @Nullable Object get(Object owner) {
		return strategy.compositeUserType.getPropertyValue( owner, propertyIndex );
	}

	@Override
	public Class<?> getReturnTypeClass() {
		return ReflectHelper.getClass( strategy.sortedPropertyTypes.get(propertyIndex) );
	}

	@Override
	public @Nullable Member getMember() {
		return null;
	}

	@Override
	public @Nullable Method getMethod() {
		return null;
	}
}
