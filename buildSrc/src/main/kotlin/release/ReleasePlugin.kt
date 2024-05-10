/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package release

import org.gradle.api.Plugin
import org.gradle.api.Project
import release.github.Failure.ExceptionalFailure
import release.github.GitHub.ReleaseVersionOutcome.Failure
import release.github.GitHub.ReleaseVersionOutcome.Success
import release.github.GitHubHttp
import release.github.GitHubHttp.GitHubApiAuthority.Companion.productionGitHubApi
import release.github.LoggingAuditor
import release.github.formatFailure
import release.pki.ReleaseTrustStore.Companion.defaultReleaseTrustStore

class ReleasePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.version = determineVersion(target).also {
            target.logger.info("Using version $it")
        }
        val extension = target.extensions.create("releasing", ReleasePluginExtension::class.java)
        target.tasks.register("sourceforgeRelease", SourceforgeReleaseTask::class.java) {
            group = "publishing"
            combinedJar.set(extension.combinedJar)
            smallJar.set(extension.smallJar)
            documentationTar.set(extension.documentationTar)
        }
        target.tasks.register("gitHubRelease", GitHubReleaseTask::class.java) {
            group = "publishing"
            jar.set(extension.jar)
            combinedJar.set(extension.combinedJar)
            smallJar.set(extension.smallJar)
        }
    }

    private fun determineVersion(target: Project) = when (val versionFromEnvironment = System.getenv("URIN_VERSION")) {
        null -> when (val outcome = GitHubHttp(productionGitHubApi, defaultReleaseTrustStore, LoggingAuditor(target.logger)).latestReleaseVersion()) {
            is Failure -> {
                when(val failure = outcome.failure) {
                    is ExceptionalFailure -> target.logger.warn("Defaulting to development version: getting latest GitHub release failed: " + formatFailure(failure), failure.exception)
                    else -> target.logger.warn("Defaulting to development version: getting latest GitHub release failed: " + formatFailure(failure))
                }
                VersionNumber.DevelopmentVersion
            }
            is Success -> outcome.versionNumber.increment()
        }
        else -> VersionNumber.fromString(versionFromEnvironment)
    }

}