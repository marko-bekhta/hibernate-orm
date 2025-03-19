/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.engine.jdbc.env.internal;

import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.SchemaNameResolver;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Default implementation of {@link SchemaNameResolver}.
 *
 * @deprecated Since Hibernate now baselines on Java 17,
 * {@link Connection#getSchema()} is always available directly.
 *
 * @author Steve Ebersole
 */
@Deprecated
public class DefaultSchemaNameResolver implements SchemaNameResolver {
	public static final DefaultSchemaNameResolver INSTANCE = new DefaultSchemaNameResolver();

	private DefaultSchemaNameResolver() {
	}

	@Override
	public String resolveSchemaName(Connection connection, Dialect dialect) throws SQLException {
		return connection.getSchema();
	}

}
