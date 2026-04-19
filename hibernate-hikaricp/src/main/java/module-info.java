module org.hibernate.orm.hikaricp {
	requires com.zaxxer.hikari;
	requires java.sql;
	requires transitive org.hibernate.orm.core;

	exports org.hibernate.hikaricp.internal;

	provides org.hibernate.boot.registry.selector.StrategyRegistrationProvider with
		org.hibernate.hikaricp.internal.StrategyRegistrationProviderImpl;

}
