/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import java.util.Map;

import org.hibernate.boot.registry.StandardServiceInitiator;
import org.hibernate.property.access.spi.PropertyAccessorService;
import org.hibernate.service.spi.ServiceRegistryImplementor;

/**
 * @author Marko Bekhta
 */
public class PropertyAccessorServiceInitiator implements StandardServiceInitiator<PropertyAccessorService> {

	public static final PropertyAccessorServiceInitiator INSTANCE = new PropertyAccessorServiceInitiator();

	@Override
	public Class<PropertyAccessorService> getServiceInitiated() {
		return PropertyAccessorService.class;
	}

	@Override
	public PropertyAccessorService initiateService(Map<String, Object> configurationValues, ServiceRegistryImplementor registry) {
		return PropertyAccessorServiceImpl.INSTANCE;
	}
}
