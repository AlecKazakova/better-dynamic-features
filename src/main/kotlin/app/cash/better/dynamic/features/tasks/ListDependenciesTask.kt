// Copyright Square, Inc.
package app.cash.better.dynamic.features.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.ArtifactCollection
import org.gradle.api.capabilities.Capability
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ListDependenciesTask : DefaultTask() {
  private lateinit var dependencyArtifacts: ArtifactCollection

  fun setDependencyArtifacts(collection: ArtifactCollection) {
    this.dependencyArtifacts = collection
  }

  @get:Input
  abstract var projectName: String

  @get:OutputFile
  abstract var listFile: File

  @TaskAction fun printList() {
    val all = dependencyArtifacts.mapNotNull { it.variant.capabilities.firstOrNull() }
    listFile.writeDependenciesList(all)
  }

  private fun File.writeDependenciesList(list: List<Capability>) {
    writeText(
      """
      |:$projectName
      |${list.joinToString(separator = "\n") { "${it.group}:${it.name}$SEPARATOR${it.version}" }}
    """.trimMargin()
    )
  }

  companion object {
    const val SEPARATOR = "-->"
  }
}