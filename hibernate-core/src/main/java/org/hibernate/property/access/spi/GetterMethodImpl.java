/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import org.hibernate.Internal;
import org.hibernate.PropertyAccessException;
import org.hibernate.internal.util.collections.ArrayHelper;

import jakarta.annotation.Nullable;

import static org.hibernate.internal.CoreMessageLogger.CORE_LOGGER;

/**
 * @author Steve Ebersole
 */
@Internal
public class GetterMethodImpl implements Getter {

	private final Class<?> containerClass;
	private final String propertyName;
	private final Method getterMethod;

	public GetterMethodImpl(Class<?> containerClass, String propertyName, Method getterMethod) {
		this.containerClass = containerClass;
		this.propertyName = propertyName;
		this.getterMethod = getterMethod;
	}

	@Override
	public @Nullable Object get(Object owner) {
		try {
			return getterMethod.invoke( owner, ArrayHelper.EMPTY_OBJECT_ARRAY );
		}
		catch (InvocationTargetException ite) {
			final var cause = ite.getCause();
			if ( cause instanceof Error error ) {
				// HHH-16403 Don't wrap Error
				throw error;
			}
			throw new PropertyAccessException(
					cause,
					"Exception occurred inside",
					false,
					containerClass,
					propertyName
			);
		}
		catch (IllegalAccessException iae) {
			throw new PropertyAccessException(
					iae,
					"IllegalAccessException occurred while calling",
					false,
					containerClass,
					propertyName
			);
			//cannot occur
		}
		catch (IllegalArgumentException iae) {
			CORE_LOGGER.illegalPropertyGetterArgument( containerClass.getName(), propertyName );
			throw new PropertyAccessException(
					iae,
					"IllegalArgumentException occurred calling",
					false,
					containerClass,
					propertyName
			);
		}
	}

	@Override
	public Class<?> getReturnTypeClass() {
		return getterMethod.getReturnType();
	}

	@Override
	public Member getMember() {
		return getterMethod;
	}

	@Override
	public Method getMethod() {
		return getterMethod;
	}

}
