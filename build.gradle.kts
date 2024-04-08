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
    id("org.javamodularity.moduleplugin") version "1.8.15"
    idea
    pmd
    `java-test-fixtures`
    `jvm-test-suite`
    id("com.github.spotbugs") version "6.0.9"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("com.gitlab.svg2ico") version "1.4"
    id("org.asciidoctor.jvm.convert") version "4.0.2"
    id("org.asciidoctor.jvm.gems") version "4.0.2"

    id("release.sourceforge")
}

group = "net.sourceforge.urin"
description = "Urin is an open source URI generator and parser written in Java."

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
    testFixturesImplementation(group = "org.apache.commons", name = "commons-lang3", version = "3.14.0")
    testFixturesImplementation(group = "org.hamcrest", name = "hamcrest", version = "2.2")

    spotbugs(group = "com.github.spotbugs", name = "spotbugs", version = "4.8.3")

    asciidoctorGems(group = "rubygems", name = "asciidoctor-tabs", version = "1.0.0.beta.6")
}

testing {
    @Suppress("UnstableApiUsage")
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
                implementation("org.junit.jupiter:junit-jupiter-params:5.10.2")
                implementation("org.hamcrest:hamcrest:2.2")
                implementation("org.apache.commons:commons-lang3:3.12.0")
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
            }
        }

        register<JvmTestSuite>("testIntegration") {
            useJUnitJupiter()
            dependencies {
                implementation(project())
                implementation(testFixtures(project()))
                implementation("org.hamcrest:hamcrest:2.2")
            }
        }

        register<JvmTestSuite>("docs") {
            useJUnitJupiter()
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
    toolVersion = "6.51.0"
    ruleSetFiles = files("tools/pmd-ruleset.xml")
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
        width = 128
        height = 128
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
        ruleSetFiles =
            files("tools/pmd-ruleset.xml", "tools/pmd-non-docs-extra-ruleset.xml", "tools/pmd-main-extra-ruleset.xml")
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
        dependsOn(
            clean,
            build,
            publish,
            closeAndReleaseStagingRepositories,
            sourceforgeRelease,
            incrementVersionNumber
        )
    }

    incrementVersionNumber {
        mustRunAfter(closeAndReleaseStagingRepositories, sourceforgeRelease)
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

artifacts {
    archives(javadocJar)
    archives(sourcesJar)
}

releasing {
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
                url = "http://urin.sourceforge.net"
                scm {
                    url = "git://git.code.sf.net/p/urin/code"
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
