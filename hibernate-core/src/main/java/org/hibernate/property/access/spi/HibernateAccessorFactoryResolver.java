/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.service.Service;

import java.lang.invoke.MethodHandles;

public interface HibernateAccessorFactoryResolver extends Service {

	HibernateAccessorFactory resolveHibernateAccessorFactoryResolver(MethodHandles.Lookup lookup);
}
