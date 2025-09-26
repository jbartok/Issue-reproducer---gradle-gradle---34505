package com.reproducer

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class RealRepositoryTest {

    @JvmField
    @Rule
    val temporaryFolder = TemporaryFolder()

    val projectDir by lazy {
        File(temporaryFolder.root, "test").apply { mkdirs() }
    }

    @Test
    fun test() {
        val repo = File("build/repo").toURI().toURL().toExternalForm()

        projectDir.resolve("settings.gradle.kts").writeText(
            """
                rootProject.name = "test"
                pluginManagement {
                    repositories {
                        maven {
                            url = uri("$repo")
                        }
                        gradlePluginPortal()
                    }
                }
            """.trimIndent()

        )

        projectDir.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    java
                    id("my-plugin") version("1.0")
                }
                
                tasks.named("work", com.reproducer.MyTask::class.java) {
                    val myTask = this as com.reproducer.MyTask
                    myTask.urlProvider = { id -> "sdfsdf" }
                }
            """
        )

        val runner =
            GradleRunner.create()
                .withArguments("work", "--configuration-cache", "--stacktrace")
                .forwardOutput()
                .withProjectDir(projectDir)

        var result = runner.build()
        require("BUILD SUCCESSFUL" in result.output)
    }
}