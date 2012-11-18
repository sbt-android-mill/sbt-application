/**
 * sbt-application - application builder with ProGuard and JavaFX support
 *
 * Copyright (c) 2012 Alexey Aksenov ezh@ezh.msk.ru
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sbt.application

import sbt._
import sbt.Keys._
import sbt.application.ApplicationKeys._
import sbt.application.javafx.JavaFX
import sbt.application.proguard.Proguard

/**
 * sbt-application plugin entry
 */
object Plugin extends sbt.Plugin {
  /** default plugin settings */
  val pluginSettings = Seq(
    applicationPackage <<= (mainClass) map { _.map(_.split("""\.""").dropRight(1).mkString(".")) },
    applicationSuffix := "-app",
    applicationLibraries := Seq(),
    sbt.Keys.`package` <<= packageTask)

  /** entry point for plugin in user's project */
  def activate: Seq[Project.Setting[_]] = inConfig(appConf)(pluginSettings ++ Proguard.settings ++ JavaFX.settings) ++ JavaFX.dependencySettings
  /** main task */
  def packageTask = (sbt.Keys.`package` in Compile, ApplicationKeys.proguard, ApplicationKeys.javafx, applicationLibraries) map {
    (originalArtifact, proguard, javafx, applicationLibraries) =>
      val source = proguard getOrElse originalArtifact
      source
  }
}
