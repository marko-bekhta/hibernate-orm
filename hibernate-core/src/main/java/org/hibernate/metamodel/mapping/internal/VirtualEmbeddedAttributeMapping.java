/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.metamodel.mapping.internal;

import jakarta.annotation.Nullable;
import org.hibernate.engine.FetchStyle;
import org.hibernate.engine.FetchTiming;
import org.hibernate.metamodel.mapping.AttributeMetadata;
import org.hibernate.metamodel.mapping.EmbeddableMappingType;
import org.hibernate.metamodel.mapping.EmbeddableValuedModelPart;
import org.hibernate.metamodel.mapping.ManagedMappingType;
import org.hibernate.metamodel.mapping.SelectableMappings;
import org.hibernate.metamodel.mapping.VirtualModelPart;
import org.hibernate.metamodel.model.domain.NavigableRole;
import org.hibernate.models.spi.MemberDetails;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.sql.ast.tree.from.TableGroupProducer;

/**
 * @author Christian Beikov
 */
public class VirtualEmbeddedAttributeMapping extends EmbeddedAttributeMapping implements VirtualModelPart {

	public VirtualEmbeddedAttributeMapping(
			String name,
			NavigableRole navigableRole,
			int stateArrayPosition,
			int fetchableIndex,
			String tableExpression,
			AttributeMetadata attributeMetadata,
			@Nullable MemberDetails parentMemberDetails,
			FetchTiming mappedFetchTiming,
			FetchStyle mappedFetchStyle,
			EmbeddableMappingType embeddableMappingType,
			ManagedMappingType declaringType,
			PropertyAccess propertyAccess) {
		super(
				name,
				navigableRole,
				stateArrayPosition,
				fetchableIndex,
				tableExpression,
				attributeMetadata,
				parentMemberDetails,
				mappedFetchTiming,
				mappedFetchStyle,
				embeddableMappingType,
				declaringType,
				propertyAccess
		);
	}

	// Constructor is only used for creating the inverse attribute mapping
	VirtualEmbeddedAttributeMapping(
			ManagedMappingType keyDeclaringType,
			TableGroupProducer declaringTableGroupProducer,
			SelectableMappings selectableMappings,
			EmbeddableValuedModelPart inverseModelPart,
			EmbeddableMappingType embeddableTypeDescriptor,
			MappingModelCreationProcess creationProcess) {
		super(
				keyDeclaringType,
				declaringTableGroupProducer,
				selectableMappings,
				inverseModelPart,
				embeddableTypeDescriptor,
				creationProcess
		);
	}

}
