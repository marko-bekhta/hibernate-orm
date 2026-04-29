/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.metamodel.internal;

import java.lang.reflect.Constructor;

import org.hibernate.InstantiationException;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.accessor.HibernateAccessorInstantiator;
import org.hibernate.metamodel.spi.EmbeddableInstantiator;
import org.hibernate.metamodel.spi.ValueAccess;

import static org.hibernate.internal.util.ReflectHelper.getConstructorOrNull;
import static org.hibernate.internal.util.ReflectHelper.getRecordComponentTypes;

/**
 * Support for instantiating embeddables as record representation
 */
public class EmbeddableInstantiatorRecordStandard extends AbstractPojoInstantiator implements EmbeddableInstantiator {

	protected final HibernateAccessorInstantiator<?> instantiator;

	public EmbeddableInstantiatorRecordStandard(HibernateAccessorFactory hibernateAccessorFactory, Class<?> javaType) {
		super( javaType );
		Constructor<?> constructor = getConstructorOrNull( javaType, getRecordComponentTypes( javaType ) );
		this.instantiator = constructor != null ? hibernateAccessorFactory.instantiator( constructor ) : null;
	}

	@Override
	public Object instantiate(ValueAccess valuesAccess) {
		if ( instantiator == null ) {
			throw new InstantiationException( "Unable to locate constructor for embeddable", getMappedPojoClass() );
		}

		try {
			return instantiator.create( valuesAccess.getValues() );
		}
		catch ( Exception e ) {
			throw new InstantiationException( "Could not instantiate entity", getMappedPojoClass(), e );
		}
	}
}
