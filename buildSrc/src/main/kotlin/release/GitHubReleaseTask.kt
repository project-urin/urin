/*
 *  Copyright 2024 Mark Slater
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package release

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import release.github.Failure
import release.github.GitHubHttp
import release.github.GitHubHttp.GitHubApiAuthority.Companion.productionGitHubApi
import release.github.GitHubHttp.GitHubToken
import release.github.GitHubHttp.GitHubUploadAuthority.Companion.productionGitHubUpload
import release.github.LoggingAuditor
import release.github.PrivilegedGitHub.ReleaseOutcome
import release.github.PrivilegedGitHub.UploadArtifactOutcome
import release.github.formatFailure
import release.pki.ReleaseTrustStore.Companion.defaultReleaseTrustStore
import kotlin.time.Duration.Companion.seconds

abstract class GitHubReleaseTask : DefaultTask() {

    @get:InputFile
    abstract val jar: RegularFileProperty

    @get:InputFile
    abstract val combinedJar: RegularFileProperty

    @get:InputFile
    abstract val smallJar: RegularFileProperty

    @TaskAction
    fun release() {
        when (val version = project.version) {
            is VersionNumber.DevelopmentVersion -> throw GradleException("Cannot release development version")
            is VersionNumber.ReleaseVersion -> {
                val gitHubToken = project.property("gitHubToken").toString()
                val privilegedGitHub = GitHubHttp(
                    productionGitHubApi,
                    defaultReleaseTrustStore,
                    LoggingAuditor(project.logger),
                    firstByteTimeout = 30.seconds,
                    endToEndTimeout = 30.seconds
                )
                    .privileged(productionGitHubUpload, GitHubToken(gitHubToken))
                when (val releaseOutcome = privilegedGitHub.release(version)) {
                    is ReleaseOutcome.Success -> {
                        listOf(
                            Triple(jar, "urin-${project.version}.jar", "Jar"),
                            Triple(combinedJar, "urin-with-source-${project.version}.jar", "Jar with source code included"),
                            Triple(smallJar, "urin-small-${project.version}.jar", "Jar stripped of debug information"),
                        ).forEach { (source, targetName, label) ->
                            when (val uploadArtifactOutcome =
                                privilegedGitHub.uploadArtifact(releaseOutcome.releaseId, targetName, label, source.get().asFile.toPath())) {
                                UploadArtifactOutcome.Success -> Unit
                                is UploadArtifactOutcome.Failure -> throw uploadArtifactOutcome.failure.toGradleException("Uploading artifact for release $version failed")
                            }
                        }
                    }

                    is ReleaseOutcome.Failure -> throw releaseOutcome.failure.toGradleException("Releasing version $version failed")
                }
            }
        }
    }

    private fun Failure.toGradleException(message: String) = when (this) {
        is Failure.ExceptionalFailure -> GradleException("$message: ${formatFailure(this)}", this.exception)
        else -> GradleException("$message: ${formatFailure(this)}")
    }
}