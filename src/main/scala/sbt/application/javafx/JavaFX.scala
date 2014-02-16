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
import sbt.application.Keys._
import sbt._
import sbt.Keys._

object JavaFX {
  /** JavaFX plugin settings */
  val settings = Seq(
    javafx <<= javafxTask,
    javefxArtifactType := FXJar,
    javafxAnt <<= javafxAntTask,
    javafxArtifact <<= javafxArtifactTask,
    javafxEnabled := true,
    javafxRT <<= javafxRTTask,
    javafxSuffix := "-jfx")

  /** user's project dependency container with JavaFX */
  def dependencySettings = Seq(unmanagedJars in Compile <++= (javafxEnabled in ApplicationConf, javafxRT in ApplicationConf) map {
    case (true, javafxRT) ⇒
      if (javafxRT.isEmpty)
        sys.error("Please, provide classpath for javafx")
      javafxRT
    case (false, javafxRT) ⇒ Seq()
  })
  /** generate JavaFX artifact name*/
  def javafxArtifactTask = (javafxSuffix, sbt.Keys.`package` in Compile) map {
    (javafxSuffix, originalArtifact) ⇒
      val name = originalArtifact.getName.split("""\.""")
      new File(originalArtifact.getParent, name.dropRight(1).mkString(".") +
        Seq(javafxSuffix, name.last).mkString("."))
  }
  /** try to generate classpath for JavaFX at default location */
  def javafxRTTask: Project.Initialize[Task[Classpath]] = (streams) map {
    (streams) ⇒
      val home = new File(System.getProperty("java.home"))
      val result = if (!home.exists()) {
        streams.log.warn("Java home not exists")
        None
      } else {
        val lib = new File(home, "lib")
        if (!lib.exists()) {
          streams.log.warn("Java library path not exists")
          None
        } else {
          val javafxPath = new File(lib, "jfxrt.jar")
          if (!javafxPath.exists()) {
            streams.log.warn("jfxrt.jar at '%s' not found".format(javafxPath))
            None
          } else
            Some(Seq(javafxPath).classpath)
        }
      }
      result getOrElse {
        streams.log.warn("Try to find jfxrt.jar at " + home)
        (home.getParentFile() ** "jfxrt.jar").classpath
      }
  }
  /** */
  def javafxAntTask: Project.Initialize[Task[Classpath]] = (streams) map {
    (streams) ⇒
      val home = new File(System.getProperty("java.home"))
      val result = if (!home.exists()) {
        streams.log.warn("Java home not exists")
        None
      } else {
        val lib = new File(home, "lib")
        if (!lib.exists()) {
          streams.log.warn("Java library path not exists")
          None
        } else {
          val javafxPath = new File(lib, "ant-javafx.jar")
          if (!javafxPath.exists()) {
            streams.log.warn("ant-javafx.jar at '%s' not found".format(javafxPath))
            None
          } else
            Some(Seq(javafxPath).classpath)
        }
      }
      result getOrElse {
        streams.log.warn("Try to find ant-javafx.jar at " + home)
        (home.getParentFile() ** "ant-javafx.jar").classpath
      }
  }
  def javafxTask(): Project.Initialize[Task[Option[File]]] = (javafxAnt, javafxArtifact, javefxArtifactType,
    mainClass, sbt.Keys.`package` in Compile, proguard, streams) map {
      (javafxAnt, javafxArtifact, javefxArtifactType, mainClass, originalArtifact, proguard, streams) ⇒
        if (javafxAnt.isEmpty)
          sys.error("Path to ant-javafx.jar not defined.")
        val classLoader = new URLClassLoader(Array(javafxAnt.head.data.toURI().toURL()), getClass().getClassLoader())
        val antProject = new org.apache.tools.ant.Project()
        javefxArtifactType match {
          case FXJar ⇒
            val task = new FXJarTask(antProject, classLoader)
            task.setVerbose(true)
            task.setDestfile(javafxArtifact.getAbsolutePath())
            IO.withTemporaryDirectory {
              dir ⇒
                val source = proguard getOrElse originalArtifact
                streams.log.info("Extracting " + source.getName + " to " + dir)
                IO.unzip(source, dir)
                // set base directory
                val fileset = task.createFileSet
                fileset.setDir(dir)
                // set main class
                val application = task.createApplication
                application.setMainClass(mainClass.getOrElse { sys.error("mainClass undefined") })
                task.execute()
            }
            Some(javafxArtifact)
          case FXDeploy ⇒
            throw new UnsupportedOperationException
        }
    }
}
