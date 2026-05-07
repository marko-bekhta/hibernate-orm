/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

import org.hibernate.Internal;
import org.hibernate.PropertyAccessException;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.accessor.HibernateAccessorValueWriter;
import org.hibernate.property.access.internal.AbstractFieldSerialForm;

import org.checkerframework.checker.nullness.qual.Nullable;

import static org.hibernate.internal.util.ReflectHelper.setterMethodOrNull;
import static org.hibernate.proxy.HibernateProxy.extractLazyInitializer;

/**
 * Field-based implementation of Setter
 *
 * @author Steve Ebersole
 */
@Internal
public class SetterFieldImpl implements Setter {
	private final HibernateAccessorFactory accessorFactory;
	private final Class<?> containerClass;
	private final String propertyName;
	private final Field field;
	private final HibernateAccessorValueWriter writer;
	private final @Nullable Method setterMethod;

	public SetterFieldImpl(HibernateAccessorFactory accessorFactory, Class<?> containerClass, String propertyName, Field field) {
		this.accessorFactory = accessorFactory;
		this.containerClass = containerClass;
		this.propertyName = propertyName;
		this.field = field;
		this.writer = writerOrStub( accessorFactory, field );
		this.setterMethod = setterMethodOrNull( containerClass, propertyName, field.getType() );
	}

	private static HibernateAccessorValueWriter writerOrStub(HibernateAccessorFactory accessorFactory, Field field) {
		if ( field.getDeclaringClass().isRecord() ) {
			return ThrowningHibernateAccessorValueWriter.INSTANCE;
		}
		return accessorFactory.valueWriter( field );
	}

	public Class<?> getContainerClass() {
		return containerClass;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Field getField() {
		return field;
	}

	@Override
	public void set(Object target, @Nullable Object value) {
		try {
			writer.set( target, value );
		}
		catch (NullPointerException e) {
			throw new PropertyAccessException( e, "Setting a field value of a record class is not allwed!",
					true,
					containerClass,
					propertyName );
		}
		catch (Exception e) {
			if ( value == null && field.getType().isPrimitive() ) {
				throw new PropertyAccessException(
						e,
						String.format(
								Locale.ROOT,
								"Null value was assigned to a property [%s.%s] of primitive type",
								containerClass,
								propertyName
						),
						true,
						containerClass,
						propertyName
				);
			}
			else {
				throw new PropertyAccessException(
						e,
						String.format(
								Locale.ROOT,
								"Could not set value of type [%s]",
								typeName( value )
						),
						true,
						containerClass,
						propertyName
				);
			}
		}
	}

	private static String typeName(@Nullable Object value) {
		final var lazyInitializer = extractLazyInitializer( value );
		if ( lazyInitializer != null ) {
			return lazyInitializer.getEntityName();
		}
		else if ( value != null ) {
			return value.getClass().getTypeName();
		}
		else {
			return "<unknown>";
		}
	}

	@Override
	public @Nullable String getMethodName() {
		return setterMethod != null ? setterMethod.getName() : null;
	}

	@Override
	public @Nullable Method getMethod() {
		return setterMethod;
	}

	@Serial
	private Object writeReplace() {
		return new SerialForm( accessorFactory, containerClass, propertyName, field );
	}

	public HibernateAccessorFactory getAccessorFactory() {
		return accessorFactory;
	}

	private static class SerialForm extends AbstractFieldSerialForm implements Serializable {
		private final Class<?> containerClass;
		private final String propertyName;


		private SerialForm(HibernateAccessorFactory accessorFactory, Class<?> containerClass, String propertyName, Field field) {
			super( accessorFactory, field );
			this.containerClass = containerClass;
			this.propertyName = propertyName;
		}

		@Serial
		private Object readResolve() {
			return new SetterFieldImpl( getAccessorFactory(), containerClass, propertyName, resolveField() );
		}

	}

	private static final class ThrowningHibernateAccessorValueWriter implements HibernateAccessorValueWriter {

		private static final HibernateAccessorValueWriter INSTANCE = new ThrowningHibernateAccessorValueWriter();

		@Override
		public void set(Object o, Object o1) {
			var container = (o != null ? o.getClass() : null);
			throw new PropertyAccessException( null, "Setter cannot be called on a Record type: " + container, true,
					container, null );
		}
	}
}
