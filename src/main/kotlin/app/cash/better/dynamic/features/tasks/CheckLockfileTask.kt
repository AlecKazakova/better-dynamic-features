package app.cash.better.dynamic.features.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

abstract class CheckLockfileTask : DefaultTask() {
  @get:InputFile
  abstract var newLockfile: File

  @get:InputFile
  abstract var currentLockfile: File

  @get:OutputFile
  abstract var outputFile: File

  @TaskAction
  fun checkLockfiles() {
    val areTheyEqual = newLockfile.readText() == currentLockfile.readText()
    outputFile.writeText(areTheyEqual.toString())
    if (!areTheyEqual) {
      Files.copy(newLockfile.toPath(), currentLockfile.toPath(), StandardCopyOption.REPLACE_EXISTING)
      throw IllegalStateException("The lockfile was out of date and has been updated. Rerun your build.")
    }
  }
}