module org.hibernate.orm.testing {
	requires java.management;
	requires narayana.jta;
	requires org.apache.logging.log4j;
	requires org.hibernate.orm.community.dialects;
	requires org.junit.platform.commons;

	requires transitive byteman;
	requires transitive jakarta.persistence;
	requires transitive jakarta.transaction;
	requires transitive java.logging;
	requires transitive java.money;
	requires transitive java.sql;
	requires transitive java.transaction.xa;
	requires transitive junit;
	requires transitive org.assertj.core;
	requires transitive org.hamcrest;
	requires transitive org.hibernate.models;
	requires transitive org.hibernate.orm.core;
	requires transitive org.jboss.logging;
	requires transitive org.junit.jupiter.api;
	requires transitive org.junit.jupiter.engine;
	requires transitive org.junit.platform.engine;
	requires transitive org.junit.platform.launcher;

	exports org.hibernate.testing;
	exports org.hibernate.testing.async;
	exports org.hibernate.testing.boot;
	exports org.hibernate.testing.bytecode.enhancement;
	exports org.hibernate.testing.bytecode.enhancement.extension;
	exports org.hibernate.testing.bytecode.enhancement.extension.engine;
	exports org.hibernate.testing.byteman;
	exports org.hibernate.testing.cache;
	exports org.hibernate.testing.cleaner;
	exports org.hibernate.testing.common.connections;
	exports org.hibernate.testing.env;
	exports org.hibernate.testing.hamcrest;
	exports org.hibernate.testing.jdbc;
	exports org.hibernate.testing.jdbc.leak;
	exports org.hibernate.testing.jta;
	exports org.hibernate.testing.junit4;
	exports org.hibernate.testing.logger;
	exports org.hibernate.testing.memory;
	exports org.hibernate.testing.orm;
	exports org.hibernate.testing.orm.assertj;
	exports org.hibernate.testing.orm.common;
	exports org.hibernate.testing.orm.domain;
	exports org.hibernate.testing.orm.domain.animal;
	exports org.hibernate.testing.orm.domain.contacts;
	exports org.hibernate.testing.orm.domain.gambit;
	exports org.hibernate.testing.orm.domain.helpdesk;
	exports org.hibernate.testing.orm.domain.library;
	exports org.hibernate.testing.orm.domain.retail;
	exports org.hibernate.testing.orm.domain.userguide;
	exports org.hibernate.testing.orm.domain.userguide.tooling;
	exports org.hibernate.testing.orm.jdbc;
	exports org.hibernate.testing.orm.jpa;
	exports org.hibernate.testing.orm.junit;
	exports org.hibernate.testing.orm.logger;
	exports org.hibernate.testing.orm.transaction;
	exports org.hibernate.testing.schema;
	exports org.hibernate.testing.transaction;
	exports org.hibernate.testing.util;
	exports org.hibernate.testing.util.ast;
	exports org.hibernate.testing.util.jpa;
	exports org.hibernate.testing.util.uuid;

	provides org.hibernate.boot.registry.selector.StrategyRegistrationProvider with
		org.hibernate.testing.cache.StrategyRegistrationProviderImpl;
	provides org.jboss.logging.LoggerProvider with
		org.hibernate.testing.logger.TestableLoggerProvider;
	provides org.junit.platform.engine.TestEngine with
		org.hibernate.testing.bytecode.enhancement.extension.engine.BytecodeEnhancedTestEngine;
	provides org.junit.platform.launcher.PostDiscoveryFilter with
		org.hibernate.testing.bytecode.enhancement.extension.BytecodeEnhancementPostDiscoveryFilter;

}
