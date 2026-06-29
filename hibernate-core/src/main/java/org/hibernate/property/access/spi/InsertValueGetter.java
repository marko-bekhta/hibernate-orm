/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.spi;

import java.util.Map;

import org.hibernate.engine.spi.SharedSessionContractImplementor;

import jakarta.annotation.Nullable;

public class InsertValueGetter {

	private enum Mode {
		STANDARD,
		BACK_REF,
		INDEX_BACK_REF
	}

	private final Mode mode;
	private final @Nullable Getter getter;
	private final @Nullable String entityName;
	private final @Nullable String propertyName;

	private InsertValueGetter(Mode mode, @Nullable Getter getter, @Nullable String entityName, @Nullable String propertyName) {
		this.mode = mode;
		this.getter = getter;
		this.entityName = entityName;
		this.propertyName = propertyName;
	}

	public static InsertValueGetter standard(Getter getter) {
		return new InsertValueGetter( Mode.STANDARD, getter, null, null );
	}

	public static InsertValueGetter backRef(String entityName, String propertyName) {
		return new InsertValueGetter( Mode.BACK_REF, null, entityName, propertyName );
	}

	public static InsertValueGetter indexBackRef(String entityName, String propertyName) {
		return new InsertValueGetter( Mode.INDEX_BACK_REF, null, entityName, propertyName );
	}

	public @Nullable Object getValue(Object owner, Map<Object, Object> mergeMap, SharedSessionContractImplementor session) {
		return switch ( mode ) {
			case STANDARD -> getter.get( owner );
			case BACK_REF -> session.getPersistenceContextInternal()
					.getOwnerId( entityName, propertyName, owner, mergeMap );
			case INDEX_BACK_REF -> session.getPersistenceContextInternal()
					.getIndexInOwner( entityName, propertyName, owner, mergeMap );
		};
	}
}
