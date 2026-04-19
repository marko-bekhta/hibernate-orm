module org.hibernate.orm.envers {
	requires jakarta.persistence;
	requires java.sql;
	requires org.hibernate.models;
	requires org.hibernate.orm.core;
	requires org.jboss.logging;
	requires static org.jboss.logging.annotations;

	exports org.hibernate.envers;
	exports org.hibernate.envers.boot;
	exports org.hibernate.envers.boot.internal;
	exports org.hibernate.envers.boot.model;
	exports org.hibernate.envers.boot.registry.classloading;
	exports org.hibernate.envers.boot.spi;
	exports org.hibernate.envers.configuration;
	exports org.hibernate.envers.configuration.internal;
	exports org.hibernate.envers.configuration.internal.metadata;
	exports org.hibernate.envers.configuration.internal.metadata.reader;
	exports org.hibernate.envers.enhanced;
	exports org.hibernate.envers.event.spi;
	exports org.hibernate.envers.exception;
	exports org.hibernate.envers.function;
	exports org.hibernate.envers.internal;
	exports org.hibernate.envers.internal.entities;
	exports org.hibernate.envers.internal.entities.mapper;
	exports org.hibernate.envers.internal.entities.mapper.id;
	exports org.hibernate.envers.internal.entities.mapper.relation;
	exports org.hibernate.envers.internal.entities.mapper.relation.component;
	exports org.hibernate.envers.internal.entities.mapper.relation.lazy;
	exports org.hibernate.envers.internal.entities.mapper.relation.lazy.initializor;
	exports org.hibernate.envers.internal.entities.mapper.relation.lazy.proxy;
	exports org.hibernate.envers.internal.entities.mapper.relation.query;
	exports org.hibernate.envers.internal.reader;
	exports org.hibernate.envers.internal.revisioninfo;
	exports org.hibernate.envers.internal.synchronization;
	exports org.hibernate.envers.internal.synchronization.work;
	exports org.hibernate.envers.internal.tools;
	exports org.hibernate.envers.internal.tools.graph;
	exports org.hibernate.envers.internal.tools.query;
	exports org.hibernate.envers.query;
	exports org.hibernate.envers.query.criteria;
	exports org.hibernate.envers.query.criteria.internal;
	exports org.hibernate.envers.query.internal.impl;
	exports org.hibernate.envers.query.internal.property;
	exports org.hibernate.envers.query.order;
	exports org.hibernate.envers.query.order.internal;
	exports org.hibernate.envers.query.projection;
	exports org.hibernate.envers.query.projection.internal;
	exports org.hibernate.envers.strategy;
	exports org.hibernate.envers.strategy.internal;
	exports org.hibernate.envers.strategy.spi;
	exports org.hibernate.envers.tools;

	provides org.hibernate.boot.model.FunctionContributor with
		org.hibernate.envers.boot.internal.FunctionContributorImpl;
	provides org.hibernate.boot.model.TypeContributor with
		org.hibernate.envers.boot.internal.TypeContributorImpl;
	provides org.hibernate.boot.registry.selector.StrategyRegistrationProvider with
		org.hibernate.envers.boot.internal.ModifiedColumnNamingStrategyRegistrationProvider,
		org.hibernate.envers.boot.internal.AuditStrategyRegistrationProvider;
	provides org.hibernate.boot.spi.AdditionalMappingContributor with
		org.hibernate.envers.boot.internal.AdditionalMappingContributorImpl;
	provides org.hibernate.integrator.spi.Integrator with
		org.hibernate.envers.boot.internal.EnversIntegrator;
	provides org.hibernate.service.spi.ServiceContributor with
		org.hibernate.envers.boot.internal.EnversServiceContributor;

}
