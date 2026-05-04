/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.boot.registry.classloading.spi.ClassLoadingException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProviderConfigurationException;
import org.hibernate.property.access.spi.HibernateAccessorFactoryResolver;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.service.spi.ServiceRegistryImplementor;

import java.util.Map;

public class HibernateAccessorFactoryResolverInitiator
		implements StandardServiceInitiator<HibernateAccessorFactoryResolver> {

	static final String HIBERNATE_ACCESSOR_FACTORY_RESOLVER = "hibernate.accessor_factory_resolver";

	/**
	 * Singleton access
	 */
	public static final HibernateAccessorFactoryResolverInitiator INSTANCE = new HibernateAccessorFactoryResolverInitiator();

	@Override
	public Class<HibernateAccessorFactoryResolver> getServiceInitiated() {
		return HibernateAccessorFactoryResolver.class;
	}

	@Override
	public HibernateAccessorFactoryResolver initiateService(Map<String, Object> configurationValues, ServiceRegistryImplementor registry) {
		final Object configValue = configurationValues.get( HIBERNATE_ACCESSOR_FACTORY_RESOLVER );
		if ( configValue == null ) {
			return HibernateAccessorFactoryResolverImpl.INSTANCE;
		}
		else if ( configValue instanceof HibernateAccessorFactoryResolver resolver ) {
			return resolver;
		}
		else {
			final var providerClass = providerClass( registry, configValue );
			try {
				return providerClass.getDeclaredConstructor().newInstance();
			}
			catch (Exception e) {
				throw new ServiceException(
						"Unable to instantiate specified Hibernate accessor factory resolver [" + providerClass.getName() + "]",
						e );
			}
		}
	}

	private static Class<? extends HibernateAccessorFactoryResolver> providerClass(
			ServiceRegistryImplementor registry, Object configValue) {
		if ( configValue instanceof Class<?> configType ) {
			if ( !HibernateAccessorFactoryResolver.class.isAssignableFrom( configType ) ) {
				throw new ConnectionProviderConfigurationException( "Class '" + configType.getName()
																	+ "' does not implement 'HibernateAccessorFactoryResolver'" );
			}
			@SuppressWarnings("unchecked") // Safe, we just checked
			final var providerClass = (Class<? extends HibernateAccessorFactoryResolver>) configType;
			return providerClass;
		}
		else {
			final String className = configValue.toString();
			final var classLoaderService = registry.requireService( ClassLoaderService.class );
			try {
				return classLoaderService.classForName( className );
			}
			catch (ClassLoadingException cle) {
				throw new ServiceException( "Unable to locate specified Hibernate accessor factory resolver [" + className + "]", cle );
			}
		}
	}
}
