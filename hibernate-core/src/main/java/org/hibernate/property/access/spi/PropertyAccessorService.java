/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import org.hibernate.models.accessor.HibernateAccessorFactory;
import org.hibernate.service.Service;

/**
 * Service providing access to the {@link HibernateAccessorFactory}.
 *
 * @author Marko Bekhta
 */
public interface PropertyAccessorService extends Service {
	HibernateAccessorFactory hibernateAccessorFactory();
}
