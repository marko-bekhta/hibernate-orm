/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.jakarta;

import javax.inject.Inject;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.ExecOperations;

import static org.hibernate.orm.jakarta.JakartaPlugin.JAKARTA;

/**
 * @author Steve Ebersole
 */
@CacheableTask
public abstract class JakartaJarTransformation extends DefaultTask {
	private final RegularFileProperty sourceJar;
	private final RegularFileProperty targetJar;
	private final ExecOperations execOperations;

	@Inject
	public JakartaJarTransformation(ObjectFactory objectFactory, ExecOperations execOperations) {
		this.sourceJar = objectFactory.fileProperty();
		this.targetJar = objectFactory.fileProperty();
		this.execOperations = execOperations;

		setGroup( JAKARTA );
	}

	@InputFile
	@PathSensitive( PathSensitivity.RELATIVE )
	public RegularFileProperty getSourceJar() {
		return sourceJar;
	}

	@OutputFile
	public RegularFileProperty getTargetJar() {
		return targetJar;
	}

	@TaskAction
	void transform() {
		execOperations.javaexec(
				(javaExecSpec) -> {
					javaExecSpec.classpath( getProject().getConfigurations().getByName( "jakartaeeTransformTool" ) );
					javaExecSpec.getMainClass().set( "org.eclipse.transformer.jakarta.JakartaTransformer" );
					javaExecSpec.args(
							sourceJar.get().getAsFile().getAbsolutePath(),
							targetJar.get().getAsFile().getAbsolutePath(),
							"-q",
							"-tr", getProject().getRootProject().file( "rules/jakarta-renames.properties" ).getAbsolutePath(),
							"-tv", getProject().getRootProject().file( "rules/jakarta-versions.properties" ).getAbsolutePath(),
							"-td", getProject().getRootProject().file( "rules/jakarta-direct.properties" ).getAbsolutePath()
					);
				}
		);
	}
}
