/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html.
 */
package org.hibernate.boot.models.annotations.internal;

import java.lang.annotation.Annotation;

import org.hibernate.boot.models.JpaAnnotations;
import org.hibernate.boot.models.annotations.spi.AttributeMarker;
import org.hibernate.models.spi.SourceModelBuildingContext;

import org.jboss.jandex.AnnotationInstance;

import jakarta.persistence.OneToOne;

import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJandexValue;
import static org.hibernate.boot.models.internal.OrmAnnotationHelper.extractJdkValue;

@SuppressWarnings({ "ClassExplicitlyAnnotation", "unused" })
@jakarta.annotation.Generated("org.hibernate.orm.build.annotations.ClassGeneratorProcessor")
public class OneToOneJpaAnnotation
		implements OneToOne, AttributeMarker.Fetchable, AttributeMarker.Cascadeable, AttributeMarker.Optionalable {
	private java.lang.Class<?> targetEntity;
	private jakarta.persistence.CascadeType[] cascade;
	private jakarta.persistence.FetchType fetch;
	private boolean optional;
	private String mappedBy;
	private boolean orphanRemoval;

	/**
	 * Used in creating dynamic annotation instances (e.g. from XML)
	 */
	public OneToOneJpaAnnotation(SourceModelBuildingContext modelContext) {
		this.targetEntity = void.class;
		this.cascade = new jakarta.persistence.CascadeType[0];
		this.fetch = jakarta.persistence.FetchType.EAGER;
		this.optional = true;
		this.mappedBy = "";
		this.orphanRemoval = false;
	}

	/**
	 * Used in creating annotation instances from JDK variant
	 */
	public OneToOneJpaAnnotation(OneToOne annotation, SourceModelBuildingContext modelContext) {
		this.targetEntity = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "targetEntity", modelContext );
		this.cascade = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "cascade", modelContext );
		this.fetch = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "fetch", modelContext );
		this.optional = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "optional", modelContext );
		this.mappedBy = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "mappedBy", modelContext );
		this.orphanRemoval = extractJdkValue( annotation, JpaAnnotations.ONE_TO_ONE, "orphanRemoval", modelContext );
	}

	/**
	 * Used in creating annotation instances from Jandex variant
	 */
	public OneToOneJpaAnnotation(AnnotationInstance annotation, SourceModelBuildingContext modelContext) {
		this.targetEntity = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "targetEntity", modelContext );
		this.cascade = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "cascade", modelContext );
		this.fetch = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "fetch", modelContext );
		this.optional = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "optional", modelContext );
		this.mappedBy = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "mappedBy", modelContext );
		this.orphanRemoval = extractJandexValue( annotation, JpaAnnotations.ONE_TO_ONE, "orphanRemoval", modelContext );
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return OneToOne.class;
	}

	@Override
	public java.lang.Class<?> targetEntity() {
		return targetEntity;
	}

	public void targetEntity(java.lang.Class<?> value) {
		this.targetEntity = value;
	}


	@Override
	public jakarta.persistence.CascadeType[] cascade() {
		return cascade;
	}

	public void cascade(jakarta.persistence.CascadeType[] value) {
		this.cascade = value;
	}


	@Override
	public jakarta.persistence.FetchType fetch() {
		return fetch;
	}

	public void fetch(jakarta.persistence.FetchType value) {
		this.fetch = value;
	}


	@Override
	public boolean optional() {
		return optional;
	}

	public void optional(boolean value) {
		this.optional = value;
	}


	@Override
	public String mappedBy() {
		return mappedBy;
	}

	public void mappedBy(String value) {
		this.mappedBy = value;
	}


	@Override
	public boolean orphanRemoval() {
		return orphanRemoval;
	}

	public void orphanRemoval(boolean value) {
		this.orphanRemoval = value;
	}


}
