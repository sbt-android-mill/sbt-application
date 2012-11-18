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

package sbt.application.javafx

import java.net.URLClassLoader

import org.apache.tools.ant.Project
import org.apache.tools.ant.taskdefs.Manifest
import org.apache.tools.ant.types.DataType
import org.apache.tools.ant.types.FileSet

class FXJarTask(val project: Project, val classLoader: URLClassLoader) extends AntTask {
  lazy val mainClass = classLoader.loadClass("com.sun.javafx.tools.ant.FXJar")
  protected lazy val createApplicationMethod = mainClass.getMethod("createApplication")
  protected lazy val createFileSetMethod = mainClass.getMethod("createFileSet")
  protected lazy val createManifestMethod = mainClass.getMethod("createManifest")
  protected lazy val createPlatformMethod = mainClass.getMethod("createPlatform")
  protected lazy val createResourcesMethod = mainClass.getMethod("createResources")
  protected lazy val setCss2BinMethod = mainClass.getMethod("setDestfile", classOf[Boolean])
  protected lazy val setDestfileMethod = mainClass.getMethod("setDestfile", classOf[String])
  protected lazy val setVerboseMethod = mainClass.getMethod("setVerbose", classOf[Boolean])
  lazy val instance = Class.forName("com.sun.javafx.tools.ant.FXJar", true, classLoader).newInstance()

  /** Set application attributes */
  def createApplication(): AntTask.Application = {
    val data = createApplicationMethod.invoke(instance).asInstanceOf[DataType]
    data.setProject(project)
    new AntTask.Application(data)
  }
  /** Base directory of the files to pack */
  def createFileSet(): FileSet = {
    val data = createFileSetMethod.invoke(instance).asInstanceOf[FileSet]
    data.setProject(project)
    data
  }
  /** Set additional manifest attributes. */
  def createManifest(): Manifest = createManifestMethod.invoke(instance).asInstanceOf[Manifest]
  /** Set platform attributes */
  def createPlatform(): AntTask.Platform = {
    val data = createPlatformMethod.invoke(instance).asInstanceOf[DataType]
    data.setProject(project)
    new AntTask.Platform(data)
  }
  /** Set artifact resources */
  def createResources(): AntTask.Resources = {
    val data = createResourcesMethod.invoke(instance).asInstanceOf[DataType]
    data.setProject(project)
    new AntTask.Resources(data)
  }
  /** The packager won't convert CSS files to binary form before copying to jar */
  def setCss2Bin(value: Boolean): Unit = setCss2BinMethod.invoke(instance, Boolean.box(value))
  /** Set artifact file */
  def setDestfile(destination: String) = setDestfileMethod.invoke(instance, destination)
  /** Enable verbose output */
  def setVerbose(verbose: Boolean): Unit = setVerboseMethod.invoke(instance, Boolean.box(verbose))
}
