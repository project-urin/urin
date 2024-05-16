/*
 * Copyright 2024 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    java
    signing
    `maven-publish`
    alias(libs.plugins.modulePlugin)
    idea
    pmd
    `java-test-fixtures`
    `jvm-test-suite`
    alias(libs.plugins.spotbugs)
    alias(libs.plugins.nexusPublish)
    alias(libs.plugins.svg2ico)
    alias(libs.plugins.asciidoctorConvert)
    alias(libs.plugins.asciidoctorGems)

    id("release")
}

group = "net.sourceforge.urin"
description = "Urin is a free, open source URI generator and parser written in Java."

repositories {
    mavenCentral()
    ruby {
        gems()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

dependencies {
    testFixturesImplementation(libs.commonsLang)
    testFixturesImplementation(libs.hamcrest)

    spotbugs(libs.spotbugs)

    asciidoctorGems(libs.asciidoctorTabs)
}

testing {
    @Suppress("UnstableApiUsage")
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(libs.hamcrest)
                implementation(libs.commonsLang)
            }
        }

        register<JvmTestSuite>("testIntegration") {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
                implementation(libs.hamcrest)
            }
        }

        register<JvmTestSuite>("docs") {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(project())
            }
        }
    }
}

idea {
    project {
        jdkName = "11"
    }
}

modularity {
    mixedJavaRelease(8)
}

pmd {
    toolVersion = "7.0.0"
    ruleSetFiles = files("tools/pmd-ruleset.xml", "tools/pmd-non-docs-extra-ruleset.xml")
    ruleSets = emptyList()
}

tasks {
    val compileSmallJava by registering(JavaCompile::class) {
        source = sourceSets["main"].allSource
        classpath = sourceSets["main"].compileClasspath
        destinationDirectory.set(project.layout.buildDirectory.dir("small-classes/main"))
        options.compilerArgs = listOf("-g:none")
    }

    compileTestJava {
        extensions.configure(org.javamodularity.moduleplugin.extensions.CompileTestModuleOptions::class) {
            isCompileOnClasspath = true
        }
    }

    test {
        extensions.configure(org.javamodularity.moduleplugin.extensions.TestModuleOptions::class) {
            runOnClasspath = true
        }
    }

    check {
        @Suppress("UnstableApiUsage")
        dependsOn(testing.suites["testIntegration"], testing.suites["docs"])
    }

    val combinedJar by registering(Jar::class) {
        archiveClassifier = "combined"
        from(sourceSets["main"].allSource)
        from(sourceSets["main"].output)
    }

    val smallJar by registering(Jar::class) {
        dependsOn(compileSmallJava, "compileModuleInfoJava")
        archiveClassifier = "small"
        from(project.layout.buildDirectory.dir("small-classes/main"))
        duplicatesStrategy =
            DuplicatesStrategy.EXCLUDE // because org.javamodularity.moduleplugin half-applies to this: it tries to add module-info.class it compiled separately in addition to the one compiled by the compileTinyJar task.
    }

    javadoc {
        title = "Urin version $version"
    }

    named<JavaCompile>("compileDocsJava") {
        javaCompiler.set(project.javaToolchains.compilerFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        })
    }

    named<Test>("docs") {
        javaLauncher.set(project.javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(21))
        })
    }

    val ico by registering(com.gitlab.svg2ico.Svg2IcoTask::class) {
        group = "documentation"
        source {
            sourcePath = file("resources/favicon.svg")
            output { width = 64; height = 64 }
            output { width = 48; height = 48 }
            output { width = 32; height = 32 }
            output { width = 24; height = 24 }
            output { width = 16; height = 16 }
        }
        destination = project.layout.buildDirectory.file("icons/favicon.ico")
    }

    val png by registering(com.gitlab.svg2ico.Svg2PngTask::class) {
        group = "documentation"
        source = file("resources/favicon.svg")
        width = 500
        height = 500
        destination = project.layout.buildDirectory.file("icons/favicon.png")
    }

    asciidoctor {
        dependsOn(
            ico,
            png,
            javadoc,
            "asciidoctorGemsPrepare"
        ) // doesn't seem to infer dependencies properly from the resources CopySpec
        resources {
            from(ico, png)
            from(javadoc) {
                into("javadoc")
            }
        }
        asciidoctorj {
            requires(
                "asciidoctor",
                project.layout.buildDirectory.file(".asciidoctorGems/gems/asciidoctor-tabs-1.0.0.beta.6/lib/asciidoctor-tabs.rb") // TODO this is a workaround for https://github.com/asciidoctor/asciidoctor-gradle-plugin/issues/718
            )
        }
    }

    val documentationTar by registering(Tar::class) {
        group = "documentation"
        from(asciidoctor)
        archiveBaseName.set("documentation")
        compression = Compression.GZIP
    }

    pmdMain {
        ruleSetFiles = files("tools/pmd-ruleset.xml", "tools/pmd-non-docs-extra-ruleset.xml", "tools/pmd-main-extra-ruleset.xml")
        ruleSets = emptyList()
    }

    named<Pmd>("pmdDocs") {
        ruleSetFiles = files("tools/pmd-ruleset.xml")
        ruleSets = emptyList()
    }

    named<com.github.spotbugs.snom.SpotBugsTask>("spotbugsDocs") {
        excludeFilter = file("tools/spotbugs-docs-filter.xml")
    }

    val release by registering {
        group = "publishing"
        dependsOn(clean, build, publish, closeAndReleaseStagingRepositories, sourceforgeRelease, gitHubRelease)
    }
}

val javadocJar by tasks.registering(Jar::class) {
    group = "documentation"
    archiveClassifier = "javadoc"
    from(tasks.javadoc)
}

val sourcesJar by tasks.registering(Jar::class) {
    dependsOn("compileModuleInfoJava")
    group = "documentation"
    archiveClassifier = "sources"
    from(sourceSets["main"].allSource)
}

releasing {
    jar = tasks.jar.get().archiveFile
    combinedJar = tasks.named<Jar>("combinedJar").get().archiveFile
    smallJar = tasks.named<Jar>("smallJar").get().archiveFile
    documentationTar = tasks.named<Tar>("documentationTar").get().archiveFile
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
            pom {
                name = "Urin"
                description = project.description
                url = "https://urin.sourceforge.net"
                scm {
                    url = "https://github.com/project-urin/urin.git"
                }
                developers {
                    developer {
                        id = "mos20"
                        name = "Mark Slater"
                    }
                }
                licenses {
                    license {
                        name = "The Apache Software License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        distribution = "repo"
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId = "7f928f40cc7cb"
            username.set(project.findProperty("ossrhUser").toString())
            password.set(project.findProperty("ossrhPassword").toString())
        }
    }
}

val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["testFixturesApiElements"]) { skip() }
javaComponent.withVariantsFromConfiguration(configurations["testFixturesRuntimeElements"]) { skip() }
