module org.hibernate.orm.community.dialects {
	requires org.jboss.logging;
	requires jakarta.persistence;
	requires java.sql;

	requires transitive org.hibernate.orm.core;
	requires static org.checkerframework.checker.qual;

	exports org.hibernate.community.dialect;
	exports org.hibernate.community.dialect.aggregate;
	exports org.hibernate.community.dialect.function;
	exports org.hibernate.community.dialect.function.array;
	exports org.hibernate.community.dialect.function.json;
	exports org.hibernate.community.dialect.identity;
	exports org.hibernate.community.dialect.lock.internal;
	exports org.hibernate.community.dialect.pagination;
	exports org.hibernate.community.dialect.sequence;
	exports org.hibernate.community.dialect.sql.ast;
	exports org.hibernate.community.dialect.temptable;
	exports org.hibernate.community.dialect.unique;

	provides org.hibernate.boot.registry.selector.spi.DialectSelector with
		org.hibernate.community.dialect.CommunityDialectSelector;
	provides org.hibernate.engine.jdbc.dialect.spi.DialectResolver with
		org.hibernate.community.dialect.CommunityDialectResolver;

}
