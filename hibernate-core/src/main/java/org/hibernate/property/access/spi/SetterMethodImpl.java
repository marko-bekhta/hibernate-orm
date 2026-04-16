/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Method;

import org.hibernate.Internal;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertySetterAccessException;
import org.hibernate.accessor.HibernateAccessorException;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.accessor.HibernateAccessorValueWriter;
import org.hibernate.property.access.internal.AbstractSetterMethodSerialForm;

import org.checkerframework.checker.nullness.qual.Nullable;

import static org.hibernate.internal.CoreMessageLogger.CORE_LOGGER;

/**
 * @author Steve Ebersole
 */
@Internal
public class SetterMethodImpl implements Setter {

	private final HibernateAccessorFactory accessorFactory;
	private final Class<?> containerClass;
	private final String propertyName;
	private final Method setterMethod;
	private final HibernateAccessorValueWriter writer;

	private final boolean isPrimitive;

	public SetterMethodImpl(HibernateAccessorFactory accessorFactory, Class<?> containerClass, String propertyName, Method setterMethod) {
		this.accessorFactory = accessorFactory;
		this.containerClass = containerClass;
		this.propertyName = propertyName;
		this.setterMethod = setterMethod;
		this.writer = this.accessorFactory.valueWriter( setterMethod );
		this.isPrimitive = setterMethod.getParameterTypes()[0].isPrimitive();
	}

	@Override
	public void set(Object target, @Nullable Object value) {
		try {
			writer.set( target, value );
		}
		catch (NullPointerException npe) {
			if ( value == null && isPrimitive ) {
				throw new PropertyAccessException(
						npe,
						"Null value was assigned to a property of primitive type",
						true,
						containerClass,
						propertyName
				);
			}
			else {
				throw new PropertyAccessException(
						npe,
						"NullPointerException occurred while calling",
						true,
						containerClass,
						propertyName
				);
			}
		}
		catch (HibernateAccessorException ae) {
			final var cause = ae.getCause();
			if ( cause instanceof Error error ) {
				// HHH-16403 Don't wrap Error
				throw error;
			}
			throw new PropertyAccessException(
					cause,
					"Exception occurred inside",
					true,
					containerClass,
					propertyName
			);
		}
		catch (IllegalArgumentException iae) {
			if ( value == null && isPrimitive ) {
				throw new PropertyAccessException(
						iae,
						"Null value was assigned to a property of primitive type",
						true,
						containerClass,
						propertyName
				);
			}
			else {
				final var expectedType = setterMethod.getParameterTypes()[0];
				CORE_LOGGER.illegalPropertySetterArgument( containerClass.getName(), propertyName );
				CORE_LOGGER.expectedType( expectedType.getName(), value == null ? null : value.getClass().getName() );
				throw new PropertySetterAccessException(
						iae,
						containerClass,
						propertyName,
						expectedType,
						target,
						value
				);
			}
		}
	}

	public Class<?> getContainerClass() {
		return containerClass;
	}

	@Override
	public String getMethodName() {
		return setterMethod.getName();
	}

	@Override
	public Method getMethod() {
		return setterMethod;
	}

	@Serial
	private Object writeReplace() {
		return new SerialForm( accessorFactory, containerClass, propertyName, setterMethod );
	}

	public HibernateAccessorFactory getAccessorFactory() {
		return accessorFactory;
	}

	private static class SerialForm extends AbstractSetterMethodSerialForm implements Serializable {
		private SerialForm(HibernateAccessorFactory accessorFactory, Class<?> containerClass, String propertyName, Method method) {
			super( accessorFactory, containerClass, propertyName, method );
		}

		@Serial
		private Object readResolve() {
			return new SetterMethodImpl( getAccessorFactory(), getContainerClass(), getPropertyName(), resolveMethod() );
		}
	}
}
