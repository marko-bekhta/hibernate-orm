/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.GetterMethodImpl;
import org.hibernate.property.access.spi.PropertyAccess;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.property.access.spi.SetterMethodImpl;


import org.checkerframework.checker.nullness.qual.Nullable;

import static org.hibernate.internal.util.ReflectHelper.findGetterMethod;
import static org.hibernate.internal.util.ReflectHelper.findSetterMethod;
import static org.hibernate.internal.util.ReflectHelper.setterMethodOrNull;

/**
 * {@link PropertyAccess} for accessing the wrapped property via get/set pair, which may be nonpublic.
 *
 * @author Steve Ebersole
 *
 * @see PropertyAccessStrategyBasicImpl
 */
public class PropertyAccessBasicImpl implements PropertyAccess {

	private final PropertyAccessStrategyBasicImpl strategy;
	private final GetterMethodImpl getter;
	private final @Nullable SetterMethodImpl setter;


	/**
	 * Caused by: jakarta.persistence.PersistenceException: [PersistenceUnit: <default>] Unable to build Hibernate SessionFactory
	 * 	at io.quarkus.hibernate.orm.runtime.boot.FastBootEntityManagerFactoryBuilder.persistenceException(FastBootEntityManagerFactoryBuilder.java:146)
	 * 	at io.quarkus.hibernate.orm.runtime.boot.FastBootEntityManagerFactoryBuilder.build(FastBootEntityManagerFactoryBuilder.java:103)
	 * 	at io.quarkus.hibernate.orm.runtime.FastBootHibernatePersistenceProvider.createEntityManagerFactory(FastBootHibernatePersistenceProvider.java:90)
	 * 	at jakarta.persistence.Persistence.createEntityManagerFactory(Persistence.java:90)
	 * 	at io.quarkus.hibernate.orm.runtime.JPAConfig$LazyPersistenceUnit.get(JPAConfig.java:178)
	 * 	at io.quarkus.hibernate.orm.runtime.JPAConfig$1.run(JPAConfig.java:65)
	 * 	at java.base/java.lang.Thread.run(Thread.java:1583)
	 * Caused by: java.lang.UnsupportedOperationException
	 * 	at io.quarkus.hibernate.accessor.runtime.QuarkusHibernateAccessorFactory.valueWriter(Unknown Source)
	 * 	at org.hibernate.property.access.spi.SetterFieldImpl.<init>(SetterFieldImpl.java:44)
	 * 	at org.hibernate.property.access.spi.EnhancedSetterImpl.<init>(EnhancedSetterImpl.java:35)
	 * 	at org.hibernate.property.access.internal.PropertyAccessEnhancedImpl.<init>(PropertyAccessEnhancedImpl.java:64)
	 * 	at org.hibernate.property.access.internal.PropertyAccessStrategyEnhancedImpl.buildPropertyAccess(PropertyAccessStrategyEnhancedImpl.java:34)
	 * 	at org.hibernate.metamodel.internal.EmbeddableRepresentationStrategyPojo.buildPropertyAccess(EmbeddableRepresentationStrategyPojo.java:194)
	 * 	at org.hibernate.metamodel.internal.EmbeddableRepresentationStrategyPojo.<init>(EmbeddableRepresentationStrategyPojo.java:70)
	 * 	at org.hibernate.metamodel.internal.ManagedTypeRepresentationResolverStandard.resolveStrategy(ManagedTypeRepresentationResolverStandard.java:77)
	 * 	at org.hibernate.metamodel.mapping.internal.EmbeddableMappingTypeImpl.<init>(EmbeddableMappingTypeImpl.java:176)
	 * 	at org.hibernate.metamodel.mapping.internal.EmbeddableMappingTypeImpl.from(EmbeddableMappingTypeImpl.java:124)
	 * 	at org.hibernate.metamodel.mapping.internal.MappingModelCreationHelper.buildEmbeddedAttributeMapping(MappingModelCreationHelper.java:392)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.buildEmbeddedAttributeMapping(AbstractEntityPersister.java:5558)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.generateNonIdAttributeMapping(AbstractEntityPersister.java:5491)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.generateNonIdAttributeMapping(AbstractEntityPersister.java:5290)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.buildDeclaredAttributeMappings(AbstractEntityPersister.java:4714)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.prepareMappings(AbstractEntityPersister.java:4666)
	 * 	at org.hibernate.persister.entity.AbstractEntityPersister.prepareMappingModel(AbstractEntityPersister.java:4632)
	 * 	at org.hibernate.metamodel.mapping.internal.MappingModelCreationProcess.execute(MappingModelCreationProcess.java:88)
	 * 	at org.hibernate.metamodel.mapping.internal.MappingModelCreationProcess.process(MappingModelCreationProcess.java:43)
	 * 	at org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl.finishInitialization(MappingMetamodelImpl.java:166)
	 * 	at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:283)
	 * 	at io.quarkus.hibernate.orm.runtime.boot.FastBootEntityManagerFactoryBuilder.build(FastBootEntityManagerFactoryBuilder.java:101)
	 * 	... 5 more
	 */

	public PropertyAccessBasicImpl(
			PropertyAccessStrategyBasicImpl strategy,
			HibernateAccessorFactory accessorFactory,
			Class<?> containerJavaType,
			final String propertyName,
			boolean setterRequired) {
		this.strategy = strategy;

		final var getterMethod = findGetterMethod( containerJavaType, propertyName );
		getter = new GetterMethodImpl( accessorFactory, containerJavaType, propertyName, getterMethod );

		final var setterMethod = setterRequired
				? findSetterMethod( containerJavaType, propertyName, getterMethod.getReturnType() )
				: setterMethodOrNull( containerJavaType, propertyName, getterMethod.getReturnType() );
		setter = setterMethod != null
				? new SetterMethodImpl( accessorFactory, containerJavaType, propertyName, setterMethod )
				: null;
	}

	@Override
	public PropertyAccessStrategy getPropertyAccessStrategy() {
		return strategy;
	}

	@Override
	public Getter getGetter() {
		return getter;
	}

	@Override
	public @Nullable Setter getSetter() {
		return setter;
	}
}
