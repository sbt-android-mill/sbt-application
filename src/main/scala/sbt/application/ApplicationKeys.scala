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

object ApplicationKeys {
  def appConf = config("application") extend (Compile)

  val applicationPackage = TaskKey[Option[String]]("application-package", "Defines the main package in application bundle")
  val applicationSuffix = SettingKey[String]("application-suffix")
  val applicationLibraries = TaskKey[Seq[File]]("application-libraries", "Jar libraries that will append to bundle at last")

  // JavaFX
  val javafx = TaskKey[Option[File]]("javafx", "JavaFX task")
  val javefxArtifactType = SettingKey[JavaFXArtifactType]("javafx-artifact-type", "createjar or deploy")
  val javafxAnt = TaskKey[Classpath]("javafx-ant", "Path to ant-javafx.jar, as usual JAVA_HOME/lib/ant-javafx.jar")
  val javafxArtifact = TaskKey[File]("javafx-artifact")
  val javafxRT = TaskKey[Classpath]("javafx-rt", "Path to jfxrt.jar, as usual JAVA_HOME/lib/jfxrt.jar")
  val javafxEnabled = SettingKey[Boolean]("javafx-enabled")
  val javafxSuffix = SettingKey[String]("javafx-suffix")
  val javafxTaskCSS2BIN = TaskKey[Boolean]("javafx-task-css2bin", "The packager won't convert CSS files to binary form before copying to jar.")
  val javafxTaskFileset = TaskKey[Seq[File]]("javafx-task-fileset", "Base dir of the files to pack.")

  // Proguard
  val proguard = TaskKey[Option[File]]("proguard", "ProGuard task")
  val proguardArtifact = TaskKey[File]("proguard-artifact")
  val proguardEnabled = SettingKey[Boolean]("proguard-enabled")
  val proguardInJars = TaskKey[Seq[File]]("proguard-in-jars")
  val proguardLibraryJars = TaskKey[Seq[File]]("proguard-library-jars")
  val proguardOption = TaskKey[Seq[String]]("proguard-option")
  val proguardOptimizations = SettingKey[Seq[String]]("proguard-optimizations")
  val proguardSuffix = SettingKey[String]("proguard-suffix")

  sealed trait JavaFXArtifactType
  case object FXJar extends JavaFXArtifactType
  case object FXDeploy extends JavaFXArtifactType
}
