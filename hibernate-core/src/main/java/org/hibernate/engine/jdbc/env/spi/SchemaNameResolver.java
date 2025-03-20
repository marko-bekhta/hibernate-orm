/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.engine.jdbc.env.spi;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.dialect.Dialect;

/**
 * Contract for resolving the schema of a {@link Connection}.
 *
 * @implNote Since Hibernate baselines on Java 17+, this should no longer be
 * needed as we should be able to rely on {@link Connection#getSchema()}.
 * However, some drivers still do not implement this or implement it "properly".
 *
 * @author Steve Ebersole
 */
public interface SchemaNameResolver {
	/**
	 * Given a JDBC {@link Connection}, resolve the name of the schema (if one) to which it connects.
	 *
	 * @param connection The JDBC connection
	 * @param dialect The {@link Dialect}
	 *
	 * @return The name of the schema; may be null.
	 */
	String resolveSchemaName(Connection connection, Dialect dialect) throws SQLException;
}
