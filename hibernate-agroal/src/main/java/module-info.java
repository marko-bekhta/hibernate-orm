module org.hibernate.orm.agroal {
	requires io.agroal.api;

	requires transitive java.sql;
	requires transitive org.hibernate.orm.core;

	exports org.hibernate.agroal.internal;

	provides org.hibernate.boot.registry.selector.StrategyRegistrationProvider with
		org.hibernate.agroal.internal.StrategyRegistrationProviderImpl;

}
