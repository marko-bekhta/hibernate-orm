module org.hibernate.orm.vector {
	requires com.microsoft.sqlserver.jdbc;
	requires com.oracle.database.jdbc;
	requires java.sql;
	requires static org.checkerframework.checker.qual;
	requires transitive org.hibernate.orm.core;

	exports org.hibernate.vector;
	exports org.hibernate.vector.internal;

	provides org.hibernate.boot.model.FunctionContributor with
		org.hibernate.vector.internal.PGVectorFunctionContributor,
		org.hibernate.vector.internal.OracleVectorFunctionContributor,
		org.hibernate.vector.internal.MariaDBFunctionContributor,
		org.hibernate.vector.internal.MySQLFunctionContributor,
		org.hibernate.vector.internal.DB2VectorFunctionContributor,
		org.hibernate.vector.internal.CockroachFunctionContributor,
		org.hibernate.vector.internal.HANAVectorFunctionContributor,
		org.hibernate.vector.internal.SQLServerVectorFunctionContributor;
	provides org.hibernate.boot.model.TypeContributor with
		org.hibernate.vector.internal.PGVectorTypeContributor,
		org.hibernate.vector.internal.OracleVectorTypeContributor,
		org.hibernate.vector.internal.MariaDBTypeContributor,
		org.hibernate.vector.internal.MySQLTypeContributor,
		org.hibernate.vector.internal.DB2VectorTypeContributor,
		org.hibernate.vector.internal.CockroachTypeContributor,
		org.hibernate.vector.internal.HANAVectorTypeContributor,
		org.hibernate.vector.internal.SQLServerTypeContributor;

}
