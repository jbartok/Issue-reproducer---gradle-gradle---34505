package com.reproducer

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class MyTask : DefaultTask() {

    @get:Input
    var urlProvider: (ModuleVersionIdentifier) -> String =
        { id -> "https://javadoc.io/doc/${'$'}{id.group}/${'$'}{id.name}/${'$'}{id.version}/" }

    @get:Input
    abstract val id: Property<ModuleVersionIdentifier>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    protected fun run() {
        val url = urlProvider(id.get())
        outputFile.get().asFile.appendText(url)
    }
}