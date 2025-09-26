package com.reproducer

import java.io.File

import org.gradle.testkit.runner.GradleRunner

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class WithPluginClasspathTest {

    @JvmField
    @Rule
    val temporaryFolder = TemporaryFolder()

    val projectDir by lazy {
        File(temporaryFolder.root, "test").apply { mkdirs() }
    }

    @Test
    fun test() {
        projectDir.resolve("settings.gradle.kts").writeText(
            "rootProject.name = \"test\""
        )

        projectDir.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    java
                    id("my-plugin")
                }
                
                tasks.named("work", com.reproducer.MyTask::class.java) {
                    val myTask = this as com.reproducer.MyTask
                    myTask.urlProvider = { id -> "sdfsdf" }
                }
                """
        ) // FUN FACT: using just `tasks.named("work")` above will make the test pass...

        val runner =
            GradleRunner.create()
                .withPluginClasspath()
                .withArguments("work", "--configuration-cache", "--stacktrace")
                .forwardOutput()
                .withProjectDir(projectDir)

        var result = runner.build()
        require("BUILD SUCCESSFUL" in result.output)
    }
}