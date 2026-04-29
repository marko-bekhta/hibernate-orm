/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.metamodel.internal;

import java.lang.reflect.Constructor;

import org.hibernate.InstantiationException;
import org.hibernate.accessor.HibernateAccessorInstantiator;
import org.hibernate.metamodel.spi.EmbeddableInstantiator;
import org.hibernate.metamodel.spi.ValueAccess;

/**
 * Support for instantiating embeddables as POJO representation through a constructor
 */
public class EmbeddableInstantiatorPojoIndirecting
		extends AbstractPojoInstantiator
		implements EmbeddableInstantiator {
	protected final Constructor<?> constructor;
	protected final HibernateAccessorInstantiator<?> instantiator;
	protected final int[] index;

	protected EmbeddableInstantiatorPojoIndirecting(Constructor<?> constructor, HibernateAccessorInstantiator<?> instantiator, int[] index) {
		super( constructor.getDeclaringClass() );
		this.constructor = constructor;
		this.instantiator = instantiator;
		this.index = index;
	}

	@Override
	public Object instantiate(ValueAccess valuesAccess) {
		try {
			final var originalValues = valuesAccess.getValues();
			final var values = new Object[originalValues.length];
			for ( int i = 0; i < values.length; i++ ) {
				values[i] = originalValues[index[i]];
			}
			return instantiator.create( values );
		}
		catch ( Exception e ) {
			throw new InstantiationException( "Could not instantiate entity", getMappedPojoClass(), e );
		}
	}

	public static EmbeddableInstantiatorPojoIndirecting of(
			String[] propertyNames,
			Constructor<?> constructor,
			HibernateAccessorInstantiator<?> instantiator,
			String[] componentNames) {
		if ( componentNames == null ) {
			throw new IllegalArgumentException( "Can't determine field assignment for constructor: " + constructor );
		}
		final var index = new int[componentNames.length];
		return EmbeddableHelper.resolveIndex( propertyNames, componentNames, index )
				? new EmbeddableInstantiatorPojoIndirectingWithGap( constructor, instantiator, index )
				: new EmbeddableInstantiatorPojoIndirecting( constructor, instantiator, index );
	}

	// Handles gaps, by leaving the value null for that index
	private static class EmbeddableInstantiatorPojoIndirectingWithGap extends EmbeddableInstantiatorPojoIndirecting {

		public EmbeddableInstantiatorPojoIndirectingWithGap(Constructor<?> constructor,HibernateAccessorInstantiator<?> instantiator, int[] index) {
			super( constructor, instantiator, index );
		}

		@Override
		public Object instantiate(ValueAccess valuesAccess) {
			try {
				final var originalValues = valuesAccess.getValues();
				final var values = new Object[index.length];
				for ( int i = 0; i < values.length; i++ ) {
					final int index = this.index[i];
					if ( index >= 0 ) {
						values[i] = originalValues[index];
					}
				}
				return instantiator.create( values );
			}
			catch ( Exception e ) {
				throw new InstantiationException( "Could not instantiate entity", getMappedPojoClass(), e );
			}
		}
	}
}
