package com.reproducer

import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val task = project.tasks.register("work", MyTask::class.java) {
            /*urlProvider = { id ->
                when (id.group) {
                    "group" -> "https://group.com/${id.name}/${id.version}/"
                    else -> "https://javadoc.io/doc/${id.group}/${id.name}/${id.version}/"
                }
            }*/
            id.set(project.configurations.named("runtimeClasspath").get().incoming.resolutionResult.rootComponent.get().moduleVersion!!)
            outputFile.set(project.layout.buildDirectory.file("output.txt"))
        }
    
        println("${MyPlugin::class.java.name} applied!")
    }
}