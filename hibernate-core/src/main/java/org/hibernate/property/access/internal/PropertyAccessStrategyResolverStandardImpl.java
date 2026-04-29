/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.property.access.internal;

import jakarta.persistence.AccessType;
import org.hibernate.HibernateException;
import org.hibernate.accessor.HibernateAccessorFactory;
import org.hibernate.boot.registry.selector.spi.StrategySelector;
import org.hibernate.metamodel.RepresentationMode;
import org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies;
import org.hibernate.property.access.spi.HibernateAccessorFactoryResolver;
import org.hibernate.property.access.spi.PropertyAccessStrategy;
import org.hibernate.property.access.spi.PropertyAccessStrategyResolver;
import org.hibernate.service.ServiceRegistry;

import java.lang.invoke.MethodHandles;

import static org.hibernate.engine.internal.ManagedTypeHelper.isManagedType;
import static org.hibernate.internal.util.StringHelper.isNotEmpty;
import static org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies.BASIC;
import static org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies.FIELD;
import static org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies.MAP;
import static org.hibernate.property.access.spi.BuiltInPropertyAccessStrategies.MIXED;

/**
 * Standard implementation of PropertyAccessStrategyResolver
 *
 * @author Steve Ebersole
 */
public class PropertyAccessStrategyResolverStandardImpl implements PropertyAccessStrategyResolver {
	private final ServiceRegistry serviceRegistry;

	private final PropertyAccessStrategy propertyAccessStrategy;
	private final PropertyAccessStrategy fieldAccessStrategy;
	private final PropertyAccessStrategy standardAccessStrategy;

	public PropertyAccessStrategyResolverStandardImpl(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
		HibernateAccessorFactory hibernateAccessorFactory = serviceRegistry.requireService(
						HibernateAccessorFactoryResolver.class )
				.resolveHibernateAccessorFactoryResolver( MethodHandles.lookup() );

		this.propertyAccessStrategy = new PropertyAccessStrategyEnhancedImpl( hibernateAccessorFactory, AccessType.PROPERTY );
		this.fieldAccessStrategy =  new PropertyAccessStrategyEnhancedImpl( hibernateAccessorFactory, AccessType.FIELD );
		this.standardAccessStrategy =  new PropertyAccessStrategyEnhancedImpl( hibernateAccessorFactory, null );
	}

	@Override
	public PropertyAccessStrategy resolvePropertyAccessStrategy(
			Class<?> containerClass,
			String explicitAccessStrategyName,
			RepresentationMode representationMode) {

		if ( isManagedType( containerClass ) ) {
			if ( BASIC.getExternalName().equals( explicitAccessStrategyName ) ) {
				return propertyAccessStrategy;
			}
			else if ( FIELD.getExternalName().equals( explicitAccessStrategyName ) ) {
				return fieldAccessStrategy;
			}
			else if ( MIXED.getExternalName().equals( explicitAccessStrategyName ) ) {
				return standardAccessStrategy;
			}
		}

		if ( isNotEmpty( explicitAccessStrategyName ) ) {
			return resolveExplicitlyNamedPropertyAccessStrategy( explicitAccessStrategyName );
		}
		else if ( representationMode == RepresentationMode.MAP ) {
			return MAP.getStrategy();
		}
		else {
			return BASIC.getStrategy();
		}
	}

	protected PropertyAccessStrategy resolveExplicitlyNamedPropertyAccessStrategy(String explicitAccessStrategyName) {
		final var builtInStrategyEnum = BuiltInPropertyAccessStrategies.interpret( explicitAccessStrategyName );
		return builtInStrategyEnum != null
				? builtInStrategyEnum.getStrategy()
				: strategySelectorService().resolveStrategy( PropertyAccessStrategy.class, explicitAccessStrategyName );

	}

	private StrategySelector strategySelectorService;

	protected StrategySelector strategySelectorService() {
		if ( strategySelectorService == null ) {
			if ( serviceRegistry == null ) {
				throw new HibernateException( "ServiceRegistry not yet injected; PropertyAccessStrategyResolver not ready for use." );
			}
			strategySelectorService = serviceRegistry.requireService( StrategySelector.class );
		}
		return strategySelectorService;
	}

}
