/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

import groovy.transform.Field
import io.jenkins.blueocean.rest.impl.pipeline.PipelineNodeGraphVisitor
import io.jenkins.blueocean.rest.impl.pipeline.FlowNodeWrapper
import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper

/*
 * See https://github.com/hibernate/hibernate-jenkins-pipeline-helpers
 */
@Library('hibernate-jenkins-pipeline-helpers@1.13') _
import org.hibernate.jenkins.pipeline.helpers.job.JobHelper

@Field final String DEFAULT_JDK_VERSION = '17'
@Field final String DEFAULT_JDK_TOOL = "OpenJDK ${DEFAULT_JDK_VERSION} Latest"
@Field final String NODE_PATTERN_BASE = 'Worker&&Containers'
@Field List<BuildEnvironment> environments

this.helper = new JobHelper(this)

helper.runWithNotification {
stage('Configure') {
	this.environments = [
		// Minimum supported versions
		new BuildEnvironment( dbName: 'hsqldb_2_6' ),
		new BuildEnvironment( dbName: 'mysql_8_0' ),
		new BuildEnvironment( dbName: 'mariadb_10_5' ),
		new BuildEnvironment( dbName: 'postgresql_12' ),
		new BuildEnvironment( dbName: 'edb_12' ),
		new BuildEnvironment( dbName: 'db2_10_5', longRunning: true ),
		new BuildEnvironment( dbName: 'mssql_2017' ), // Unfortunately there is no SQL Server 2008 image, so we have to test with 2017
// 		new BuildEnvironment( dbName: 'sybase_16' ), // There only is a Sybase ASE 16 image, so no pint in testing that nightly
		new BuildEnvironment( dbName: 'sybase_jconn' ),
		// Long running databases
		new BuildEnvironment( dbName: 'cockroachdb', node: 'cockroachdb', longRunning: true ),
		new BuildEnvironment( dbName: 'hana_cloud', dbLockableResource: 'hana-cloud', dbLockResourceAsHost: true )
	];

	helper.configure {
		file 'job-configuration.yaml'
		// We don't require the following, but the build helper plugin apparently does
		jdk {
			defaultTool DEFAULT_JDK_TOOL
		}
		maven {
			defaultTool 'Apache Maven 3.8'
		}
	}
	properties([
			buildDiscarder(
					logRotator(daysToKeepStr: '30', numToKeepStr: '10')
			),
			rateLimitBuilds(throttle: [count: 1, durationName: 'day', userBoost: true]),
			// If two builds are about the same branch or pull request,
			// the older one will be aborted when the newer one starts.
			disableConcurrentBuilds(abortPrevious: true),
			helper.generateNotificationProperty()
	])
}

// Avoid running the pipeline on branch indexing
if (currentBuild.getBuildCauses().toString().contains('BranchIndexingCause')) {
  	print "INFO: Build skipped due to trigger being Branch Indexing"
	currentBuild.result = 'NOT_BUILT'
  	return
}

stage('Build') {
	Map<String, Closure> executions = [:]
	Map<String, Map<String, String>> state = [:]
	environments.each { BuildEnvironment buildEnv ->
		// Don't build environments for newer JDKs when this is a PR
		if ( helper.scmSource.pullRequest && buildEnv.testJdkVersion ) {
			return
		}
		state[buildEnv.tag] = [:]
	}
	// Don't run additional checks when this is a PR
	if ( !helper.scmSource.pullRequest ) {
		executions.put('Strict JAXP configuration', {
			runBuildOnNode(NODE_PATTERN_BASE) {
				// we want to test with JDK 23 where the strict settings were introduced
				def testJavaHome = tool(name: "OpenJDK 23 Latest", type: 'jdk')
				def javaHome = tool(name: DEFAULT_JDK_TOOL, type: 'jdk')
				// Use withEnv instead of setting env directly, as that is global!
				// See https://github.com/jenkinsci/pipeline-plugin/blob/master/TUTORIAL.md
				withEnv(["JAVA_HOME=${javaHome}", "PATH+JAVA=${javaHome}/bin"]) {
					stage('Checkout') {
						checkout scm
					}
					stage('Test') {
						withGradle {
						    def lastCommitter = sh(script: 'git show -s --format=\'%an\'', returnStdout: true).trim()
                            def secondLastCommitter = sh(script: 'git show -s --format=\'%an\' HEAD~1', returnStdout: true).trim()

                            print "lastCommitter ${lastCommitter}"
                            print "secondLastCommitter ${secondLastCommitter}"
                            print "get class ${secondLastCommitter.getClass()}"
                            print "test1 ${lastCommitter == 'marko-bekhta'}"
                            print "test2 ${lastCommitter.equals('marko-bekhta')}"
                            if (lastCommitter == 'marko-bekhta' && secondLastCommitter == 'marko-bekhta') {
                                print "marko-bekhta == marko-bekhta"
                            }

							def tempDir = pwd(tmp: true)
							def jaxpStrictProperties = tempDir + '/jaxp-strict.properties'
							def jaxpStrictTemplate = testJavaHome + '/conf/jaxp-strict.properties.template'

							echo 'Copy strict JAXP configuration properties.'
							sh "cp $jaxpStrictTemplate $jaxpStrictProperties"

							// explicitly calling toString here to prevent Jenkins failures like:
							//  > Scripts not permitted to use method groovy.lang.GroovyObject invokeMethod java.lang.String java.lang.Object (org.codehaus.groovy.runtime.GStringImpl positive)
							String args = ("-Ptest.jdk.version=23 -Porg.gradle.java.installations.paths=${javaHome},${testJavaHome}"
								+ " -Ptest.jdk.launcher.args=\"-Djava.xml.config.file=${jaxpStrictProperties}\"").toString()

							timeout( [time: 60, unit: 'MINUTES'] ) {
								ciBuild(args)
							}
						}
					}
				}
			}
		})
	}
	parallel(executions)
}

} // End of helper.runWithNotification

// Job-specific helpers

class BuildEnvironment {
	String testJdkVersion
	String testJdkLauncherArgs
	String dbName = 'h2'
	String node
	String dbLockableResource
	boolean dbLockResourceAsHost
	String additionalOptions
	String notificationRecipients
	boolean longRunning

	String toString() { getTag() }
	String getTag() { "${node ? node + "_" : ''}${testJdkVersion ? 'jdk_' + testJdkVersion + '_' : '' }${dbName}" }
}

void runBuildOnNode(String label, Closure body) {
	node( label ) {
		pruneDockerContainers()
    tryFinally(body, {
      // If this is a PR, we clean the workspace at the end
      if ( env.CHANGE_BRANCH != null ) {
        cleanWs()
      }
      pruneDockerContainers()
    })
	}
}

void ciBuild(buildEnv, String args) {
  // On untrusted nodes, we use the same access key as for PRs:
  // it has limited access, essentially it can only push build scans.
  def develocityCredentialsId = buildEnv.node ? 'ge.hibernate.org-access-key-pr' : 'ge.hibernate.org-access-key'

  ciBuild(develocityCredentialsId, args)
}

void ciBuild(String args) {
  ciBuild('ge.hibernate.org-access-key-pr', args)
}

void ciBuild(String develocityCredentialsId, String args) {
  withCredentials([string(credentialsId: develocityCredentialsId,
      variable: 'DEVELOCITY_ACCESS_KEY')]) {
    withGradle { // withDevelocity, actually: https://plugins.jenkins.io/gradle/#plugin-content-capturing-build-scans-from-jenkins-pipeline
      sh "./ci/build.sh $args"
    }
  }
}

void pruneDockerContainers() {
	if ( !sh( script: 'command -v docker || true', returnStdout: true ).trim().isEmpty() ) {
		sh 'docker container prune -f || true'
		sh 'docker image prune -f || true'
		sh 'docker network prune -f || true'
		sh 'docker volume prune -f || true'
	}
}

void handleNotifications(currentBuild, buildEnv) {
	def currentResult = getParallelResult(currentBuild, buildEnv.tag)
	boolean success = currentResult == 'SUCCESS' || currentResult == 'UNKNOWN'
	def previousResult = currentBuild.previousBuild == null ? null : getParallelResult(currentBuild.previousBuild, buildEnv.tag)

	// Ignore success after success
	if ( !( success && previousResult == 'SUCCESS' ) ) {
		def subject
		def body
		if ( success ) {
			if ( previousResult != 'SUCCESS' && previousResult != null ) {
				subject = "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Fixed"
				body = """<p>${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Fixed:</p>
					<p>Check console output at <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a> to view the results.</p>"""
			}
			else {
				subject = "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Success"
				body = """<p>${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Success:</p>
					<p>Check console output at <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a> to view the results.</p>"""
			}
		}
		else if (currentBuild.rawBuild.getActions(jenkins.model.InterruptedBuildAction.class).isEmpty()) {
			// If there are interrupted build actions, this means the build was cancelled, probably superseded
			// Thanks to https://issues.jenkins.io/browse/JENKINS-43339 for the "hack" to determine this
			if ( currentResult == 'FAILURE' ) {
				if ( previousResult != null && previousResult == "FAILURE" ) {
					subject = "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Still failing"
					body = """<p>${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Still failing:</p>
						<p>Check console output at <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a> to view the results.</p>"""
				}
				else {
					subject = "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Failure"
					body = """<p>${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - Failure:</p>
						<p>Check console output at <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a> to view the results.</p>"""
				}
			}
			else {
				subject = "${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${currentResult}"
				body = """<p>${env.JOB_NAME} - Build ${env.BUILD_NUMBER} - ${currentResult}:</p>
					<p>Check console output at <a href='${env.BUILD_URL}'>${env.BUILD_URL}</a> to view the results.</p>"""
			}
		}

		emailext(
				subject: subject,
				body: body,
				to: buildEnv.notificationRecipients
		)
	}
}

@NonCPS
String getParallelResult( RunWrapper build, String parallelBranchName ) {
    def visitor = new PipelineNodeGraphVisitor( build.rawBuild )
    def branch = visitor.pipelineNodes.find{ it.type == FlowNodeWrapper.NodeType.PARALLEL && parallelBranchName == it.displayName }
    if ( branch == null ) {
    	echo "Couldn't find parallel branch name '$parallelBranchName'. Available parallel branch names:"
		visitor.pipelineNodes.findAll{ it.type == FlowNodeWrapper.NodeType.PARALLEL }.each{
			echo " - ${it.displayName}"
		}
    	return null;
    }
    return branch.status.result
}

// try-finally construct that properly suppresses exceptions thrown in the finally block.
def tryFinally(Closure main, Closure ... finallies) {
	def mainFailure = null
	try {
		main()
	}
	catch (Throwable t) {
		mainFailure = t
		throw t
	}
	finally {
		finallies.each {it ->
			try {
				it()
			}
			catch (Throwable t) {
				if ( mainFailure ) {
					mainFailure.addSuppressed( t )
				}
				else {
					mainFailure = t
				}
			}
		}
	}
	if ( mainFailure ) { // We may reach here if only the "finally" failed
		throw mainFailure
	}
}
