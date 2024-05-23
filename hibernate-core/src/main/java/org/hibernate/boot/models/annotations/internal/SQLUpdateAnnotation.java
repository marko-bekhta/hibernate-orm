/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;

import org.hibernate.annotations.SQLUpdate;
import org.hibernate.boot.models.HibernateAnnotations;
import org.hibernate.boot.models.annotations.spi.CustomSqlDetails;
import org.hibernate.models.spi.SourceModelBuildingContext;

import org.jboss.jandex.AnnotationInstance;

import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJandexValue;
import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJdkValue;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class SQLUpdateAnnotation implements SQLUpdate, CustomSqlDetails {
	private String sql;
	private boolean callable;
	private java.lang.Class<? extends org.hibernate.jdbc.Expectation> verify;
	private org.hibernate.annotations.ResultCheckStyle check;
	private String table;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public SQLUpdateAnnotation(SourceModelBuildingContext modelContext) {
		this.callable = false;
		this.verify = org.hibernate.jdbc.Expectation.class;
		this.check = org.hibernate.annotations.ResultCheckStyle.NONE;
		this.table = "";
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public SQLUpdateAnnotation(SQLUpdate annotation, SourceModelBuildingContext modelContext) {
		this.sql = extractJdkValue( annotation, HibernateAnnotations.SQL_UPDATE, "sql", modelContext );
		this.callable = extractJdkValue( annotation, HibernateAnnotations.SQL_UPDATE, "callable", modelContext );
		this.verify = extractJdkValue( annotation, HibernateAnnotations.SQL_UPDATE, "verify", modelContext );
		this.check = extractJdkValue( annotation, HibernateAnnotations.SQL_UPDATE, "check", modelContext );
		this.table = extractJdkValue( annotation, HibernateAnnotations.SQL_UPDATE, "table", modelContext );
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public SQLUpdateAnnotation(AnnotationInstance annotation, SourceModelBuildingContext modelContext) {
		this.sql = extractJandexValue( annotation, HibernateAnnotations.SQL_UPDATE, "sql", modelContext );
		this.callable = extractJandexValue( annotation, HibernateAnnotations.SQL_UPDATE, "callable", modelContext );
		this.verify = extractJandexValue( annotation, HibernateAnnotations.SQL_UPDATE, "verify", modelContext );
		this.check = extractJandexValue( annotation, HibernateAnnotations.SQL_UPDATE, "check", modelContext );
		this.table = extractJandexValue( annotation, HibernateAnnotations.SQL_UPDATE, "table", modelContext );
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return SQLUpdate.class;
	}

	@Override
	public String sql() {
		return sql;
	}

	public void sql(String value) {
		this.sql = value;
	}


	@Override
	public boolean callable() {
		return callable;
	}

	public void callable(boolean value) {
		this.callable = value;
	}


	@Override
	public java.lang.Class<? extends org.hibernate.jdbc.Expectation> verify() {
		return verify;
	}

	public void verify(java.lang.Class<? extends org.hibernate.jdbc.Expectation> value) {
		this.verify = value;
	}


	@Override
	public org.hibernate.annotations.ResultCheckStyle check() {
		return check;
	}

	public void check(org.hibernate.annotations.ResultCheckStyle value) {
		this.check = value;
	}


	@Override
	public String table() {
		return table;
	}

	public void table(String value) {
		this.table = value;
	}


}
